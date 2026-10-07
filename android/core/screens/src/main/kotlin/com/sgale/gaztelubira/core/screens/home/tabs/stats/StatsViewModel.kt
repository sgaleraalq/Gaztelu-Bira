/*
 * Designed and developed by 2026 sgale (Sergio Galera)
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sgale.gaztelubira.core.screens.home.tabs.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.DEFENDER
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.FORWARD
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.GOALKEEPER
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.MANAGER
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.MIDFIELDER
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.UNDEFINED
import com.sgale.gaztelubira.core.domain.migration.model.player.Player
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.migration.repository.Preferences
import com.sgale.gaztelubira.core.domain.migration.repository.SeasonLocal
import com.sgale.gaztelubira.core.domain.migration.usecase.GetMatches
import com.sgale.gaztelubira.core.domain.migration.usecase.GetSeasonPlayerStats
import com.sgale.gaztelubira.core.domain.migration.usecase.GetSquad
import com.sgale.gaztelubira.core.domain.migration.usecase.SelectedSeason
import com.sgale.gaztelubira.core.domain.utils.formatDecimal
import com.sgale.gaztelubira.multiplatform.model.GBPlayerStat
import com.sgale.gaztelubira.multiplatform.model.GBPlayerStatsDetail
import com.sgale.gaztelubira.multiplatform.model.GBPosition
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.model.GBStat.BLUE_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBStat.PERCENTAGE
import com.sgale.gaztelubira.multiplatform.model.GBStatValue
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.StatsUiState
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBSeason
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBSeason.Companion.EMPTY_SEASONS
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsPlayerModal
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsPlayerModal.DismissPlayer
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsPlayerModal.ShowPlayer
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsSettings
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsSettings.ChangePunctuation
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsSettings.Hidden
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsState
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsState.Loaded.Default
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.StatsSeason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val KEEP_ALIVE_MILLIS = 5_000L

@HiltViewModel
internal class StatsViewModel @Inject constructor(
    selectedSeason: SelectedSeason,
    private val getSquad: GetSquad,
    private val getSeasonPlayerStats: GetSeasonPlayerStats,
    private val getMatches: GetMatches,
    private val seasonLocal: SeasonLocal,
    private val preferences: Preferences
) : ViewModel() {
    private val seasonId = selectedSeason()

    private val seasonStats = seasonId.map { id ->
        id?.let { loadSeasonStats(it) } ?: SeasonStats.EMPTY
    }

    private val seasons = seasonId.map { id ->
        val all = seasonLocal.getSeasons().map { StatsSeason(it.id.value, it.name) }
        all.firstOrNull { it.id == id?.value }
            ?.let { GBSeason(selectedSeason = it, seasons = all) }
            ?: EMPTY_SEASONS
    }

    private val selectedStat = MutableStateFlow(PERCENTAGE)
    private val punctuation = MutableStateFlow(GBPunctuation())
    private val screen = MutableStateFlow(StatsControls())

    internal val state: StateFlow<StatsUiState> =
        combine(
            seasonStats,
            seasons,
            selectedStat,
            punctuation,
            screen
        ) { season, seasons, stat, punctuation, screen ->
            StatsUiState(
                state = screen.state,
                selectedStat = stat,
                players = StatsCalculator.rank(season, stat, punctuation)
                    .map { it.toGBPlayerStat(stat) },
                punctuation = punctuation,
                selectedPlayer = screen.selectedPlayer?.let { season.toDetail(it, punctuation) },
                seasons = seasons,
                settings = screen.settings
            )
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(KEEP_ALIVE_MILLIS),
            initialValue = StatsUiState()
        )

    internal fun onChangeState(newState: GBStatsState) {
        screen.update { it.copy(state = newState) }
    }

    internal fun onChangeSeason(newSeason: String) {
        onChangeState(Default)
        viewModelScope.launch {
            withContext(IO) { preferences.selectSeason(SeasonId(newSeason)) }
        }
    }

    internal fun onStatSelected(stat: GBStat) {
        selectedStat.value = stat
    }

    internal fun onPunctuationDraftChanged(punctuation: GBPunctuation) {
        screen.update { it.copy(settings = ChangePunctuation(punctuation)) }
    }

    /**
     * Only now does the leaderboard get rescored: the sliders were editing a draft.
     */
    internal fun onPunctuationConfirmed(punctuation: GBPunctuation) {
        this.punctuation.value = punctuation
    }

    internal fun onSettingsChanged(settings: GBStatsSettings) {
        screen.update { it.copy(settings = settings) }
    }

    internal fun onPlayerAction(action: GBStatsPlayerModal) {
        val selectedPlayer = when (action) {
            DismissPlayer -> null
            is ShowPlayer -> PlayerId(action.id)
        }
        screen.update { it.copy(selectedPlayer = selectedPlayer) }
    }

    private suspend fun loadSeasonStats(seasonId: SeasonId) =
        SeasonStats(
            squad = getSquad(seasonId),
            stats = getSeasonPlayerStats(seasonId).groupBy { it.playerId },
            lastMatchId = getMatches(seasonId).maxByOrNull { it.information.date }?.id
        )

    private fun RankedPlayer.toGBPlayerStat(stat: GBStat): GBPlayerStat =
        GBPlayerStat(
            id = player.player.id.value,
            name = player.player.displayName(),
            dorsal = player.dorsal,
            faceImage = player.player.faceImage,
            bodyImage = player.player.bodyImage,
            position = player.position.toGBPosition(),
            value = formatDecimal(value),
            changedPosition = changedPosition
        )

    private fun SeasonStats.toDetail(
        id: PlayerId,
        punctuation: GBPunctuation
    ): GBPlayerStatsDetail? {
        val player = squad.find { it.player.id == id }?.player ?: return null
        val playerStats = stats[id].orEmpty()

        return GBPlayerStatsDetail(
            name = player.displayName(),
            faceImage = player.faceImage,
            stats = GBStat.entries.filter { it != BLUE_CARDS }.map { stat ->
                val value = formatDecimal(StatsCalculator.total(playerStats, stat, punctuation))
                GBStatValue(stat, if (stat == PERCENTAGE) "$value %" else value)
            }
        )
    }

    private fun Player.displayName(): String =
        nickname ?: name

    private fun Position.toGBPosition() =
        when (this) {
            UNDEFINED -> GBPosition.UNDEFINED
            MANAGER -> GBPosition.MANAGER
            GOALKEEPER -> GBPosition.GOALKEEPER
            DEFENDER -> GBPosition.DEFENDER
            MIDFIELDER -> GBPosition.MIDFIELDER
            FORWARD -> GBPosition.FORWARD
        }

    private data class StatsControls(
        val state: GBStatsState = Default,
        val settings: GBStatsSettings = Hidden,
        val selectedPlayer: PlayerId? = null
    )
}

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

package com.sgale.gaztelubira.core.screens.insert.match

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.common.utils.TAG
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.AlreadyExists
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.NoSeasonSelected
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.Unknown
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Inserted.Document
import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.match.MatchCompetition.League
import com.sgale.gaztelubira.core.domain.model.player.Position.MANAGER
import com.sgale.gaztelubira.core.domain.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.repository.SeasonLocal
import com.sgale.gaztelubira.core.domain.usecase.GetMatches
import com.sgale.gaztelubira.core.domain.usecase.GetSquad
import com.sgale.gaztelubira.core.domain.usecase.GetTeams
import com.sgale.gaztelubira.core.domain.usecase.InsertMatch
import com.sgale.gaztelubira.core.domain.usecase.SelectedSeason
import com.sgale.gaztelubira.core.domain.utils.GazteluBiraUtils.GAZTELU_BIRA
import com.sgale.gaztelubira.core.domain.utils.GazteluBiraUtils.GAZTELU_BIRA_ID
import com.sgale.gaztelubira.core.navigation.Destination.Home
import com.sgale.gaztelubira.core.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.mapper.InsertMatchMapper.supportedFormations
import com.sgale.gaztelubira.core.screens.mapper.InsertMatchMapper.toMatch
import com.sgale.gaztelubira.core.screens.mapper.InsertMatchMapper.toMatchPlayers
import com.sgale.gaztelubira.core.screens.mapper.PlayerUiMapper.toGBPlayer
import com.sgale.gaztelubira.core.screens.mapper.TeamUiMapper.toGBTeam
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Default
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Loading
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.match.pick
import com.sgale.gaztelubira.multiplatform.ui.insert.match.remove
import com.sgale.gaztelubira.multiplatform.ui.insert.match.show
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchPlayerTarget
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection
import com.sgale.gaztelubira.multiplatform.ui.insert.match.toggle
import com.sgale.gaztelubira.multiplatform.ui.insert.match.update
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_error_no_season_selected
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_error_already_exists
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_error_inserting
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_error_inserting_reason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getString
import javax.inject.Inject

/**
 * The rules of the form — what each edit does to the state — are shared with iOS and live next to
 * [InsertMatchUiState]. What is left here is what only the platform can do: loading the season the
 * match belongs to, and publishing it.
 */
@HiltViewModel
internal class InsertMatchViewModel @Inject constructor(
    private val selectedSeason: SelectedSeason,
    private val seasonLocal: SeasonLocal,
    private val getTeams: GetTeams,
    private val getSquad: GetSquad,
    private val getMatches: GetMatches,
    private val insertNewMatch: InsertMatch
) : ViewModel() {

    private val _state = MutableStateFlow(
        InsertMatchUiState(
            team = GAZTELU_BIRA.toGBTeam(),
            formations = supportedFormations
        )
    )
    internal val state: StateFlow<InsertMatchUiState> = _state.asStateFlow()

    /**
     * The season the form was loaded for and the matches it already has: the id of the new match
     * is worked out from both.
     */
    private var season: SeasonId? = null
    private var seasonMatches: List<Match> = emptyList()

    init {
        viewModelScope.launch {
            try {
                val loaded = withContext(IO) { loadSeason() } ?: return@launch
                _state.update { loaded(it) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Error loading the season of the new match: $e")
            }
        }
    }

    internal fun updateField(field: InsertMatchField) {
        _state.update { it.update(field) }
    }

    internal fun updateDialogState(dialog: InsertMatchDialog) {
        _state.update { it.show(dialog) }
    }

    internal fun toggleSection(section: InsertMatchSection) {
        _state.update { it.toggle(section) }
    }

    internal fun pickPlayer(player: GBPlayer) {
        _state.update { it.pick(player) }
    }

    internal fun removePlayer(target: InsertMatchPlayerTarget, index: Int) {
        _state.update { it.remove(target, index) }
    }

    /**
     * @param onFailure Receives the message to show. The form keeps its fields so the user can
     * retry.
     */
    internal fun insertMatch(
        navState: NavigationState,
        onFailure: (String) -> Unit
    ) {
        val form = _state.value
        if (!form.handler.isValid) return

        viewModelScope.launch {
            _state.update { it.copy(state = Loading) }
            val result = withContext(IO) { publish(form) }
            if (result is Document) {
                navState.navigateTo(Home, true)
            } else {
                onFailure(result.errorMessage())
                _state.update { it.copy(state = Default) }
            }
        }
    }

    private suspend fun publish(form: InsertMatchUiState): FirebaseInsertResult {
        val season = season ?: return NoSeasonSelected
        val match = form.toMatch(season, seasonMatches)
        return insertNewMatch(match, form.toMatchPlayers(match))
    }

    /**
     * @return How to fill the form in with the season, or null when there is no season to load.
     */
    private suspend fun loadSeason(): ((InsertMatchUiState) -> InsertMatchUiState)? {
        val seasonId = selectedSeason().first() ?: return null
        val teams = getTeams(seasonId)
        val squad = getSquad(seasonId)
        val matches = getMatches(seasonId)
        val seasonName = seasonLocal.getSeasons().firstOrNull { it.id == seasonId }?.name.orEmpty()

        season = seasonId
        seasonMatches = matches

        val journey = matches.maxOfOrNull { (it.competition as? League)?.journey ?: 0 } ?: 0
        return { form ->
            form.copy(
                seasonName = seasonName,
                journey = journey + 1,
                team = teams.firstOrNull { it.id.value == GAZTELU_BIRA_ID }?.toGBTeam() ?: form.team,
                rivals = teams.filter { it.id.value != GAZTELU_BIRA_ID }
                    .sortedBy { it.name }
                    .map { it.toGBTeam() },
                players = squad.filter { it.position != MANAGER }.map { it.toGBPlayer() },
                staff = squad.filter { it.position == MANAGER }.map { it.toGBPlayer() }
            )
        }
    }

    private suspend fun FirebaseInsertResult.errorMessage(): String =
        when (this) {
            is AlreadyExists -> getString(Res.string.insert_match_error_already_exists)
            is NoSeasonSelected -> getString(Res.string.insert_error_no_season_selected)
            is Unknown -> if (message.isNotBlank()) {
                getString(Res.string.insert_match_error_inserting_reason, message)
            } else {
                getString(Res.string.insert_match_error_inserting)
            }
            else -> getString(Res.string.insert_match_error_inserting)
        }
}

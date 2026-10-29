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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.AlreadyExists
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.NoSeasonSelected
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.Unknown
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Inserted.Document
import com.sgale.gaztelubira.core.domain.model.match.MatchCompetition.Cup
import com.sgale.gaztelubira.core.domain.model.match.MatchCompetition.League
import com.sgale.gaztelubira.core.domain.model.player.Position.MANAGER
import com.sgale.gaztelubira.core.domain.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.usecase.GetMatches
import com.sgale.gaztelubira.core.domain.usecase.GetSquad
import com.sgale.gaztelubira.core.domain.usecase.GetTeams
import com.sgale.gaztelubira.core.domain.usecase.InsertMatch
import com.sgale.gaztelubira.core.domain.usecase.SelectedSeason
import com.sgale.gaztelubira.core.domain.utils.GazteluBiraUtils.GAZTELU_BIRA
import com.sgale.gaztelubira.core.domain.utils.toDate
import com.sgale.gaztelubira.core.screens.mapper.InsertMatchUiMapper.toMatch
import com.sgale.gaztelubira.core.screens.mapper.InsertMatchUiMapper.toMatchPlayers
import com.sgale.gaztelubira.core.screens.mapper.PlayerUiMapper.toGBPlayer
import com.sgale.gaztelubira.core.screens.mapper.TeamUiMapper.toGBTeam
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Default
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Inserted
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Loading
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog.None
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection
import com.sgale.gaztelubira.multiplatform.ui.insert.match.update
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_error_no_season_selected
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_error_already_exists
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_error_inserting
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_error_inserting_reason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import java.lang.System.currentTimeMillis
import javax.inject.Inject

private const val DEFAULT_LOCATION = "Pamplona, España"

@HiltViewModel
internal class InsertMatchViewModel @Inject constructor(
    private val selectedSeason: SelectedSeason,
    private val getMatches: GetMatches,
    private val getSquad: GetSquad,
    private val getTeams: GetTeams,
    private val insertNewMatch: InsertMatch
) : ViewModel() {
    private val date = currentTimeMillis()
    private var season: SeasonId? = null
    private var cupMatches = 0

    private val _state = MutableStateFlow(
        InsertMatchUiState(
            date = date.toDate(),
            location = DEFAULT_LOCATION
        )
    )
    internal val state: StateFlow<InsertMatchUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch { loadSeason() }
    }

    /**
     * The next journey follows the last league one stored; cups are only counted, for their id.
     */
    private suspend fun loadSeason() {
        val seasonId = selectedSeason().first() ?: return
        val teams = getTeams(seasonId)
        val squad = getSquad(seasonId)
        val matches = getMatches(seasonId)

        season = seasonId
        cupMatches = matches.count { it.competition is Cup }

        _state.update { state ->
            state.copy(
                journey = (matches.mapNotNull { (it.competition as? League)?.journey }.maxOrNull() ?: 0) + 1,
                team = (teams.firstOrNull { it.isGaztelu() } ?: GAZTELU_BIRA).toGBTeam(),
                rivals = teams.filterNot { it.isGaztelu() }.map { it.toGBTeam() },
                squad = squad.filterNot { it.position == MANAGER }.map { it.toGBPlayer() },
                staff = squad.filter { it.position == MANAGER }.map { it.toGBPlayer() }
            )
        }
    }

    internal fun updateField(field: InsertMatchField) {
        _state.update { it.update(field) }
    }

    internal fun changeSection(section: InsertMatchSection) {
        _state.update { it.copy(section = section, dialog = None) }
    }

    internal fun updateDialog(dialog: InsertMatchDialog) {
        _state.update { it.copy(dialog = dialog) }
    }

    internal fun insertMatch() {
        val form = _state.value
        if (!form.handler.isValid) return

        viewModelScope.launch {
            _state.update { it.copy(state = Loading) }

            val seasonId = season
            val result = if (seasonId == null) {
                NoSeasonSelected
            } else {
                val match = form.toMatch(seasonId, date, cupNumber = cupMatches + 1)
                insertNewMatch(match, form.toMatchPlayers(match))
            }

            _state.update {
                it.copy(
                    state = if (result is Document) Inserted else InsertingDataState.Error(result.errorMessage())
                )
            }
        }
    }

    /**
     * Once the screen has shown the error, the form is back to editable with everything kept.
     */
    internal fun dismissError() {
        _state.update { it.copy(state = Default) }
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

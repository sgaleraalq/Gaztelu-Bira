/*
 * Designed and developed by 2025 sgaleraalq (Sergio Galera)
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

package com.sgale.gaztelubira.core.screens.insert_match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.domain.model.player.Stat.Assists
import com.sgale.gaztelubira.core.domain.model.player.Stat.CleanSheets
import com.sgale.gaztelubira.core.domain.model.player.Stat.Fails
import com.sgale.gaztelubira.core.domain.model.player.Stat.GamesPlayed
import com.sgale.gaztelubira.core.domain.model.player.Stat.Goals
import com.sgale.gaztelubira.core.domain.model.player.Stat.GoalsProvoked
import com.sgale.gaztelubira.core.domain.model.player.Stat.PenaltiesProvoked
import com.sgale.gaztelubira.core.domain.model.player.Stat.Percentage
import com.sgale.gaztelubira.core.domain.model.player.Stat.RedCards
import com.sgale.gaztelubira.core.domain.model.player.Stat.Saves
import com.sgale.gaztelubira.core.domain.model.player.Stat.YellowCards
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult.StatsInserted
import com.sgale.gaztelubira.core.domain.usecase.db.GetPlayers
import com.sgale.gaztelubira.core.domain.usecase.db.GetTeams
import com.sgale.gaztelubira.core.domain.usecase.firestore.insert.InsertNewMatch
import com.sgale.gaztelubira.core.domain.utils.IToastManager
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchFormation
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchFormation.ManagerPosition
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchFormation.ManagerPosition.First
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchFormation.ManagerPosition.Second
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchInformation
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchState
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchState.Default
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchState.Loading
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchStats
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchTeamsInformation
import com.sgale.gaztelubira.core.screens.insert_match.data.PlayerState
import com.sgale.gaztelubira.core.screens.insert_match.data.PlayerState.Bench
import com.sgale.gaztelubira.core.screens.insert_match.data.PlayerState.LineUp
import com.sgale.gaztelubira.core.screens.insert_match.data.PlayerState.Manager
import com.sgale.gaztelubira.core.screens.insert_match.data.PlayerState.StatPlayer
import com.sgale.gaztelubira.core.screens.insert_match.data.isValid
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class InsertMatchViewModel @Inject constructor(
    private val getPlayers: GetPlayers,
    private val getTeams: GetTeams,
    private val toastManager: IToastManager,
    private val insertNewMatch: InsertNewMatch
) : ViewModel() {
    data class InsertMatchPosition(val position: Int = 0)

    private val position = MutableStateFlow(InsertMatchPosition())

    /**
     * Fixed values
     */
    private val _teams = MutableStateFlow<List<TeamModel>>(emptyList())
    val teams: StateFlow<List<TeamModel>> = _teams
    private val _players = MutableStateFlow<List<PlayerModel>>(emptyList())
    val players: StateFlow<List<PlayerModel>> = _players

    init {
        viewModelScope.launch {
            _teams.value = withContext(Dispatchers.IO) {
                getTeams().first()
            }
            _players.value = withContext(Dispatchers.IO) {
                getPlayers().first()
                    .sortedBy { it.dorsal }
                    .filter { it.dorsal != null }
            }
        }
    }

    /**
     * UI State
     */
    private val _uiState = MutableStateFlow<InsertMatchState>(Default)
    val uiState: StateFlow<InsertMatchState> = _uiState

    fun updateState(newState: InsertMatchState) {
        _uiState.value = newState
    }

    /**
     * Insert Match Formation
     */
    private val _formationState = MutableStateFlow(InsertMatchFormation())
    val formationState: StateFlow<InsertMatchFormation> = _formationState

    fun changeFormation(newFormation: LineUpFormation) {
        _formationState.value = _formationState.value.copy(
            formation = newFormation
        )
    }

    fun changeSelectedManager(position: ManagerPosition) {
        _formationState.value = _formationState.value.copy(
            selectedManager = position
        )
    }

    fun changeSelectedPosition(position: LineUpPosition) {
        _formationState.value = _formationState.value.copy(
            selectedPosition = position
        )
    }

    fun updatePlayerState(newState: PlayerState) {
        _formationState.value = _formationState.value.copy(
            state = newState
        )
    }

    fun insertPlayer(player: PlayerModel) {
        val state = _formationState.value.state
        when (state) {
            Bench -> insertBenchPlayer(player)
            LineUp -> insertLineUpPlayer(player, position.value.position)
            Manager -> insertManager(player)
            StatPlayer -> addStat(player)
        }
    }

    fun removePlayer(player: PlayerModel) {
        val state = _formationState.value.state
        when (state) {
            Bench -> removeBenchPlayer(player)
            LineUp -> removeLineUpPlayer(player)
            Manager -> removeManager()
            StatPlayer -> Unit
        }
    }

    fun selectPlayer(playerIdx: Int) {
        position.value = position.value.copy(position = playerIdx)
    }

    private fun insertBenchPlayer(player: PlayerModel) {
        _formationState.value = _formationState.value.copy(
            benchPlayers = _formationState.value.benchPlayers.toMutableList().apply {
                add(player)
            }
        )
    }

    private fun insertManager(manager: PlayerModel) {
        _formationState.value = _formationState.value.copy(
            managers = when (_formationState.value.selectedManager) {
                First -> Pair(manager, _formationState.value.managers.second)
                Second -> Pair(_formationState.value.managers.first, manager)
            }
        )
    }

    private fun insertLineUpPlayer(player: PlayerModel, position: Int) {
        val lineUp = _formationState.value.lineUp.toMutableMap()
        lineUp[position] = player
        _formationState.value = _formationState.value.copy(
            lineUp = lineUp.toMap()
        )
    }

    private fun removeBenchPlayer(player: PlayerModel) {
        _formationState.value = _formationState.value.copy(
            benchPlayers = _formationState.value.benchPlayers.toMutableList().apply {
                remove(player)
            }
        )
    }

    private fun removeLineUpPlayer(player: PlayerModel) {
        val lineUp = _formationState.value.lineUp.toMutableMap()
        lineUp.forEach {
            if (it.value == player) {
                lineUp.remove(it.key)
            }
        }
        _formationState.value = _formationState.value.copy(
            lineUp = lineUp.toMap()
        )
    }

    fun removeManager() {
        _formationState.value = _formationState.value.copy(
            managers = when (_formationState.value.selectedManager) {
                First -> Pair(null, _formationState.value.managers.second)
                Second -> Pair(_formationState.value.managers.first, null)
            }
        )
    }

    /**
     * Insert Match Information
     */

    private val _informationState = MutableStateFlow(InsertMatchInformation())
    val informationState: StateFlow<InsertMatchInformation> = _informationState

    fun updateDescription(newDescription: String) {
        _informationState.value = _informationState.value.copy(
            description = newDescription
        )
    }

    fun updateLocation(newLocation: String) {
        _informationState.value = _informationState.value.copy(
            location = newLocation
        )
    }


    /**
     * Insert Match Stats
     */
    private val _statsState = MutableStateFlow(InsertMatchStats())
    val statsState: StateFlow<InsertMatchStats> = _statsState

    fun selectStat(stat: Stat) {
        _statsState.value = _statsState.value.copy(
            selectedStat = stat
        )
    }

    fun addStat(value: PlayerModel) {
        val stat = _statsState.value.selectedStat ?: return
        if (!stat.isInsertable) return
        _statsState.value = when (stat) {
            Assists -> _statsState.value.copy(
                assists = _statsState.value.assists + value
            )

            CleanSheets -> _statsState.value.copy(
                cleanSheets = _statsState.value.cleanSheets + value
            )

            Fails -> _statsState.value.copy(
                fails = _statsState.value.fails + value
            )

            Goals -> _statsState.value.copy(
                goals = _statsState.value.goals + value
            )

            GoalsProvoked -> _statsState.value.copy(
                goalsProvoked = _statsState.value.goalsProvoked + value
            )

            PenaltiesProvoked -> _statsState.value.copy(
                penaltiesProvoked = _statsState.value.penaltiesProvoked + value
            )

            RedCards -> _statsState.value.copy(
                redCards = _statsState.value.redCards + value
            )

            Saves -> _statsState.value.copy(
                saves = _statsState.value.saves + value
            )

            YellowCards -> _statsState.value.copy(
                yellowCards = _statsState.value.yellowCards + value
            )

            GamesPlayed, Percentage -> _statsState.value
        }
    }

    fun removeStat(idx: Int, stat: Stat) {
        if (!stat.isInsertable) return

        _statsState.value = when (stat) {
            Assists -> _statsState.value.copy(
                assists = _statsState.value.assists.toMutableList().apply {
                    if (idx in indices) removeAt(idx)
                }
            )

            CleanSheets -> _statsState.value.copy(
                cleanSheets = _statsState.value.cleanSheets.toMutableList().apply {
                    if (idx in indices) removeAt(idx)
                }
            )

            Fails -> _statsState.value.copy(
                fails = _statsState.value.fails.toMutableList().apply {
                    if (idx in indices) removeAt(idx)
                }
            )

            Goals -> _statsState.value.copy(
                goals = _statsState.value.goals.toMutableList().apply {
                    if (idx in indices) removeAt(idx)
                }
            )

            GoalsProvoked -> _statsState.value.copy(
                goalsProvoked = _statsState.value.goalsProvoked.toMutableList().apply {
                    if (idx in indices) removeAt(idx)
                }
            )

            PenaltiesProvoked -> _statsState.value.copy(
                penaltiesProvoked = _statsState.value.penaltiesProvoked.toMutableList().apply {
                    if (idx in indices) removeAt(idx)
                }
            )

            RedCards -> _statsState.value.copy(
                redCards = _statsState.value.redCards.toMutableList().apply {
                    if (idx in indices) removeAt(idx)
                }
            )

            Saves -> _statsState.value.copy(
                saves = _statsState.value.saves.toMutableList().apply {
                    if (idx in indices) removeAt(idx)
                }
            )

            YellowCards -> _statsState.value.copy(
                yellowCards = _statsState.value.yellowCards.toMutableList().apply {
                    if (idx in indices) removeAt(idx)
                }
            )

            GamesPlayed, Percentage -> _statsState.value
        }
    }

    fun insertMatch(
        matchInformation: InsertMatchTeamsInformation,
        state: NavigationState
    ) {
        val matchModel = matchInformation.toMatchModel()
        val goalsScored = matchInformation.getGoalsScored()
        val goalsAgainst = matchInformation.getGoalsReceived()
        if (!isValid(goalsScored, goalsAgainst, matchInformation)) {
            showToast()
            return
        }

        viewModelScope.launch {
            _uiState.value = Loading
            val matchInserted = withContext(Dispatchers.IO) {
                insertNewMatch(
                    match = matchModel,
                    matchStats = createMatchStatsModel(matchInformation)
                )
            }

            if (matchInserted is StatsInserted) {
                state.navigateBack()
            } else {
                showErrorToast()
            }
            _uiState.value = Default
        }
    }

    private fun createMatchStatsModel(match: InsertMatchTeamsInformation) =
        MatchStatsModel(
            id = match.id,
            location = _informationState.value.location,
            description = _informationState.value.description,
            matchModel = match.toMatchModel(),
            formation = _formationState.value.formation.formation,
            lineUpPlayers = _formationState.value.getLineUpPlayers(),
            benchPlayers = _formationState.value.benchPlayers,
            managers = _formationState.value.managers.toList().filterNotNull(),
            stats = _statsState.value.toStatsMatchModel()
        )

    private fun isValid(
        goalsScored: Int,
        goalsAgainst: Int,
        matchInformation: InsertMatchTeamsInformation
    ): Boolean {
        val information = _informationState.value
        val formation = _formationState.value
        val stats = _statsState.value

        val validMatch = matchInformation.validMatch()
        val validInformation = information.isValid()
        val validFormation = formation.isValid()
        val validStats = stats.isValid(goalsScored, goalsAgainst)

        return validMatch && validInformation && validFormation && validStats
    }

    private fun showErrorToast() {
        toastManager.showToast("Error inserting match")
    }

    private fun showToast() {
        toastManager.showToast("Please fill in all the fields")
    }
}

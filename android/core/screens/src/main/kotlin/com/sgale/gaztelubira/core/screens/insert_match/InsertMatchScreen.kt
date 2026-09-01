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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.designsystem.components.GBScaffold
import com.sgale.gaztelubira.core.designsystem.model.LineUpPosition.Manager
import com.sgale.gaztelubira.core.screens.LocalMainViewModel
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchState.Default
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchState.Formation
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchState.Information
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchState.Loading
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchState.Stats
import com.sgale.gaztelubira.core.screens.insert_match.data.PlayerState.StatPlayer
import com.sgale.gaztelubira.core.screens.insert_match.ui.InsertMatchDialogPlayers
import com.sgale.gaztelubira.core.screens.insert_match.ui.InsertMatchDialogStatsPlayers
import com.sgale.gaztelubira.core.screens.insert_match.ui.InsertMatchFormation
import com.sgale.gaztelubira.core.screens.insert_match.ui.InsertMatchFormationButton
import com.sgale.gaztelubira.core.screens.insert_match.ui.InsertMatchInformation
import com.sgale.gaztelubira.core.screens.insert_match.ui.InsertMatchInformationButton
import com.sgale.gaztelubira.core.screens.insert_match.ui.InsertMatchScaffold
import com.sgale.gaztelubira.core.screens.insert_match.ui.InsertMatchStats
import com.sgale.gaztelubira.core.screens.insert_match.ui.InsertMatchStatsButton
import androidx.compose.ui.res.stringResource

@Composable
internal fun InsertMatchScreen(
    state: NavigationState,
    viewModel: InsertMatchViewModel = hiltViewModel<InsertMatchViewModel>()
) {
    val mainViewModel = LocalMainViewModel.current
    var showPlayers by rememberSaveable { mutableStateOf(false) }
    var showStatsPlayers by rememberSaveable { mutableStateOf(false) }
    val user by mainViewModel.userSession.collectAsStateWithLifecycle()

    val teams by viewModel.teams.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val players by viewModel.players.collectAsStateWithLifecycle()

    /**
     * States
     */
    val statsState by viewModel.statsState.collectAsStateWithLifecycle()
    val formationState by viewModel.formationState.collectAsStateWithLifecycle()
    val informationState by viewModel.informationState.collectAsStateWithLifecycle()

    GBScaffold(
        appTeam = user?.team,
        showTopAppBar = true,
        topBarTitle = stringResource(R.string.insert_match)
    ) { modifier ->
        when (uiState) {
            Default, Loading -> {
                InsertMatchScreenUI(
                    modifier = modifier,
                    team = user?.team,
                    teams = teams,
                    loading = uiState is Loading,
                    changeState = { viewModel.updateState(it) },
                    insertMatch = { viewModel.insertMatch(it, state) }
                )
            }

            Formation -> {
                InsertMatchScaffold(
                    modifier = modifier,
                    changeState = { viewModel.updateState(it) },
                    button = { InsertMatchFormationButton { viewModel.updateState(Default) } },
                    content = { contentModifier ->
                        InsertMatchFormation(
                            modifier = contentModifier,
                            formationState = formationState,
                            showPlayers = { showPlayers = true },
                            changePlayerState = { viewModel.updatePlayerState(it) },
                            removePlayer = { viewModel.removePlayer(it) },
                            removeManager = { viewModel.removeManager() },
                            onPlayerSelected = { showPlayers = true; viewModel.selectPlayer(it) },
                            changeFormationSelected = { viewModel.changeFormation(it) },
                            changeManagerSelected = { viewModel.changeSelectedManager(it) },
                            changeSelectedPosition = { viewModel.changeSelectedPosition(it) }
                        )
                    }
                )
            }

            Information -> {
                InsertMatchScaffold(
                    modifier = modifier,
                    changeState = { viewModel.updateState(it) },
                    button = { InsertMatchInformationButton { viewModel.updateState(Default) } },
                    content = {
                        InsertMatchInformation(
                            modifier = it,
                            insertMatchInformation = informationState,
                            updateDescription = { description -> viewModel.updateDescription(description) },
                            updateLocation = { location -> viewModel.updateLocation(location) }
                        )
                    }
                )
            }

            Stats -> {
                InsertMatchScaffold(
                    modifier = modifier,
                    changeState = { viewModel.updateState(it) },
                    button = { InsertMatchStatsButton { viewModel.updateState(Default) } },
                    content = {
                        InsertMatchStats(
                            modifier = it,
                            statsState = statsState,
                            changePlayerState = { viewModel.updatePlayerState(StatPlayer) },
                            showPlayers = { showStatsPlayers = true },
                            selectStat = { stat -> viewModel.selectStat(stat) },
                            removeStat = { idx, stat -> viewModel.removeStat(idx, stat) }
                        )
                    }
                )
            }
        }

        if (showPlayers) {
            InsertMatchDialogPlayers(
                playerPosition = formationState.selectedPosition,
                isManager = formationState.selectedPosition == Manager,
                players = players,
                formation = formationState,
                onPlayerSelected = { player -> viewModel.insertPlayer(player) },
                dismiss = { showPlayers = false }
            )
        }

        if (showStatsPlayers) {
            InsertMatchDialogStatsPlayers(
                statsState = statsState,
                players = players,
                formationState = formationState,
                onPlayerSelected = { player -> viewModel.insertPlayer(player) },
                dismiss = { showStatsPlayers = false }
            )
        }
    }
}

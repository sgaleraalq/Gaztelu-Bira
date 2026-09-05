/*
 * Designed and developed by 2026 sgaleraalq (Sergio Galera)
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

package com.sgale.gaztelubira.core.screens.insert_match.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchFormation
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchFormation.ManagerPosition
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchFormation.ManagerPosition.First
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchFormation.ManagerPosition.Second
import com.sgale.gaztelubira.core.screens.insert_match.data.PlayerState
import com.sgale.gaztelubira.core.screens.insert_match.data.PlayerState.Bench
import com.sgale.gaztelubira.core.screens.insert_match.data.PlayerState.LineUp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBAddButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBFootballField
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBPlayerCard
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation.Companion.ALL_FORMATIONS
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.Manager
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.core.screens.toGBPlayer

private val BENCH_PLAYER_SIZE = 70.dp

@Composable
internal fun InsertMatchFormation(
    modifier: Modifier,
    formationState: InsertMatchFormation,
    showPlayers: () -> Unit,
    changePlayerState: (PlayerState) -> Unit,
    removePlayer: (PlayerModel) -> Unit,
    removeManager: () -> Unit,
    onPlayerSelected: (Int) -> Unit,
    changeFormationSelected: (LineUpFormation) -> Unit,
    changeManagerSelected: (ManagerPosition) -> Unit,
    changeSelectedPosition: (LineUpPosition) -> Unit
) {
    FormationPossibilities(
        formationState = formationState,
        changeFormationSelected = { changeFormationSelected(it) }
    )
    LineUpPlayers(
        modifier = modifier,
        formationState = formationState,
        onPlayerSelected = { changePlayerState(LineUp); onPlayerSelected(it) },
        changeSelectedPosition = { changeSelectedPosition(it) }
    )
    NonLineUpPlayers(
        formationState = formationState,
        changePlayerState = { changePlayerState(it) },
        removePlayer = { removePlayer(it) },
        showPlayers = { showPlayers() },
        changeManagerSelected = { changeManagerSelected(it) },
        changeSelectedPosition = { changeSelectedPosition(it) },
        removeManager = { removeManager() }
    )
}

@Composable
internal fun FormationPossibilities(
    formationState: InsertMatchFormation,
    changeFormationSelected: (LineUpFormation) -> Unit
) {
    val formations = remember { ALL_FORMATIONS }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(12.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(
            items = formations,
            key = { it.formation }
        ) { formation ->
            InsertMatchFormationButton(
                text = formation.formation,
                isSelected = formation == formationState.formation,
                onClick = { changeFormationSelected(formation) }
            )
        }
    }
}

@Composable
internal fun LineUpPlayers(
    modifier: Modifier,
    formationState: InsertMatchFormation,
    onPlayerSelected: (Int) -> Unit,
    changeSelectedPosition: (LineUpPosition) -> Unit
) {
    GBFootballField(
            fieldImage = AppImages.footballField,
        modifier = modifier.fillMaxWidth(),
        showAnimation = true,
        players = formationState.lineUp.mapValues { entry -> entry.value?.toGBPlayer() },
        formation = formationState.formation,
        onPlayerSelected = { position, player ->
            changeSelectedPosition(position)
            onPlayerSelected(player)
        }
    )
}


@Composable
private fun NonLineUpPlayers(
    formationState: InsertMatchFormation,
    changePlayerState: (PlayerState) -> Unit,
    removePlayer: (PlayerModel) -> Unit,
    showPlayers: () -> Unit,
    changeManagerSelected: (ManagerPosition) -> Unit,
    changeSelectedPosition: (LineUpPosition) -> Unit,
    removeManager: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(12.dp)
    ) {
        BenchPlayers(
            modifier = Modifier.weight(1f),
            formationState = formationState,
            changePlayerState = { changePlayerState(it) },
            removePlayer = { removePlayer(it) },
            showPlayers = { showPlayers() },
            changeSelectedPosition = { changeSelectedPosition(it) }
        )
        VerticalDivider(Modifier.height(BENCH_PLAYER_SIZE+10.dp))
        ManagersPlayers(
            managers = formationState.managers,
            changePlayerState = { changePlayerState(it) },
            changeManagerSelected = { changeManagerSelected(it) },
            changeSelectedPosition = { changeSelectedPosition(it) },
            showPlayers = { showPlayers() },
            removeManager = { removeManager() }
        )
    }
}

/**
 * BENCH
 */
@Composable
private fun BenchPlayers(
    modifier: Modifier,
    formationState: InsertMatchFormation,
    changePlayerState: (PlayerState) -> Unit,
    removePlayer: (PlayerModel) -> Unit,
    showPlayers: () -> Unit,
    changeSelectedPosition: (LineUpPosition) -> Unit
) {
    Column(modifier) {
        BenchTitle()
        Bench(
            formationState = formationState,
            showPlayers = { changePlayerState(Bench); showPlayers() },
            removeBenchPlayer = { changePlayerState(Bench); removePlayer(it) },
            changeSelectedPosition = { changeSelectedPosition(it) }
        )
    }
}

@Composable
internal fun BenchTitle() {
    GBText(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        text = stringResource(R.string.bench),
        style = gBTypography().bodyLarge
    )
}

@Composable
private fun Bench(
    formationState: InsertMatchFormation,
    showPlayers: () -> Unit,
    removeBenchPlayer: (PlayerModel) -> Unit,
    changeSelectedPosition: (LineUpPosition) -> Unit
) {
    if (formationState.benchPlayers.isEmpty()) {
        Box(Modifier.height(BENCH_PLAYER_SIZE), contentAlignment = Center) {
            GBElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.insert_bench_player),
                onClick = {
                    changeSelectedPosition(LineUpPosition.Bench)
                    showPlayers()
                }
            )
        }
    } else {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = spacedBy(12.dp),
        ) {
            items(
                items = formationState.benchPlayers,
                key = { it.id }
            ) { player ->
                GBPlayerCard(
                    modifier = Modifier.size(BENCH_PLAYER_SIZE),
                    player = player.toGBPlayer(),
                    showDorsal = false,
                    showDeletion = true,
                    onPlayerRemoved = { removeBenchPlayer(player) }
                )
            }
            if (formationState.benchPlayers.size < 6) {
                item {
                    AddBenchPlayerButton { showPlayers() }
                }
            }
        }
    }
}

@Composable
private fun AddBenchPlayerButton(
    onButtonClicked: () -> Unit
) {
    GBAddButton(
        modifier = Modifier.height(BENCH_PLAYER_SIZE)
    ) { onButtonClicked() }
}


/**
 * MANAGERS
 */
@Composable
fun ManagersPlayers(
    managers: Pair<PlayerModel?, PlayerModel?>,
    changePlayerState: (PlayerState) -> Unit,
    changeSelectedPosition: (LineUpPosition) -> Unit,
    changeManagerSelected: (ManagerPosition) -> Unit,
    showPlayers: () -> Unit,
    removeManager: () -> Unit
) {
    Column {
        ManagersTitle()
        Managers(
            managers = managers,
            changePlayerState = { changePlayerState(it) },
            changeManagerSelected = { changeManagerSelected(it) },
            changeSelectedPosition = { changeSelectedPosition(it) },
            showPlayers = { showPlayers() },
            removeManager = { removeManager() }
        )
    }
}

@Composable
private fun ManagersTitle() {
    GBText(
        modifier = Modifier.padding(top = 12.dp),
        text = stringResource(R.string.managers),
        style = gBTypography().bodyLarge
    )
}

@Composable
private fun Managers(
    managers: Pair<PlayerModel?, PlayerModel?>,
    changePlayerState: (PlayerState) -> Unit,
    changeSelectedPosition: (LineUpPosition) -> Unit,
    changeManagerSelected: (ManagerPosition) -> Unit,
    showPlayers: () -> Unit,
    removeManager: () -> Unit
) {
    Row(
        modifier = Modifier.height(BENCH_PLAYER_SIZE),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(6.dp)
    ) {
        if (managers.first == null) {
            GBAddButton {
                showPlayers()
                changeSelectedPosition(Manager)
                changePlayerState(PlayerState.Manager)
                changeManagerSelected(First)
            }
        } else {
            ManagerCard(
                manager = managers.first!!,
                removeManager = { removeManager() }
            )
        }
        if (managers.second == null) {
            GBAddButton {
                showPlayers()
                changePlayerState(PlayerState.Manager)
                changeSelectedPosition(Manager)
                changeManagerSelected(Second)
            }
        } else {
            ManagerCard(
                manager = managers.second!!,
                removeManager = { removeManager() }
            )
        }
    }
}

@Composable
private fun ManagerCard(
    manager: PlayerModel,
    removeManager: () -> Unit
) {
    GBPlayerCard(
        modifier = Modifier.size(36.dp).clickable { removeManager() },
        player = manager.toGBPlayer(),
        showDorsal = false,
        showDeletion = false,
        onPlayerRemoved = { removeManager() }
    )
}

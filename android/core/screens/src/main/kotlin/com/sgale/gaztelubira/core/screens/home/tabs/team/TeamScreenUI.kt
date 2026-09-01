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

package com.sgale.gaztelubira.core.screens.home.tabs.team

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.components.GBPlayerCard
import com.sgale.gaztelubira.core.designsystem.components.GBTopAppBar
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.Position.Manager
import com.sgale.gaztelubira.core.domain.model.team.TeamModel

@Composable
fun TeamScreenUI(
    appTeam: TeamModel?,
    players: List<PlayerModel>,
    navigateToPlayerDetail: (String, Boolean) -> Unit,
    isAdmin: Boolean? = false,
    navigateToInsertPlayer: () -> Unit = {}
) {
    Column(Modifier.fillMaxSize()) {
        GBTopAppBar(
            appTeam = appTeam,
            showAdminButton = isAdmin,
            topBarTitle = null,
            onButtonClicked = { navigateToInsertPlayer() },
            modifier = Modifier
        )
        TeamPlayerList(Modifier.fillMaxSize(), players) { playerId ->
            navigateToPlayerDetail(
                playerId,
                players.find { it.id == playerId }?.position == Manager
            )
        }

        if (players.isEmpty()) {
            EmptyPlayersComponent(isAdmin)
        }
    }
}

@Composable
fun TeamPlayerList(
    modifier: Modifier,
    players: List<PlayerModel>,
    onPlayerClicked: (String) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(8.dp),
        contentPadding = PaddingValues(top = 0.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
    ) {
        items(players.filter { it.position != Manager }) { player ->
            GBPlayerCard(
                modifier = Modifier.size(100.dp),
                player = player,
                onPlayerClicked = { onPlayerClicked(player.id) }
            )
        }
        item(span = { GridItemSpan(3) }) {
            HorizontalDivider(Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
        }
        items(players.filter { it.position == Manager }.sortedBy { it.name }) { player ->
            GBPlayerCard(
                modifier = Modifier.size(100.dp),
                player = player,
                onPlayerClicked = { onPlayerClicked(player.id) },
                showDorsal = false
            )
        }
    }
}

@Composable
fun EmptyPlayersComponent(
    isAdmin: Boolean?
) {
    when (isAdmin) {
        true -> EmptyPlayersComponentAdmin()
        else -> EmptyPlayersComponentNonAdmin()
    }
}

@Composable
fun EmptyPlayersComponentAdmin() {
    // TODO
}

@Composable
fun EmptyPlayersComponentNonAdmin() {
    // TODO
}


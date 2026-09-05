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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.team

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
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBPlayerCard
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTopAppBar
import com.sgale.gaztelubira.multiplatform.designsystem.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromTeamTab
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromTeamTab.InsertPlayer
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromTeamTab.PlayerDetail

@Composable
internal fun TeamViewUI(
    players: List<GBPlayer>,
    managers: List<GBPlayer>,
    isAdmin: Boolean? = false,
    navigateTo: (FromTeamTab) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GBTopAppBar(
            showAdminButton = isAdmin == true,
            onButtonClicked = { navigateTo(InsertPlayer) },
        )

        TeamPlayerList(
            players = players,
            managers = managers,
            onPlayerClicked = { playerId -> navigateTo(PlayerDetail(playerId)) }
        )

        if (players.isEmpty()) {
            EmptyPlayersComponent(isAdmin)
        }
    }
}

@Composable
fun TeamPlayerList(
    players: List<GBPlayer>,
    managers: List<GBPlayer>,
    onPlayerClicked: (String) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(3),
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(8.dp),
        contentPadding = PaddingValues(top = 0.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
    ) {
        items(players) { player ->
            GBPlayerCard(
                modifier = Modifier.size(100.dp),
                player = player,
                onPlayerClicked = { onPlayerClicked(player.id) }
            )
        }
        item(
            span = { GridItemSpan(3) }
        ) {
            HorizontalDivider(Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
        }
        items(managers) { manager ->
            GBPlayerCard(
                modifier = Modifier.size(100.dp),
                player = manager,
                onPlayerClicked = { onPlayerClicked(manager.id) },
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

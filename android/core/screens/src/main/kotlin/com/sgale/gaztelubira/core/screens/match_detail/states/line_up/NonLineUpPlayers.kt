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

package com.sgale.gaztelubira.core.screens.match_detail.states.line_up

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.components.GBIcon
import com.sgale.gaztelubira.core.designsystem.components.GBPlayerCard
import com.sgale.gaztelubira.core.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import androidx.compose.ui.res.stringResource
import com.sgale.gaztelubira.core.screens.R

private val PLAYER_SIZE = 32.dp

/**
 * BENCH
 */
internal fun LazyGridScope.benchPlayers(
    showBench: Boolean,
    benchPlayers: List<PlayerModel>
) {
    item( span = { GridItemSpan(maxLineSpan) }) {
        AnimatedVisibility(
            visible = showBench,
            enter = fadeIn() + scaleIn(initialScale = 0.9f)
        ) {
            BenchTitle()
        }
    }

    items(
        items = benchPlayers,
        key = { it.id }
    ) { player ->
        AnimatedVisibility(
            visible = showBench,
            enter = fadeIn()
        ) {
            SubstituteCard(
                modifier = Modifier.size(PLAYER_SIZE),
                player = player
            )
        }

    }
}

@Composable
private fun BenchTitle() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 6.dp),
        verticalAlignment = Bottom,
        horizontalArrangement = spacedBy(16.dp)
    ) {
        GBIcon(
            modifier = Modifier.size(32.dp),
            icon = R.drawable.ic_bench
        )
        GBText(
            text = stringResource(R.string.bench),
            style = gBTypography().bodyLarge
        )
    }
}

@Composable
private fun SubstituteCard(
    modifier: Modifier,
    player: PlayerModel
) {
    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(8.dp)
    ) {
        GBPlayerCard(
            modifier = modifier.size(24.dp),
            player = player,
            showDorsal = false,
            showDeletion = false
        )

        GBText(
            modifier = Modifier.weight(1f),
            text = player.name,
            style = gBTypography().bodySmall
        )
    }
}


/**
 * MANAGERS
 */
@Composable
internal fun Managers(
    managers: Pair<PlayerModel?, PlayerModel?>
) {
    managers.first?.let { manager ->
        ManagerCard(
            modifier = Modifier.size(PLAYER_SIZE),
            player = manager
        )
    }

    managers.second?.let { manager ->
        ManagerCard(
            modifier = Modifier.size(PLAYER_SIZE),
            player = manager
        )
    }
}


@Composable
private fun ManagerCard(
    modifier: Modifier,
    player: PlayerModel
) {
    Column(
        horizontalAlignment = CenterHorizontally
    ) {
        GBPlayerCard(
            modifier = modifier,
            player = player,
            showDorsal = false,
            showDeletion = false
        )
    }
}

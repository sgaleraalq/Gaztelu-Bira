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

package com.sgale.gaztelubira.multiplatform.ui.detail.match.ui

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
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBPlayerCard
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.bench
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_bench
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val PLAYER_SIZE = 32.dp

/**
 * BENCH
 */
internal fun LazyGridScope.benchPlayers(
    showBench: Boolean,
    benchPlayers: List<GBPlayer>
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
            icon = painterResource(Res.drawable.ic_bench)
        )
        GBText(
            text = stringResource(Res.string.bench),
            style = gBTypography().bodyLarge
        )
    }
}

@Composable
private fun SubstituteCard(
    modifier: Modifier,
    player: GBPlayer
) {
    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(8.dp)
    ) {
        GBPlayerCard(
            modifier = modifier.size(24.dp),
            player = player,
            placeholder = AppImages.facePlayer,
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
    managers: Pair<GBPlayer?, GBPlayer?>
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
    player: GBPlayer
) {
    Column(
        horizontalAlignment = CenterHorizontally
    ) {
        GBPlayerCard(
            modifier = modifier,
            player = player,
            placeholder = AppImages.manager,
            showDorsal = false,
            showDeletion = false
        )
    }
}

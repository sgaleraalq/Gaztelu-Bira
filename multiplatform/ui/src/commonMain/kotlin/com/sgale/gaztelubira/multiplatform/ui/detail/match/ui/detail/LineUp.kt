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

package com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBFootballField
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBPlayerCard
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.gray_box_in_black_bg
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBTeam
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Lineup
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.bench
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_bench
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.milliseconds

internal val benchBgColor = gray_box_in_black_bg
internal val benchHorizontalPadding = 12.dp

@Composable
fun MatchDetailStateLineUp(
    modifier: Modifier,
    team: GBTeam?,
    state: MatchDetailState?
) {
    val lineUp = (state as? Lineup)?.lineUp ?: return

    val scope = rememberCoroutineScope()
    var animationPlayed by rememberSaveable { mutableStateOf(false) }
    var showBench by rememberSaveable { mutableStateOf(false) }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        ),
        verticalArrangement = spacedBy(8.dp)
    )  {
        item( span= { GridItemSpan(maxLineSpan) }) {
            Column {
                StartingElevenHeader(
                    managers = Pair(
                        first = lineUp.managers.getOrNull(0),
                        second = lineUp.managers.getOrNull(1)
                    ),
                    team = team
                )
                Spacer(Modifier.height(8.dp))
                GBFootballField(
            fieldImage = AppImages.footballField,
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.95f),
                    formation = lineUp.matchFormation,
                    players = lineUp.players.mapValues { entry -> entry.value }, // TODO
                    showAnimation = !animationPlayed,
                    onAnimationFinished = {
                        animationPlayed = true
                        scope.launch {
                            delay(200.milliseconds)
                            showBench = true
                        }
                    }
                )
            }
        }
        benchPlayers(showBench, lineUp.benchPlayers)
    }
}

@Composable
fun StartingElevenHeader(
    managers: Pair<GBPlayer?, GBPlayer?>,
    team: GBTeam?
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(12.dp)
    ) {
        GBImage(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(50))
                .border(width = 1.dp, color = White, shape = RoundedCornerShape(50)),
            image = team?.logo,
            placeholder = AppImages.teamCrest
        )
        GBText(
            modifier = Modifier.weight(1f),
            text = team?.name ?: "",
            style = gBTypography().bodySmall
        )
        Managers(managers)
    }
}

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
private fun Managers(
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

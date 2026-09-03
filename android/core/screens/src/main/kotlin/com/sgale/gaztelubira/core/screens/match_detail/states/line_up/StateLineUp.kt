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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.screens.match_detail.MatchDetailState
import com.sgale.gaztelubira.core.screens.match_detail.MatchDetailState.Lineup
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBFootballField
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.components.SaverStatus.Team
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.gray_box_in_black_bg
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal val benchBgColor = gray_box_in_black_bg
internal val benchHorizontalPadding = 12.dp

@Composable
fun MatchDetailStateLineUp(
    modifier: Modifier,
    team: TeamModel?,
    state: MatchDetailState?
) {
    val lineUp = (state as Lineup).lineUp ?: return

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
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.95f),
                    formation = lineUp.matchFormation,
                    players = lineUp.players,
                    showAnimation = !animationPlayed,
                    onAnimationFinished = {
                        animationPlayed = true
                        scope.launch {
                            delay(200)
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
    managers: Pair<PlayerModel?, PlayerModel?>,
    team: TeamModel?
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
            saverStatus = Team
        )
        GBText(
            modifier = Modifier.weight(1f),
            text = team?.name ?: "",
            style = gBTypography().bodySmall
        )
        Managers(managers)
    }
}

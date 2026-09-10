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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.white_in_gray_box
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Stats
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_down
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_right
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun MatchDetailStateStats(
    modifier: Modifier,
    state: MatchDetailState?
) {
    val statsState = state as? Stats ?: return
    val matchStats = statsState.stats ?: return

    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
//        items(Stat.entries.toList()) { stat ->
//            val players = when (stat) {
//                Goals -> matchStats.goals
//                GoalsProvoked -> matchStats.goalsProvoked
//                Assists -> matchStats.assists
//                CleanSheets -> matchStats.cleanSheets
//                PenaltiesProvoked -> matchStats.penaltiesProvoked
//                Saves -> matchStats.saves
//                Fails -> matchStats.fails
//                YellowCards -> matchStats.yellowCards
//                RedCards -> matchStats.redCards
//                else -> emptyList()
//            }
//
//            if (players.isNotEmpty()) {
//                StatDropdown(
////                    stat = stat,
//                    players = players
//                )
//            }
//        }
    }
}

@Composable
private fun StatDropdown(
//    stat: Stat,
    players: List<GBPlayer>,
) {
//    var expanded by rememberSaveable(stat) { mutableStateOf(false) }

//    Column(Modifier.fillMaxWidth()) {
//        StatHeader(
////            stat = stat,
//            expanded = expanded,
//            onClick = { expanded = !expanded }
//        )
//        HorizontalDivider()
//
//        if (expanded) {
//            players.forEach { player ->
//                RowPlayer(player = player)
//            }
//            Spacer(Modifier.height(8.dp))
//        }
//    }
}

@Composable
private fun StatHeader(
//    stat: Stat,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(benchBgColor)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(16.dp)
    ) {
//        GBIcon(
//            icon = painterResource(stat.icon),
//        )
//        GBText(
//            modifier = Modifier.weight(1f),
//            text = stringResource(stat.statName),
//            style = gBTypography().bodyLarge.copy(fontWeight = Bold),
//            textColor = white_in_gray_box
//        )
        GBIcon(
            modifier = Modifier.size(12.dp),
            icon = painterResource(if (expanded) Res.drawable.ic_arrow_down else Res.drawable.ic_arrow_right),
            tint = White
        )
    }
}

@Composable
private fun RowPlayer(
    modifier: Modifier = Modifier,
    player: GBPlayer
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = benchHorizontalPadding)
            .padding(vertical = 8.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(12.dp)
    ) {
        GBImage(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(50)),
            image = player.image,
            placeholder = AppImages.facePlayer
        )
        GBText(
            text = player.name,
            textColor = white_in_gray_box,
            style = gBTypography().bodyMedium
        )
    }
}

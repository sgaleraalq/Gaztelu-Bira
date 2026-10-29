/*
 * Designed and developed by 2026 sgale (Sergio Galera)
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

package com.sgale.gaztelubira.multiplatform.ui.insert.match.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextOverflow.Companion.Visible
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBPlayerCard
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_text_color
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.insert.match.INSERTABLE_STATS
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchActions
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog.Stat
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val STAT_BOX_SIZE = 75.dp
private val PLAYER_STAT_BOX_SIZE = 20.dp
private const val FACES_IN_BOX = 9

@Composable
internal fun InsertMatchStats(
    modifier: Modifier,
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = STAT_BOX_SIZE),
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(8.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(INSERTABLE_STATS) { stat ->
            StatItem(
                stat = stat,
                players = state.stat(stat),
                onClick = { actions.updateDialog(Stat(stat)) }
            )
        }
    }
}

@Composable
private fun StatItem(
    stat: GBStat,
    players: List<GBPlayer>,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(8.dp)
    ) {
        Box(Modifier.size(STAT_BOX_SIZE)) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = elevated_button_text_color),
                onClick = onClick
            ) {
                if (players.isEmpty()) {
                    GBIcon(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        icon = painterResource(stat.icon),
                        size = 50.dp
                    )
                } else {
                    Faces(players)
                }
            }
            if (players.isNotEmpty()) {
                GBText(
                    modifier = Modifier
                        .align(TopEnd)
                        .clip(CircleShape)
                        .background(White)
                        .padding(4.dp),
                    text = players.size.toString(),
                    textColor = Black,
                    style = GBTheme.typography.bodySmall.copy(fontSize = 10.sp)
                )
            }
        }
        GBText(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(stat.label),
            style = GBTheme.typography.bodySmall.copy(fontSize = 10.sp),
            overflow = Visible,
            alignment = Center
        )
    }
}

@Composable
private fun Faces(players: List<GBPlayer>) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.FixedSize(PLAYER_STAT_BOX_SIZE),
        contentPadding = PaddingValues(2.dp),
        horizontalArrangement = spacedBy(4.dp),
        verticalArrangement = spacedBy(4.dp),
        userScrollEnabled = false
    ) {
        items(players.distinctBy { it.id }.take(FACES_IN_BOX)) { player ->
            GBPlayerCard(
                modifier = Modifier.size(PLAYER_STAT_BOX_SIZE),
                player = player,
                placeholder = AppImages.facePlayer,
                isClickable = false,
                showDorsal = false
            )
        }
    }
}

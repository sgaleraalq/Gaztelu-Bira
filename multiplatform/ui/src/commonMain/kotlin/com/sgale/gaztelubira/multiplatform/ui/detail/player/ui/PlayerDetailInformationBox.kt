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

package com.sgale.gaztelubira.multiplatform.ui.detail.player.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.primaryBlue
import com.sgale.gaztelubira.multiplatform.designsystem.style.primaryRed
import com.sgale.gaztelubira.multiplatform.ui.detail.player.PlayerDetailActions
import com.sgale.gaztelubira.multiplatform.ui.detail.player.PlayerDetailUiState
import com.sgale.gaztelubira.multiplatform.ui.detail.player.PlayerDetailUiState.PlayerWinRate
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.draws
import com.sgale.gaztelubira.multiplatform.ui.resources.img_football_ball
import com.sgale.gaztelubira.multiplatform.ui.resources.img_gaztelu_bira
import com.sgale.gaztelubira.multiplatform.ui.resources.loses
import com.sgale.gaztelubira.multiplatform.ui.resources.view_stats
import com.sgale.gaztelubira.multiplatform.ui.resources.wins
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val LOGO_SIZE = 50

@Composable
internal fun PlayerDetailInformationBox(
    modifier: Modifier,
    state: PlayerDetailUiState,
    actions: PlayerDetailActions
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(1.25f / 3f)
            .background(
                color = White,
                shape = RoundedCornerShape(36.dp, 36.dp, 0.dp, 0.dp)
            )
    ) {
        Image(
            modifier = Modifier.fillMaxSize().alpha(0.025f),
            painter = painterResource(Res.drawable.img_football_ball),
            contentDescription = null,
            contentScale = Crop
        )

        GBImage(
            modifier = Modifier.align(TopCenter)
                .size(LOGO_SIZE.dp)
                .offset(y = (-(LOGO_SIZE / 2)).dp),
            painter = painterResource(Res.drawable.img_gaztelu_bira)
        )

        PlayerInformation(
            state = state,
            onViewStats = { actions.showToast() }
        )
    }
}

@Composable
private fun PlayerInformation(
    state: PlayerDetailUiState,
    onViewStats: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = (LOGO_SIZE / 2 + 16).dp, bottom = 16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        PlayerName(state.player.name)
        Spacer(modifier = Modifier.height(24.dp))
        PlayerBasicStats(state.winRate)
        Spacer(modifier = Modifier.height(12.dp))
        ViewStatsButton { onViewStats() }
    }
}

@Composable
private fun PlayerName(playerName: String?) {
    GBText(
        modifier = Modifier.fillMaxWidth(),
        text = playerName?.uppercase() ?: "",
        alignment = Center,
        textColor = primaryRed,
        style = gBTypography().titleLarge.copy(
            fontWeight = Bold
        )
    )
}

@Composable
private fun PlayerBasicStats(
    winRate: PlayerWinRate
) {
    var itemHeight by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        PlayerStatItem(
            modifier = Modifier
                .weight(1f)
                .onGloballyPositioned { coordinates -> itemHeight = coordinates.size.height },
            statValue = winRate.wins.toString(),
            playerStat = stringResource(Res.string.wins)
        )
        PersonalizedSpacer(itemHeight)
        PlayerStatItem(
            modifier = Modifier.weight(1f),
            statValue = winRate.draws.toString(),
            playerStat = stringResource(Res.string.draws)
        )
        PersonalizedSpacer(itemHeight)
        PlayerStatItem(
            modifier = Modifier.weight(1f),
            statValue = winRate.loses.toString(),
            playerStat = stringResource(Res.string.loses)
        )
    }
}

@Composable
private fun PlayerStatItem(
    modifier: Modifier,
    statValue: String,
    playerStat: String
) {
    Column(
        modifier = modifier.padding(12.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalDivider(thickness = 1.dp, color = primaryRed)
        Spacer(Modifier.height(12.dp))
        GBText(
            text = statValue,
            alignment = Center,
            style = gBTypography().headlineLarge.copy(fontWeight = Bold),
            textColor = primaryBlue
        )
        Spacer(Modifier.height(4.dp))
        GBText(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            text = playerStat,
            style = gBTypography().bodySmall,
            alignment = Center,
            textColor = Black
        )
        Spacer(Modifier.height(12.dp))
        HorizontalDivider(thickness = 1.dp, color = primaryRed)
    }
}

@Composable
private fun ViewStatsButton(
    viewStats: () -> Unit
) {
    GBElevatedButton(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        text = stringResource(Res.string.view_stats),
        backgroundColor = primaryRed,
        textColor = White,
        roundness = 32,
        onClick = { viewStats() }
    )
}

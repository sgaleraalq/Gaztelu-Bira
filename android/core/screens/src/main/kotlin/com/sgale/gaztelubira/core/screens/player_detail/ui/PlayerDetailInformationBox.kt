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

package com.sgale.gaztelubira.core.screens.player_detail.ui

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.components.GBAnimatedMessage
import com.sgale.gaztelubira.core.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.core.designsystem.components.GBText
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.designsystem.style.primaryBlue
import com.sgale.gaztelubira.core.designsystem.style.primaryRed
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.player_detail.LOGO_SIZE
import com.sgale.gaztelubira.core.screens.player_detail.PlayerDetailState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
internal fun PlayerDetailInformationBox(
    appTeam: TeamModel?,
    modifier: Modifier,
    player: PlayerModel?,
    playerStats: PlayerStatsModel?,
    state: PlayerDetailState?
) {
    var viewStats by remember { mutableStateOf(false) }
    var showGBMessage by remember { mutableStateOf(false) }

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
            painter = painterResource(R.drawable.img_football_ball),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.025f),
            contentScale = Crop
        )
        TeamImage(
            modifier = Modifier.align(TopCenter),
            logo = appTeam?.logo,
            logoSize = LOGO_SIZE
        )
        PlayerInformation(
            player = player,
            playerStats = playerStats,
            wins = state?.wins,
            draws = state?.draws,
            loses = state?.loses,
            onViewStats = { /* TODO */ showGBMessage = true }
        )
    }

    if (showGBMessage) {
        GBAnimatedMessage(
            msg = stringResource(R.string.not_yet_available),
            dismissMsg = { showGBMessage = false }
        )
    }
}

@Composable
private fun PlayerInformation(
    player: PlayerModel?,
    playerStats: PlayerStatsModel?,
    wins: Int?,
    draws: Int?,
    loses: Int?,
    onViewStats: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = (LOGO_SIZE / 2 + 16).dp, bottom = 16.dp)
    ) {
        Spacer(Modifier.weight(1f))
        PlayerName(player?.name)
        Spacer(Modifier.height(24.dp))
        PlayerBasicStats(wins, draws, loses)
        Spacer(Modifier.height(12.dp))
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
    wins: Int?,
    draws: Int?,
    loses: Int?
) {
    var itemHeight by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        PlayerStatItem(
            modifier = Modifier
                .weight(1f)
                .onGloballyPositioned { coordinates ->
                    itemHeight = coordinates.size.height
                },
            statValue = wins.toString(),
            playerStat = stringResource(R.string.wins)
        )

        PersonalizedSpacer(itemHeight)

        PlayerStatItem(
            modifier = Modifier.weight(1f),
            statValue = draws.toString(),
            playerStat = stringResource(R.string.draws)
        )

        PersonalizedSpacer(itemHeight)

        PlayerStatItem(
            modifier = Modifier.weight(1f),
            statValue = loses.toString(),
            playerStat = stringResource(R.string.loses)
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
            style = gBTypography().headlineLarge.copy(
                fontWeight = Bold
            ),
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
private fun PersonalizedSpacer(itemHeight: Int) {
    Column(
        modifier = Modifier
            .width(8.dp)
            .height(with(LocalDensity.current) { itemHeight.toDp() })
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        repeat(8) {
            DiagonalLine()
        }
    }
}

@Composable
private fun DiagonalLine() {
    Canvas(Modifier.fillMaxWidth().height(5.dp)) {
        val start = Offset(0f, 0f)
        val end = Offset(size.width * 1f, size.height * 1f)

        drawLine(
            color = primaryBlue,
            start = start,
            end = end,
            strokeWidth = 4f
        )
    }
}

@Composable
private fun ViewStatsButton(
    viewStats: () -> Unit
) {
    GBElevatedButton(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        text = stringResource(R.string.view_stats),
        backgroundColor = primaryRed,
        textColor = White,
        roundness = 32,
        onClick = { viewStats() }
    )
}

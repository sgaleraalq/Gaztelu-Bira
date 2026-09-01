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

package com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.components.GBImage
import com.sgale.gaztelubira.core.designsystem.components.GBText
import com.sgale.gaztelubira.core.designsystem.components.SaverStatus.Team
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.domain.model.match.MatchResult
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira.GBInformation
import com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira.Streak
import androidx.compose.ui.res.stringResource

@Composable
fun GBHomeInformationBox(
    gbInformation: GBInformation?
) {
    Column {
        GBHomeHeader(gbInformation)
        GBHomeMatchStats(
            points = gbInformation?.points,
            games = gbInformation?.games,
            wins = gbInformation?.wins,
            draws = gbInformation?.draws,
            loses = gbInformation?.loses,
            favorGoals = gbInformation?.goalsFor,
            disfavorGoals = gbInformation?.goalsAgainst
        )
    }
}

@Composable
fun GBHomeHeader(
    gbInformation: GBInformation?
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        GBHomeTeamLogo(gbInformation?.team?.logo)
        Spacer(Modifier.width(12.dp))
        GBHomeData(
            modifier = Modifier.weight(1f),
            gbInformation = gbInformation
        )
    }
}

@Composable
fun GBHomeTeamLogo(logo: String?) {
    GBImage(
        modifier = Modifier
            .size(75.dp)
            .clip(RoundedCornerShape(50))
            .border(
                width = 1.dp,
                color = White,
                shape = RoundedCornerShape(50)
            ),
        image = logo,
        saverStatus = Team
    )
}

@Composable
fun GBHomeData(
    modifier: Modifier,
    gbInformation: GBInformation?
) {
    Column(modifier.height(75.dp)) {
        GBHomeTeamName(Modifier.weight(1f), gbInformation?.team?.name)
        GBHomeStreak(Modifier.weight(1f), gbInformation?.streak)
    }
}

@Composable
fun GBHomeTeamName(modifier: Modifier, name: String?) {
    GBText(
        modifier = modifier.fillMaxWidth().padding(end = 60.dp),
        text = name,
        style = gBTypography().titleLarge.copy(
            fontWeight = Bold
        )
    )
}

@Composable
fun GBHomeStreak(modifier: Modifier, streak: Streak?) {
    Column(modifier) {
        GBHomeStreakHeader(Modifier.weight(1f), streak?.currentStreak)
        GBHomeStreakImage(Modifier.weight(1f), streak?.lastGames)
    }
}

@Composable
fun GBHomeStreakHeader(modifier: Modifier, currentStreak: Int?) {
    Row(modifier.fillMaxWidth()){
        GBText(
            text = stringResource(R.string.last_streak) + ":",
            style = gBTypography().bodySmall
        )
        Spacer(Modifier.width(12.dp))
        GBHomeFlameStreak(currentStreak)
    }
}

@Composable
fun GBHomeFlameStreak(currentStreak: Int?) {
    GBText(
        text = currentStreak.toString(),
        style = gBTypography().bodySmall
    )
}

@Composable
fun GBHomeStreakImage(
    modifier: Modifier,
    lastGames: List<MatchResult>?
) {
    LazyHorizontalGrid(
        modifier = modifier,
        rows = GridCells.Fixed(1),
        horizontalArrangement = spacedBy(8.dp)
    ) {
        items(lastGames?.size ?: 0) { idx ->
            val game = lastGames?.get(idx)
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(game?.solidColor ?: Transparent)
            )
        }
    }
}

@Composable
fun GBHomeMatchStats(
    points: Int?,
    games: Int?,
    wins: Int?,
    draws: Int?,
    loses: Int?,
    favorGoals: Int?,
    disfavorGoals: Int?
) {
    val stats = listOf(
        stringResource(R.string.short_pts) to points,
        stringResource(R.string.short_gms) to games,
        stringResource(R.string.short_win) to wins,
        stringResource(R.string.short_draw) to draws,
        stringResource(R.string.short_lose) to loses,
        stringResource(R.string.short_goalsFor) to favorGoals,
        stringResource(R.string.short_goalsAgainst) to disfavorGoals
    )

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        stats.forEach { (short, value) ->
            GBHomeStat(
                modifier = Modifier.weight(1f),
                name = short,
                value = value?.toString() ?: "-"
            )
        }
    }
}

@Composable
fun GBHomeStat(
    modifier: Modifier = Modifier,
    name: String,
    value: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GBText(
            text = name,
            style = gBTypography().bodySmall
        )
        GBText(
            text = value,
            style = gBTypography().bodySmall
        )
    }
}

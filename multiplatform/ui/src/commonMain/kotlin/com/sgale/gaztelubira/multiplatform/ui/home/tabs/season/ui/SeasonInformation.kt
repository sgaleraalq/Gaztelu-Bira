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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.season.ui

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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.model.GBMatchResult
import com.sgale.gaztelubira.multiplatform.model.GBSeason
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.common_app_name
import com.sgale.gaztelubira.multiplatform.ui.resources.home_season_last_streak
import com.sgale.gaztelubira.multiplatform.ui.resources.home_season_short_draw
import com.sgale.gaztelubira.multiplatform.ui.resources.home_season_short_gms
import com.sgale.gaztelubira.multiplatform.ui.resources.home_season_short_goalsAgainst
import com.sgale.gaztelubira.multiplatform.ui.resources.home_season_short_goalsFor
import com.sgale.gaztelubira.multiplatform.ui.resources.home_season_short_lose
import com.sgale.gaztelubira.multiplatform.ui.resources.home_season_short_pts
import com.sgale.gaztelubira.multiplatform.ui.resources.home_season_short_win
import com.sgale.gaztelubira.multiplatform.ui.resources.img_gaztelu_bira
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val NO_VALUE = "-"

@Composable
internal fun SeasonInformationBox(
    summary: GBSeason?
) {
    Column {
        SeasonHeader(summary)
        SeasonMatchStats(summary)
    }
}

@Composable
private fun SeasonHeader(
    summary: GBSeason?
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        SeasonLogo()
        Spacer(modifier = Modifier.width(12.dp))
        SeasonData(
            modifier = Modifier.weight(1f),
            summary = summary
        )
    }
}

@Composable
private fun SeasonLogo() {
    GBImage(
        modifier = Modifier
            .size(75.dp)
            .clip(RoundedCornerShape(50))
            .border(width = 1.dp, color = White, shape = RoundedCornerShape(50)),
        painter = painterResource(Res.drawable.img_gaztelu_bira),
    )
}

@Composable
private fun SeasonData(
    modifier: Modifier,
    summary: GBSeason?
) {
    Column(
        modifier = modifier.height(75.dp)
    ) {
        GBText(
            modifier = Modifier.weight(1f).fillMaxWidth().padding(end = 60.dp),
            text = stringResource(Res.string.common_app_name),
            style = gBTypography().titleLarge.copy(fontWeight = Bold)
        )
        SeasonStreak(
            modifier = Modifier.weight(1f),
            summary = summary
        )
    }
}

@Composable
private fun SeasonStreak(
    modifier: Modifier,
    summary: GBSeason?
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            GBText(
                text = stringResource(Res.string.home_season_last_streak) + ":",
                style = gBTypography().bodySmall
            )
            Spacer(modifier = Modifier.width(8.dp))
            GBText(
                text = summary?.currentStreak?.toString() ?: NO_VALUE,
                style = gBTypography().bodySmall
            )
        }
        SeasonStreakDots(
            modifier = Modifier.weight(1f),
            lastGames = summary?.lastGames.orEmpty()
        )
    }
}

@Composable
private fun SeasonStreakDots(
    modifier: Modifier,
    lastGames: List<GBMatchResult>
) {
    LazyHorizontalGrid(
        modifier = modifier,
        rows = GridCells.Fixed(1),
        horizontalArrangement = spacedBy(8.dp)
    ) {
        items(lastGames.size) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(lastGames[index].solidColor)
            )
        }
    }
}

@Composable
private fun SeasonMatchStats(summary: GBSeason?) {
    val stats = listOf(
        stringResource(Res.string.home_season_short_pts) to summary?.points,
        stringResource(Res.string.home_season_short_gms) to summary?.games,
        stringResource(Res.string.home_season_short_win) to summary?.wins,
        stringResource(Res.string.home_season_short_draw) to summary?.draws,
        stringResource(Res.string.home_season_short_lose) to summary?.loses,
        stringResource(Res.string.home_season_short_goalsFor) to summary?.goalsFor,
        stringResource(Res.string.home_season_short_goalsAgainst) to summary?.goalsAgainst
    )

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        stats.forEach { (label, value) ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally
            ) {
                GBText(
                    text = label,
                    style = gBTypography().bodySmall
                )
                GBText(
                    text = value?.toString() ?: NO_VALUE,
                    style = gBTypography().bodySmall
                )
            }
        }
    }
}

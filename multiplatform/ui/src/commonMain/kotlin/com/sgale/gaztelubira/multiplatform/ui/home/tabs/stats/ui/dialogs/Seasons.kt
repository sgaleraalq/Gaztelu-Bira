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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.StatsSeason
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_date
import com.sgale.gaztelubira.multiplatform.ui.resources.seasons_dialog_subtitle
import com.sgale.gaztelubira.multiplatform.ui.resources.seasons_dialog_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val seasonsDialogColor = Color(0xFF131722)
private val seasonsBorderColor = Color(0xFF1D2738)
private val seasonsTextColor = Color(0xFF94A4B8)

@Composable
internal fun SeasonsDialog(
    show: Boolean,
    seasons: List<StatsSeason>,
    onChangeSeason: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = BottomCenter
    ) {
        AnimatedVisibility(
            visible = show,
            enter = slideInVertically { it },
            exit = slideOutVertically { it }
        ) {
            SeasonsPicker(
                seasons = seasons,
                onSeasonClicked = onChangeSeason
            )
        }
    }
}

@Composable
private fun SeasonsPicker(
    seasons: List<StatsSeason>,
    onSeasonClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            )
            .border(
                width = 2.dp,
                color = seasonsBorderColor
            )
            .background(seasonsDialogColor)
    ) {
        SeasonsDialogHeader()
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = seasonsTextColor
        )
        seasons.forEach { season ->
            SeasonsDialogOption(season, onSeasonClicked)
        }
    }
}

@Composable
private fun SeasonsDialogHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp)
    ) {
        SeasonsDialogTitle(
            modifier = Modifier.weight(1f)
        )
        SeasonsDialogCloseButton()
    }
}

@Composable
private fun SeasonsDialogTitle(
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(12.dp)
    ) {
        SeasonsDialogTitle()
        SeasonsDialogSubtitle()
    }
}

@Composable
private fun SeasonsDialogTitle() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(8.dp)
    ) {
        GBIcon(
            modifier = Modifier.size(24.dp),
            icon = painterResource(Res.drawable.ic_date),
            tint = Red
        )
        GBText(
            text = stringResource(Res.string.seasons_dialog_title),
            style = gBTypography().titleLarge,
            textColor = White
        )
    }
}

@Composable
private fun SeasonsDialogSubtitle() {
    GBText(
        text = stringResource(Res.string.seasons_dialog_subtitle),
        style = gBTypography().bodyMedium,
        textColor = seasonsTextColor
    )
}

@Composable
private fun SeasonsDialogCloseButton() {

}

@Composable
private fun SeasonsDialogOption(
    season: StatsSeason,
    onSeasonClicked: (String) -> Unit
) {
    GBText(
        modifier = Modifier
            .clickable { onSeasonClicked(season.id) }
            .fillMaxWidth()
            .padding(24.dp),
        text = season.name,
        textColor = seasonsTextColor
    )
}

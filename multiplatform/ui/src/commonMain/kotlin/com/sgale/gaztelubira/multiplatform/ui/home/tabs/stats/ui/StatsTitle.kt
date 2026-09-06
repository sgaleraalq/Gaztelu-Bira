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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBProgressDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg_not_selected
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.primaryRed
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_settings
import com.sgale.gaztelubira.multiplatform.ui.resources.leaderboard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun StatsTitle(
    onSettingsClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(12.dp)
    ) {
        GBText(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.leaderboard),
            style = gBTypography().headlineSmall
        )
        GBIcon(
            modifier = Modifier.size(24.dp).clickable { onSettingsClicked() },
            icon = painterResource(Res.drawable.ic_settings),
            tint = elevated_button_bg_not_selected
        )
    }
}

@Composable
internal fun SelectedStatTitle(
    stat: GBStat
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(16.dp)
    ) {
        GBIcon(
            modifier = Modifier.size(24.dp),
            icon = painterResource(stat.icon)
        )
        GBText(
            text = stringResource(stat.label),
            style = gBTypography().bodyLarge.copy(fontWeight = Normal)
        )
    }
}

@Composable
internal fun StatsLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
    ) {
        GBProgressDialog(
            show = true,
            color = primaryRed
        )
    }
}

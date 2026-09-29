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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.loaded

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.DEFAULT_BORDER_COLOR
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBContainer
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg_not_selected
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_down
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_settings
import com.sgale.gaztelubira.multiplatform.ui.resources.leaderboard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun StatsHeader(
    onSettingsClicked: () -> Unit,
    onShowSeasons: () -> Unit
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
        SeasonsDropdown(
            onShowSeasons = onShowSeasons
        )
        GBIcon(
            modifier = Modifier.size(24.dp).clickable { onSettingsClicked() },
            icon = painterResource(Res.drawable.ic_settings),
            tint = elevated_button_bg_not_selected
        )
    }
}

@Composable
private fun SeasonsDropdown(
    onShowSeasons: () -> Unit
) {
    GBContainer {
        Row(
            modifier = Modifier.widthIn(0.dp, 150.dp).width(200.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = spacedBy(8.dp)
        ) {
            GBText(".")
            GBText(
                modifier = Modifier.weight(1f),
                text = "26/27"
            )
            GBIcon(
                modifier = Modifier.size(24.dp).padding(8.dp),
                icon = painterResource(Res.drawable.ic_arrow_down),
                tint = DEFAULT_BORDER_COLOR
            )
        }
    }
}

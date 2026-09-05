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

package com.sgale.gaztelubira.core.screens.home.tabs.stats.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.screens.home.tabs.stats.dialogs.SettingsDialogState.ChangeStat
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg_not_selected
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import androidx.compose.ui.res.painterResource

@Composable
internal fun SettingsStats(
    state: SettingsDialogState,
    changeSelectedStat: (Stat) -> Unit,
    dismiss: () -> Unit
) {
    state as ChangeStat

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Stat.entries.forEach { stat ->
            StatIndicator(
                stat = stat,
                isSelected = state.selectedStat == stat,
                onClick = {
                    changeSelectedStat(it)
                    dismiss()
                }
            )
        }
    }
}

@Composable
private fun StatIndicator(
    stat: Stat,
    isSelected: Boolean,
    onClick: (Stat) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable { onClick(stat) }
            .background(if (isSelected) elevated_button_bg_not_selected else Transparent)
            .padding(12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(12.dp)
    ) {
        GBIcon(
            modifier = Modifier.size(24.dp),
            icon = painterResource(stat.icon)
        )
        GBText(
            modifier = Modifier.weight(1f),
            text = stringResource(stat.statName),
            style = gBTypography().bodyMedium,
            textColor = if (isSelected) White else Black,
        )
    }
}

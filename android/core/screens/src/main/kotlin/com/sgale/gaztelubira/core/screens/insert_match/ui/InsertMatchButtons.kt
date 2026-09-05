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

package com.sgale.gaztelubira.core.screens.insert_match.ui

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchState
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchState.Formation
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchState.Information
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchState.Stats
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg_not_selected
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_text_color
import com.sgale.gaztelubira.multiplatform.designsystem.style.gb_dialog_background
import androidx.compose.ui.res.painterResource

@Composable
internal fun InsertMatchButtons(
    modifier: Modifier,
    changeState: (InsertMatchState) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(12.dp)
    ) {
        InsertMatchInformationButton { changeState(Information) }
        InsertMatchFormationButton { changeState(Formation) }
        InsertMatchStatsButton { changeState(Stats) }
    }
}

@Composable
internal fun InsertMatchInformationButton(
    changeState: () -> Unit
) {
    InsertMatchButton(
        icon = R.drawable.ic_information,
        text = stringResource(R.string.information),
        onClick = { changeState() }
    )
}
@Composable
internal fun InsertMatchFormationButton(
    changeState: () -> Unit
) {
    InsertMatchButton(
        icon = R.drawable.ic_formation,
        text = stringResource(R.string.formation),
        onClick = { changeState() }
    )
}
@Composable
internal fun InsertMatchStatsButton(
    changeState: () -> Unit
) {
    InsertMatchButton(
        icon = R.drawable.ic_stats,
        text = stringResource(R.string.stats),
        onClick = { changeState() }
    )
}

@Composable
internal fun InsertMatchFormationButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    GBElevatedButton(
        text = text,
        onClick = { onClick() },
        backgroundColor = if (isSelected) elevated_button_bg else elevated_button_bg_not_selected,
        textColor = if (isSelected) elevated_button_text_color else elevated_button_bg
    )
}

@Composable
internal fun InsertMatchButton(
    icon: Int,
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(12.dp),
        onClick = { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = elevated_button_text_color,
            contentColor = elevated_button_bg
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = spacedBy(24.dp)
        ) {
            GBIcon(
                icon = painterResource(icon),
                tint = Black
            )
            GBText(
                modifier = Modifier.weight(1f),
                text = text,
                textColor = gb_dialog_background
            )
            GBIcon(
                icon = painterResource(R.drawable.ic_arrow_right)
            )
        }
    }
}

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

package com.sgale.gaztelubira.core.screens.insert_player.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.domain.model.player.Position
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTextField
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.gray_box_in_black_bg

@Composable
internal fun MainInformation(
    playerName: String,
    dorsal: Int,
    position: Position?,
    onPlayerNameChanged: (String) -> Unit,
    showAvailableDorsals: () -> Unit,
    showAvailablePositions: () -> Unit
) {
    GBText(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.information),
        style = gBTypography().titleMedium,
        alignment = Start
    )
    GBTextField(
        modifier = Modifier.fillMaxWidth(),
        text = playerName,
        onTextChanged = { onPlayerNameChanged(it) },
        label = stringResource(R.string.player_name),
        firstCap = true
    )
    Spacer(Modifier.height(8.dp))
    DorsalAndPosition(dorsal, position, showAvailableDorsals, showAvailablePositions)
}

@Composable
internal fun DorsalAndPosition(
    dorsal: Int,
    position: Position?,
    showAvailableDorsals: () -> Unit,
    showAvailablePositions: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(8.dp)
    ) {
        InformationComponent(
            modifier = Modifier.weight(1f),
            informationText =
                if (dorsal == 0) {
                    stringResource(R.string.dorsal)
                } else {
                    "${stringResource(R.string.dorsal)}: $dorsal"
                }
        ) { showAvailableDorsals() }
        InformationComponent(
            modifier = Modifier.weight(1f),
            informationText = stringResource(position?.positionName ?: R.string.position)
        ) { showAvailablePositions() }
    }
}

@Composable
internal fun InformationComponent(
    modifier: Modifier,
    informationText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = gray_box_in_black_bg
        ),
        onClick = { onClick() }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Center
        ) {
            GBText(
                text = informationText
            )
        }
    }
}

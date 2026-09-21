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

package com.sgale.gaztelubira.multiplatform.ui.insert.player.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerUiState
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.select_position
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PositionDialog(
    state: InsertPlayerUiState,
    onPositionClicked: (String) -> Unit,
) {
    state.positions.forEach { position ->
        GBText(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPositionClicked(position) }
                .padding(16.dp),
            text = position,
            alignment = TextAlign.Center
        )
    }
}

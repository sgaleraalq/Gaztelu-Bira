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

package com.sgale.gaztelubira.core.screens.insert_player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.components.GBInsertButton
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField.Dorsal
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField.Position
import com.sgale.gaztelubira.core.screens.insert_player.UiState.Capture
import com.sgale.gaztelubira.core.screens.insert_player.UiState.Default
import com.sgale.gaztelubira.core.screens.insert_player.UiState.Dorsal
import com.sgale.gaztelubira.core.screens.insert_player.UiState.Loading
import com.sgale.gaztelubira.core.screens.insert_player.UiState.Position
import com.sgale.gaztelubira.core.screens.insert_player.ui.DorsalDialog
import com.sgale.gaztelubira.core.screens.insert_player.ui.InsertPlayerUiComponents
import com.sgale.gaztelubira.core.screens.insert_player.ui.MediaOrCameraDialog
import com.sgale.gaztelubira.core.screens.insert_player.ui.PositionDialog
import androidx.compose.ui.res.stringResource

@Composable
internal fun InsertPlayerScreenUI(
    modifier: Modifier,
    data: InsertPlayerData,
    uiState: UiState,
    getAvDorsals: () -> List<Int>,
    updateUi: (UiState) -> Unit,
    updateField: (InsertPlayerField, Any) -> Unit,
    onMediaClicked: () -> Unit,
    onCameraClicked: () -> Unit,
    removeImage: () -> Unit,
    onInsert: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        InsertPlayerUiComponents(
            data = data,
            getAvDorsals = { getAvDorsals() },
            updateField = { field, value -> updateField(field, value) },
            updateUi = { updateUi(it) },
            removeImage = { removeImage() }
        )
        Spacer(Modifier.weight(1f))
        GBInsertButton(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 16.dp),
            text = stringResource(R.string.insert_player),
            enabled = data.canInsert,
            loading = uiState == Loading,
            onInsert = { onInsert() }
        )
    }

    when (uiState) {
        Default -> Unit
        Loading -> Unit
        Capture -> {
            MediaOrCameraDialog(
                onDismiss = { updateUi(Default) },
                onMedia = { updateUi(Default); onMediaClicked() },
                onCamera = { onCameraClicked() }
            )
        }

        is Dorsal -> {
            DorsalDialog(
                dorsals = uiState.dorsals,
                onDorsalClicked = { dorsal ->
                    keyboardController?.hide()
                    updateField(Dorsal, dorsal)
                },
                dismiss = { updateUi(Default) }
            )
        }

        is Position -> {
            PositionDialog(
                onPositionClicked = { position ->
                    keyboardController?.hide()
                    updateField(Position, position)
                },
                dismiss = { updateUi(Default) }
            )
        }
    }
}

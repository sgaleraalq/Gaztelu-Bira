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

package com.sgale.gaztelubira.multiplatform.ui.insert.player.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBMediaOrCamera
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.white_in_gray_box
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerActions
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Generic
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Images
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.None
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Dorsal
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Position
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.select_dorsal
import com.sgale.gaztelubira.multiplatform.ui.resources.select_media_from
import com.sgale.gaztelubira.multiplatform.ui.resources.select_position
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun InsertPlayerDialogs(
    state: InsertPlayerUiState,
    actions: InsertPlayerActions
) {
    val dismiss = { actions.onInsertPlayerAction(None) }

    when (state.dialog) {
        None -> Unit

        is Images -> {
            GBMediaOrCamera(
                title = stringResource(Res.string.select_media_from),
                dismiss = dismiss,
                onMediaClicked = { actions.openGallery() },
                onCameraClicked = { actions.openCamera() }
            )
        }

        is Generic -> {
            GenericDialog(
                dialog = state.dialog,
                state = state,
                actions = actions,
                dismiss = dismiss
            )
        }
    }
}

@Composable
private fun GenericDialog(
    dialog: Generic,
    state: InsertPlayerUiState,
    actions: InsertPlayerActions,
    dismiss: () -> Unit
) {
    val title = when (dialog) {
        Generic.Dorsal -> stringResource(Res.string.select_dorsal)
        Generic.Position -> stringResource(Res.string.select_position)
    }

    InsertPlayerGenericDialog(
        dismiss = dismiss,
        title = title,
        dialogContent =  {
            when (dialog) {
                Generic.Dorsal -> DorsalDialog(
                    dorsals = state.availableDorsals,
                    onDorsalClicked = { dorsal -> actions.updateField(Dorsal(dorsal)) },
                    dismiss = dismiss
                )
                Generic.Position -> PositionDialog(
                    state = state,
                    onPositionClicked = { position -> actions.updateField(Position(position)) },
                )
            }
        }
    )
}

@Composable
private fun InsertPlayerGenericDialog(
    dismiss: () -> Unit,
    title: String,
    dialogContent: @Composable () -> Unit
) {
    GBDialog(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        dismiss = dismiss
    ) { modifier ->
        InsertPlayerDialogScaffold(modifier, title, dialogContent)
    }
}

@Composable
private fun InsertPlayerDialogScaffold(
    modifier: Modifier,
    title: String,
    dialogContent: @Composable () -> Unit
) {
    Column(
        modifier = modifier.size(400.dp)
    ) {
        InsertPlayerDialogTitle(title)
        dialogContent()
    }
}

@Composable
private fun InsertPlayerDialogTitle(
    text: String
) {
    GBText(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = white_in_gray_box,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            )
            .padding(12.dp),
        text = text,
        style = gBTypography().bodyLarge,
        alignment = Start,
        textColor = Black
    )
}

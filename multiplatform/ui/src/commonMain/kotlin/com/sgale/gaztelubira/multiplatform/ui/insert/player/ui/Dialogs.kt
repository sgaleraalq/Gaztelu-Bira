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

package com.sgale.gaztelubira.multiplatform.ui.insert.player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBMediaOrCamera
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.white_in_gray_box
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerActions
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Capture
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Dorsals
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.None
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Positions
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Dorsal
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Position
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.PlayerPosition
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
    val dismiss = { actions.showDialog(None) }

    when (state.dialog) {
        None -> Unit

        Capture -> GBMediaOrCamera(
            title = stringResource(Res.string.select_media_from),
            dismiss = dismiss,
            onMediaClicked = {
                dismiss()
                actions.pickImage()
            },
            onCameraClicked = {
                dismiss()
                actions.takePicture()
            }
        )

        Dorsals -> DorsalDialog(
            dorsals = state.availableDorsals,
            onDorsalClicked = { dorsal -> actions.updateField(Dorsal(dorsal)) },
            dismiss = dismiss
        )

        Positions -> PositionDialog(
            onPositionClicked = { position -> actions.updateField(Position(position)) },
            dismiss = dismiss
        )
    }
}

@Composable
private fun DorsalDialog(
    dorsals: List<Int>,
    onDorsalClicked: (Int) -> Unit,
    dismiss: () -> Unit
) {
    GBDialog(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        dismiss = dismiss
    ) { modifier ->
        Column(
            modifier = modifier.size(400.dp)
        ) {
            InsertPlayerDialogTitle(text = stringResource(Res.string.select_dorsal))
            LazyVerticalGrid(
                modifier = Modifier.weight(1f),
                columns = Fixed(5),
                horizontalArrangement = spacedBy(12.dp),
                verticalArrangement = spacedBy(12.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(dorsals) { dorsal ->
                    DorsalCard(
                        dorsal = dorsal,
                        onDorsalClicked = onDorsalClicked,
                        dismiss = dismiss
                    )
                }
            }
        }
    }
}

@Composable
private fun DorsalCard(
    dorsal: Int,
    onDorsalClicked: (Int) -> Unit,
    dismiss: () -> Unit
) {
    Box(
        modifier = Modifier.size(40.dp)
            .clip(CircleShape)
            .background(White)
            .clickable {
                onDorsalClicked(dorsal)
                dismiss()
            },
        contentAlignment = Center
    ) {
        GBText(
            text = dorsal.toString(),
            style = gBTypography().bodyMedium,
            textColor = Black,
            alignment = TextAlign.Center
        )
    }
}

@Composable
private fun PositionDialog(
    onPositionClicked: (PlayerPosition) -> Unit,
    dismiss: () -> Unit
) {
    GBDialog(
        modifier = Modifier.padding(32.dp),
        dismiss = dismiss
    ) { modifier ->
        Column(modifier = modifier) {
            InsertPlayerDialogTitle(text = stringResource(Res.string.select_position))
            PlayerPosition.selectable.forEach { position ->
                GBText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onPositionClicked(position)
                            dismiss()
                        }
                        .padding(16.dp),
                    text = stringResource(position.label),
                    alignment = TextAlign.Center
                )
            }
        }
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

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.domain.model.player.Position
import com.sgale.gaztelubira.core.domain.model.player.Position.Bench
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBMediaOrCamera
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.white_in_gray_box

@Composable
internal fun MediaOrCameraDialog(
    onDismiss: () -> Unit,
    onMedia: () -> Unit,
    onCamera: () -> Unit
) {
    GBMediaOrCamera(
        title = stringResource(R.string.select_media_from),
        dismiss = onDismiss,
        onMediaClicked = onMedia,
        onCameraClicked = onCamera
    )
}

@Composable
internal fun DorsalDialog(
    dorsals: List<Int>,
    onDorsalClicked: (Int) -> Unit,
    dismiss: () -> Unit
) {
    GBDialog(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        dismiss = { dismiss() }
    ) { modifier ->
        Column(
            modifier = modifier.size(400.dp)
        ) {
            InsertPlayerDialogTitle(
                text = stringResource(R.string.select_dorsal)
            )
            LazyVerticalGrid(
                modifier = Modifier.weight(1f),
                columns = GridCells.Fixed(5),
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
internal fun DorsalCard(
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
        contentAlignment = Alignment.Center
    ) {
        GBText(
            text = dorsal.toString(),
            style = gBTypography().bodyMedium,
            textColor = Black,
            alignment = Center
        )
    }
}

@Composable
internal fun PositionDialog(
    onPositionClicked: (Position) -> Unit,
    dismiss: () -> Unit
) {
    GBDialog(
        modifier = Modifier.padding(32.dp),
        dismiss = { dismiss() }
    ) { modifier ->
        Column(
            modifier = modifier
        ) {
            InsertPlayerDialogTitle(
                text = stringResource(R.string.select_position)
            )
            PositionsCard(
                onPositionClicked = onPositionClicked,
                dismiss = dismiss
            )
        }
    }
}

@Composable
internal fun PositionsCard(
    onPositionClicked: (Position) -> Unit,
    dismiss: () -> Unit
) {
    Position.entries
        .filter { it != Bench }
        .forEach { position ->
            GBText(
                text = stringResource(position.positionName),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onPositionClicked(position)
                        dismiss()
                    }
                    .padding(16.dp),
                alignment = Center
            )
        }
}

@Composable
internal fun InsertPlayerDialogTitle(
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

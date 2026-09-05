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

package com.sgale.gaztelubira.core.screens.insert_player.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerData
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerData.PictureType.Body
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerData.PictureType.Face
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField.ImageSelected
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField.Name
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField.UseSameImg
import com.sgale.gaztelubira.core.screens.insert_player.UiState
import com.sgale.gaztelubira.core.screens.insert_player.UiState.Capture
import com.sgale.gaztelubira.core.screens.insert_player.UiState.Dorsal
import com.sgale.gaztelubira.core.screens.insert_player.UiState.Position

@Composable
internal fun InsertPlayerUiComponents(
    data: InsertPlayerData,
    getAvDorsals: () -> List<Int>,
    updateField: (InsertPlayerField, Any) -> Unit,
    updateUi: (UiState) -> Unit,
    removeImage: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(8.dp)
    ) {
        MainInformation(
            playerName = data.name,
            dorsal = data.dorsal,
            position = data.position,
            onPlayerNameChanged = { newName -> updateField(Name, newName) },
            showAvailableDorsals = { updateUi(Dorsal(getAvDorsals())) },
            showAvailablePositions = { updateUi(Position) }
        )
        Spacer(Modifier.height(16.dp))
        InsertPlayerImages(
            faceImg = data.faceImage,
            bodyImg = data.bodyImage,
            useSameImage = data.useSameImage,
            onFaceClicked = { updateField(ImageSelected, Face) },
            onBodyClicked = { updateField(ImageSelected, Body) },
            onUseSameImageClicked = { updateField(UseSameImg, !data.useSameImage) },
            removeImage = { removeImage() },
            showMediaOrCamera = { updateUi(Capture) },
        )
    }
}

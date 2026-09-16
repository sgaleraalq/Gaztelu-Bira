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

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBImageBoxRequester
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBInsertButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTextField
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.gray_box_in_black_bg
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isNotLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerActions
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Generic.Dorsal
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Generic.Position
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Images
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Images.Body
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Images.Face
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Companion.emptyImage
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Name
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.body_image
import com.sgale.gaztelubira.multiplatform.ui.resources.dorsal
import com.sgale.gaztelubira.multiplatform.ui.resources.face_image
import com.sgale.gaztelubira.multiplatform.ui.resources.images
import com.sgale.gaztelubira.multiplatform.ui.resources.information
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_player
import com.sgale.gaztelubira.multiplatform.ui.resources.player_name
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun InsertPlayerMainInformation(
    state: InsertPlayerUiState,
    actions: InsertPlayerActions
) {
    GBText(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(Res.string.information),
        style = gBTypography().titleMedium,
        alignment = Start
    )
    GBTextField(
        modifier = Modifier.fillMaxWidth(),
        text = state.playerName,
        onTextChanged = { newName -> actions.updateField(Name(newName)) },
        label = stringResource(Res.string.player_name),
        firstCap = true,
        enabled = state.state.isNotLoading()
    )
    Spacer(
        modifier = Modifier.height(8.dp)
    )
    DorsalAndPosition(
        state = state,
        actions = actions
    )
}

@Composable
private fun DorsalAndPosition(
    state: InsertPlayerUiState,
    actions: InsertPlayerActions
) {
    val loading = state.state.isLoading()
    val dorsalText = if (state.playerDorsal == 0) {
        stringResource(Res.string.dorsal)
    } else {
        state.playerDorsal.toString()
    }

    Row(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(8.dp)
    ) {
        InformationComponent(
            modifier = Modifier.weight(1f),
            informationText = dorsalText,
            enabled = !loading,
            onClick = { actions.updateDialogState(Dorsal) }
        )

        InformationComponent(
            modifier = Modifier.weight(1f),
            informationText = state.playerPosition,
            enabled = !loading,
            onClick = { actions.updateDialogState(Position) }
        )
    }
}

@Composable
private fun InformationComponent(
    modifier: Modifier,
    informationText: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = gray_box_in_black_bg),
        enabled = enabled,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Center
        ) {
            GBText(text = informationText)
        }
    }
}

@Composable
internal fun InsertPlayerImages(
    state: InsertPlayerUiState,
    actions: InsertPlayerActions
) {
    GBText(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(Res.string.images),
        style = gBTypography().titleMedium,
        alignment = Start
    )
    PlayerImageRow(
        image = state.faceImage,
        actions = actions,
        action = Face,
        text = stringResource(Res.string.face_image)
    )
    PlayerImageRow(
        image = state.bodyImage,
        actions = actions,
        action = Body,
        text = stringResource(Res.string.body_image)
    )
}

@Composable
private fun PlayerImageRow(
    image: String,
    actions: InsertPlayerActions,
    action: Images,
    text: String
) {
    val placeholder = when (action) {
        Body -> AppImages.bodyPlayer
        Face -> AppImages.facePlayer
    }

    GBImageBoxRequester(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        imageUri = image,
        placeholder = placeholder,
        onClick = { actions.updateDialogState(action) },
        removeImage = { actions.updateField(emptyImage()) }
    )
}

@Composable
internal fun InsertPlayerButton(
    modifier: Modifier,
    state: InsertPlayerUiState,
    actions: InsertPlayerActions
) {
    val invalidMessage = state.handler.invalidReason?.let { stringResource(it.reason) }

    Spacer(modifier = modifier)
    GBInsertButton(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 16.dp),
        text = stringResource(Res.string.insert_player),
        loading = state.state.isLoading(),
        enabled = state.state.isNotLoading(),
        onInsert = {
            if (invalidMessage != null) {
                actions.onMissingField(invalidMessage)
            } else {
                actions.insertPlayer()
            }
        }
    )
}

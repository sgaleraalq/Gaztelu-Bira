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
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Capture
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Dorsals
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Positions
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Companion.emptyImage
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Name
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.SelectedPicture
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.PictureType
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.PictureType.Body
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.PictureType.Face
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.body_image
import com.sgale.gaztelubira.multiplatform.ui.resources.dorsal
import com.sgale.gaztelubira.multiplatform.ui.resources.face_image
import com.sgale.gaztelubira.multiplatform.ui.resources.images
import com.sgale.gaztelubira.multiplatform.ui.resources.information
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_player
import com.sgale.gaztelubira.multiplatform.ui.resources.player_name
import com.sgale.gaztelubira.multiplatform.ui.resources.position
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
    Spacer(Modifier.height(8.dp))
    DorsalAndPosition(state, actions)
}

@Composable
private fun DorsalAndPosition(
    state: InsertPlayerUiState,
    actions: InsertPlayerActions
) {
    val dorsalLabel = stringResource(Res.string.dorsal)

    Row(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(8.dp)
    ) {
        InformationComponent(
            modifier = Modifier.weight(1f),
            informationText = if (state.dorsal == 0) dorsalLabel else "$dorsalLabel: ${state.dorsal}",
            enabled = state.state.isNotLoading()
        ) { actions.showDialog(Dorsals) }

        InformationComponent(
            modifier = Modifier.weight(1f),
            informationText = state.position?.let { stringResource(it.label) }
                ?: stringResource(Res.string.position),
            enabled = state.state.isNotLoading()
        ) { actions.showDialog(Positions) }
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
        state = state,
        actions = actions,
        type = Face,
        text = stringResource(Res.string.face_image)
    )
    PlayerImageRow(
        state = state,
        actions = actions,
        type = Body,
        text = stringResource(Res.string.body_image)
    )
}

@Composable
private fun PlayerImageRow(
    state: InsertPlayerUiState,
    actions: InsertPlayerActions,
    type: PictureType,
    text: String
) {
    GBImageBoxRequester(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        imageUri = state.imageOf(type).takeIf { it.isNotBlank() },
        placeholder = if (type == Face) AppImages.facePlayer else AppImages.bodyPlayer,
        onClick = {
            /* The box that was tapped is where the next picture lands, so it is recorded before
               the source dialog opens. */
            actions.updateField(SelectedPicture(type))
            actions.showDialog(Capture)
        },
        removeImage = { actions.updateField(emptyImage(type)) }
    )
}

/**
 * The button stays clickable while the form is incomplete: pressing it reports the first field
 * that does not pass through [InsertPlayerActions.onMissingField], so the user is told what is
 * missing instead of being left with a dead button and no explanation.
 */
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

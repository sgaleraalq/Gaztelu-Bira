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

package com.sgale.gaztelubira.multiplatform.ui.insert.team.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBInsertButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBInsertImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTextField
import com.sgale.gaztelubira.multiplatform.designsystem.style.lightGray
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamActions
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField.Companion.EMPTY_IMAGE
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isInvalidInformation
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isNotLoading
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_team
import com.sgale.gaztelubira.multiplatform.ui.resources.team_name
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun InsertTeamName(
    state: InsertTeamUiState,
    onTeamNameChanged: (String) -> Unit
) {
    GBTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        text = state.teamName,
        onTextChanged = { onTeamNameChanged(it) },
        label = stringResource(Res.string.team_name),
        firstCap = true,
        enabled = state.state.isNotLoading(),
        error = state.state.isInvalidInformation()
    )
}

@Composable
internal fun InsertTeamImage(
    state: InsertTeamUiState,
    actions: InsertTeamActions
) {
    GBInsertImage(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .size(400.dp)
            .background(
                color = lightGray,
                shape = RoundedCornerShape(12.dp)
            ),
        imageModifier = Modifier.size(400.dp),
        iconModifier = Modifier.size(100.dp),
        image = state.teamImage,
        iconSize = 100.dp,
        onClick = actions.pickImage,
        removeImage = { actions.updateField(EMPTY_IMAGE) },
        isClickable = state.state.isNotLoading(),
        enableExpansion = false,
        placeholder = AppImages.teamCrest
    )
}

@Composable
internal fun InsertTeamButton(
    modifier: Modifier,
    loading: Boolean,
    onInsert: () -> Unit
) {
    Spacer(modifier = modifier)
    GBInsertButton(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 16.dp),
        text = stringResource(Res.string.insert_team),
        loading = loading,
        enabled = true,
        onInsert = onInsert
    )
}

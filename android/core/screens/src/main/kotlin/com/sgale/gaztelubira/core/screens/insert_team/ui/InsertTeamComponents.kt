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

package com.sgale.gaztelubira.core.screens.insert_team.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.domain.utils.CommonImage
import com.sgale.gaztelubira.core.domain.utils.rememberGalleryManager
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBInsertButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBInsertImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTextField
import com.sgale.gaztelubira.multiplatform.designsystem.components.SaverStatus.Team
import com.sgale.gaztelubira.multiplatform.designsystem.style.lightGray

@Composable
internal fun InsertTeamName(
    teamName: String,
    loading: Boolean,
    validInformation: Boolean,
    onTeamNameChanged: (String) -> Unit
) {
    GBTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        text = teamName,
        onTextChanged = { onTeamNameChanged(it) },
        label = stringResource(R.string.team_name),
        firstCap = true,
        enabled = !loading,
        error = !validInformation
    )
}

@Composable
internal fun InsertTeamImage(
    img: CommonImage?,
    loading: Boolean,
    updatePicture: (CommonImage?) -> Unit,
    removeImage: () -> Unit
) {
    val galleryManager = rememberGalleryManager { commonImage ->
        if (!loading) {
            updatePicture(commonImage)
        }
    }

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
        image = img,
        iconSize = 100.dp,
        onClick = { galleryManager.launch() },
        removeImage = { removeImage() },
        isClickable = !loading,
        enableExpansion = false,
        saverStatus = Team
    )
}

@Composable
internal fun InsertTeamButton(
    loading: Boolean,
    onInsert: () -> Unit
) {
    GBInsertButton(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 16.dp),
        text = stringResource(R.string.insert_team),
        loading = loading,
        enabled = true
    ) {
        onInsert()
    }
}

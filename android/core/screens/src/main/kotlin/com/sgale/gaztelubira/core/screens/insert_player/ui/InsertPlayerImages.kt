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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.gray_box_in_black_bg
import com.sgale.gaztelubira.multiplatform.designsystem.style.lightGray
import com.sgale.gaztelubira.multiplatform.designsystem.style.softGreen
import com.sgale.gaztelubira.core.domain.utils.CommonImage
import androidx.compose.ui.res.stringResource
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBImageBoxRequester
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText

@Composable
internal fun InsertPlayerImages(
    faceImg: CommonImage?,
    bodyImg: CommonImage?,
    useSameImage: Boolean,
    onFaceClicked: () -> Unit,
    onBodyClicked: () -> Unit,
    onUseSameImageClicked: () -> Unit,
    removeImage: () -> Unit,
    showMediaOrCamera: () -> Unit
) {
    GBText(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.images),
        style = gBTypography().titleMedium,
        alignment = Start
    )
    GBImageBoxRequester(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.face_image),
        imageUri = faceImg?.uri,
        onClick = {
            onFaceClicked()
            showMediaOrCamera()
        },
        removeImage = { removeImage() }
    )
    GBImageBoxRequester(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.body_image),
        imageUri = bodyImg?.uri,
        onClick = {
            onBodyClicked()
            showMediaOrCamera()
        },
        removeImage = { removeImage() }
    )
    Spacer(Modifier.height(2.dp))
//    UseSameImageBox(
//        checked = useSameImage,
//        enabled = faceImg != null || bodyImg != null,
//    ) { onUseSameImageClicked() }
}

@Composable
internal fun UseSameImageBox(
    checked: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically
    ) {
        GBText(
            modifier = Modifier
                .weight(1f)
                .clickable { if (enabled) onClick() }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(R.string.use_same_image),
            style = gBTypography().bodySmall.copy(
                fontStyle = Italic
            )
        )
        Checkbox(
            checked = checked,
            onCheckedChange = { if (enabled) onClick() },
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkmarkColor = gray_box_in_black_bg,
                disabledUncheckedColor = lightGray,
                checkedColor = softGreen,
                uncheckedColor = lightGray
            )
        )
    }
}

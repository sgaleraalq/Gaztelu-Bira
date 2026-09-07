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


package com.sgale.gaztelubira.multiplatform.ui.review_image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBImage
import com.sgale.gaztelubira.multiplatform.designsystem.style.gray_box_in_black_bg
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.accept
import com.sgale.gaztelubira.multiplatform.ui.resources.repeat
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ReviewImageViewUI(
    state: ReviewImageUiState,
    actions: ReviewImageActions
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        /**
         * The buttons are usable before the preview lands, so nothing here waits on the image.
         */
        state.image?.let { image ->
            GBImage(
                modifier = Modifier.align(Center).fillMaxSize(),
                image = image,
                contentScale = Fit
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(BottomCenter)
                .padding(16.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            GBElevatedButton(
                modifier = Modifier.weight(1f).padding(8.dp),
                text = stringResource(Res.string.repeat),
                textColor = gray_box_in_black_bg,
                onClick = actions.onRepeat
            )
            GBElevatedButton(
                modifier = Modifier.weight(1f).padding(8.dp),
                text = stringResource(Res.string.accept),
                textColor = gray_box_in_black_bg,
                onClick = actions.onAccept
            )
        }
    }
}

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

package com.sgale.gaztelubira.multiplatform.designsystem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.white_in_gray_box
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_gaztelu_bira
import com.sgale.gaztelubira.multiplatform.ui.resources.img_gaztelu_bira
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GBLogoutDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    logout: String,
    sureLogout: String,
    no: String,
    yes: String
) {
    GBDialog(
        show = true,
        dismiss = { onCancel() }
    ) { modifier ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = spacedBy(12.dp)
            ) {
                GBImage(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(50))
                        .border(width = 1.dp, color = White, shape = RoundedCornerShape(50)),
                    painter = painterResource(Res.drawable.img_gaztelu_bira),
                )
                GBText(
                    modifier = Modifier.weight(1f),
                    text = logout,
                    style = gBTypography().titleLarge.copy(
                        fontWeight = Bold
                    )
                )
            }

            GBText(
                modifier = Modifier.padding(12.dp),
                text = sureLogout,
                style = gBTypography().titleMedium,
                textColor = white_in_gray_box
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = spacedBy(12.dp)
            ) {
                GBElevatedButton(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    text = no,
                    onClick = { onCancel() }
                )
                GBElevatedButton(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    text = yes,
                    onClick = {
                        onCancel() // dismiss dialog
                        onConfirm()
                    }
                )
            }
        }
    }
}

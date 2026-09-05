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

package com.sgale.gaztelubira.multiplatform.ui.auth.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTextField
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_eye_close
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_eye_open
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun AuthTextField(
    text: String,
    label: String,
    style: TextStyle = gBTypography().bodyMedium,
    icon: DrawableResource? = null,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onTextChanged: (String) -> Unit,
    showPassword: () -> Unit = {},
    firstCap: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(16.dp)
    ) {
        if (icon != null) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(icon),
                contentDescription = null,
                tint = White
            )
        }

        GBTextField(
            modifier = Modifier.weight(1f),
            text = text,
            onTextChanged = { onTextChanged(it) },
            label = label,
            style = style,
            trailingIcon = {
                if (isPassword) {
                    Icon(
                        modifier = Modifier.size(24.dp).clickable { showPassword() },
                        painter = painterResource(
                            if (isPasswordVisible) Res.drawable.ic_eye_open else Res.drawable.ic_eye_close),
                        contentDescription = null,
                        tint = White
                    )
                }
            },
            isEmail = !isPassword,
            isPassword = isPassword && !isPasswordVisible,
            firstCap = firstCap
        )
    }
}

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

package com.sgale.gaztelubira.multiplatform.ui.auth.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBProgressDialog
import com.sgale.gaztelubira.multiplatform.designsystem.style.login_button_color
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Loading

@Composable
fun AuthButton(
    text: String,
    authState: AuthState,
    onClick: () -> Unit
) {
    if (authState is Loading) {
        GBProgressDialog(
            show = true,
            color = login_button_color,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        GBElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            onClick = { onClick() },
            backgroundColor = login_button_color,
            textColor = White
        )
    }
}

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

package com.sgale.gaztelubira.multiplatform.ui.auth.login.ui

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.ui.auth.common.AuthTextField
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginActions
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginUiState.LoginUser
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.email_id
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_at_sign
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_padlock
import com.sgale.gaztelubira.multiplatform.ui.resources.password
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LoginTextFields(
    user: LoginUser,
    actions: LoginActions
) {
    Column(
        verticalArrangement = spacedBy(4.dp)
    ) {
        AuthTextField(
            text = user.email,
            label = stringResource(Res.string.email_id),
            icon = Res.drawable.ic_at_sign,
            onTextChanged = actions.onEmailChanged
        )
        AuthTextField(
            text = user.password,
            label = stringResource(Res.string.password),
            icon = Res.drawable.ic_padlock,
            isPassword = true,
            isPasswordVisible = user.isPasswordVisible,
            onTextChanged = actions.onPasswordChanged,
            showPassword = actions.onTogglePasswordVisibility
        )
    }
}

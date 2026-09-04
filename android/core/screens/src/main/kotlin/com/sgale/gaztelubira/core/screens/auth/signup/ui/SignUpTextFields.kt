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

package com.sgale.gaztelubira.core.screens.auth.signup.ui

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.auth.common.AuthTextField
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpUser
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpViewModel.SignUpField
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpViewModel.SignUpField.Email
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpViewModel.SignUpField.Name
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpViewModel.SignUpField.Password
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpViewModel.SignUpField.PasswordVisible
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpViewModel.SignUpField.RepeatPassword
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpViewModel.SignUpField.RepeatPasswordVisible
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource

@Composable
fun SignUpTextFields(
    user: SignUpUser,
    changeUserValue: (SignUpField, Any) -> Unit
) {
    Column(
        verticalArrangement = spacedBy(4.dp)
    ) {
        AuthTextField(
            text = user.name,
            label = stringResource(R.string.name),
            style = gBTypography().bodyLarge,
            onTextChanged = { changeUserValue(Name, it) },
            firstCap = true
        )
        Spacer(Modifier.height(16.dp))
        AuthTextField(
            text = user.email,
            label = stringResource(R.string.email_id),
            icon = R.drawable.ic_at_sign,
            onTextChanged = { changeUserValue(Email, it) }
        )
        AuthTextField(
            text = user.password,
            label = stringResource(R.string.password),
            icon = R.drawable.ic_padlock,
            isPassword = true,
            isPasswordVisible = user.passwordVisible,
            onTextChanged = { changeUserValue(Password, it) },
            showPassword = { changeUserValue(PasswordVisible, !user.passwordVisible) }
        )
        AuthTextField(
            text = user.repeatPassword,
            label = stringResource(R.string.repeat_password),
            icon = R.drawable.ic_padlock,
            isPassword = true,
            isPasswordVisible = user.repeatPasswordVisible,
            onTextChanged = { changeUserValue(RepeatPassword, it) },
            showPassword = { changeUserValue(RepeatPasswordVisible, !user.repeatPasswordVisible) }
        )
    }
}

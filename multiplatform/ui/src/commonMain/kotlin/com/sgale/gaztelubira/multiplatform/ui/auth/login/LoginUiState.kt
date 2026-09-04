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

package com.sgale.gaztelubira.multiplatform.ui.auth.login

import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Default
import com.sgale.gaztelubira.multiplatform.ui.auth.common.Email
import com.sgale.gaztelubira.multiplatform.ui.auth.common.valid
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginUiState.ValidationError.InvalidEmail
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.error_invalid_email
import com.sgale.gaztelubira.multiplatform.ui.resources.error_login
import org.jetbrains.compose.resources.StringResource

data class LoginUiState(
    val user: LoginUser = LoginUser(),
    val error: ValidationError? = null,
    val auth: AuthState = Default
) {
    data class LoginUser(
        val email: Email = "",
        val password: String = "",
        val isPasswordVisible: Boolean = false
    ) {
        fun validate(): ValidationError? = when {
            !email.valid() -> InvalidEmail
            else -> null
        }
    }

    fun errorMessage(): StringResource? = when {
        error != null -> error.messageRes
        auth == AuthState.Error -> Res.string.error_login
        else -> null
    }

    sealed class ValidationError(
        val messageRes: StringResource
    ) {
        data object InvalidEmail : ValidationError(Res.string.error_invalid_email)
    }
}

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

package com.sgale.gaztelubira.multiplatform.ui.auth.signup

import com.sgale.gaztelubira.multiplatform.ui.auth.common.Email
import com.sgale.gaztelubira.multiplatform.ui.auth.common.Password
import com.sgale.gaztelubira.multiplatform.ui.auth.common.blank
import com.sgale.gaztelubira.multiplatform.ui.auth.common.mismatch
import com.sgale.gaztelubira.multiplatform.ui.auth.common.noDigits
import com.sgale.gaztelubira.multiplatform.ui.auth.common.short
import com.sgale.gaztelubira.multiplatform.ui.auth.common.valid
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpUser.ValidationError.EmptyPassword
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpUser.ValidationError.InvalidEmail
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpUser.ValidationError.InvalidName
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpUser.ValidationError.PasswordMismatch
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpUser.ValidationError.PasswordNoNumber
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpUser.ValidationError.PasswordTooShort
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.error_empty_password
import com.sgale.gaztelubira.multiplatform.ui.resources.error_invalid_email
import com.sgale.gaztelubira.multiplatform.ui.resources.error_invalid_name
import com.sgale.gaztelubira.multiplatform.ui.resources.error_password_mismatch
import com.sgale.gaztelubira.multiplatform.ui.resources.error_password_no_number
import com.sgale.gaztelubira.multiplatform.ui.resources.error_password_too_short
import org.jetbrains.compose.resources.StringResource

data class SignUpUser(
    val name: String = "",
    val email: Email = "",
    val password: Password = "",
    val repeatPassword: Password = "",
    val passwordVisible: Boolean = false,
    val repeatPasswordVisible: Boolean = false
) {
    fun isNotValid(): ValidationError? {
        return when {
            name.isBlank() -> InvalidName
            !email.valid() -> InvalidEmail
            password.blank() -> EmptyPassword
            password.mismatch(repeatPassword) -> PasswordMismatch
            password.short() -> PasswordTooShort
            password.noDigits() -> PasswordNoNumber
            else -> null
        }
    }

    sealed class ValidationError(
        val messageRes: StringResource
    ) {
        object InvalidName : ValidationError(Res.string.error_invalid_name)
        object InvalidEmail : ValidationError(Res.string.error_invalid_email)
        object EmptyPassword : ValidationError(Res.string.error_empty_password)
        object PasswordTooShort : ValidationError(Res.string.error_password_too_short)
        object PasswordNoNumber : ValidationError(Res.string.error_password_no_number)
        object PasswordMismatch : ValidationError(Res.string.error_password_mismatch)
    }
}

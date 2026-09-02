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

package com.sgale.gaztelubira.core.screens.auth.signup

import com.sgale.gaztelubira.core.domain.utils.Email
import com.sgale.gaztelubira.core.domain.utils.Password
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpUser.ValidationError.InvalidName

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
//            TODO
//            !email.valid() -> InvalidEmail
//            password.blank() -> EmptyPassword
//            password.mismatch(repeatPassword) -> PasswordMismatch
//            password.short() -> PasswordTooShort
//            password.noDigits() -> PasswordNoNumber
            else -> null
        }
    }

    sealed class ValidationError(val messageRes: Int) {
        object InvalidName : ValidationError(R.string.error_invalid_name)
        object InvalidEmail : ValidationError(R.string.error_invalid_email)
        object EmptyPassword : ValidationError(R.string.error_empty_password)
        object PasswordTooShort : ValidationError(R.string.error_password_too_short)
        object PasswordNoNumber : ValidationError(R.string.error_password_no_number)
        object PasswordMismatch : ValidationError(R.string.error_password_mismatch)
    }
}

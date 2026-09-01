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

package com.sgale.gaztelubira.core.domain.auth.usecase

import com.sgale.gaztelubira.core.domain.auth.AuthResult
import com.sgale.gaztelubira.core.domain.auth.AuthResult.Error
import com.sgale.gaztelubira.core.domain.auth.AuthResult.Success
import com.sgale.gaztelubira.core.domain.auth.GoogleResult
import com.sgale.gaztelubira.core.domain.auth.GoogleResult.GoogleLogin
import com.sgale.gaztelubira.core.domain.auth.GoogleResult.GoogleSignUp
import com.sgale.gaztelubira.core.domain.model.user.UserModel
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.usecase.users.InsertUser
import com.sgale.gaztelubira.core.domain.usecase.users.IsUserInserted
import javax.inject.Inject

class LoginGoogle @Inject constructor(
    private val isUserInserted: IsUserInserted,
    private val insertUser: InsertUser
) {
    suspend operator fun invoke(user: UserModel?): AuthResult {
        if (user != null) {
            val googleResult = resolveGoogleUser(user.uid)
            return when (googleResult) {
                is GoogleLogin -> { loginGoogle(googleResult.user) }
                is GoogleSignUp -> { signupGoogle(user) }
            }
        } else {
            return Error()
        }
    }

    private suspend fun resolveGoogleUser(id: FirebaseId): GoogleResult {
        val user = isUserInserted(id)
        return if (user != null) {
            GoogleLogin(user)
        } else {
            GoogleSignUp
        }
    }

    private fun loginGoogle(user: UserModel): AuthResult = Success(user)
    private suspend fun signupGoogle(user: UserModel): AuthResult {
        val inserted = insertUser(user)
        return if (inserted) {
            Success(user)
        } else {
            Error()
        }
    }
}

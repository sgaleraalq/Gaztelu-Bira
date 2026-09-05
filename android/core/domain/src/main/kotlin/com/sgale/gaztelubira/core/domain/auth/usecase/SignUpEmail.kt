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

package com.sgale.gaztelubira.core.domain.auth.usecase

import com.sgale.gaztelubira.core.domain.auth.AuthResult
import com.sgale.gaztelubira.core.domain.auth.AuthResult.Error
import com.sgale.gaztelubira.core.domain.auth.AuthResult.Success
import com.sgale.gaztelubira.core.domain.auth.IAuthRepository
import com.sgale.gaztelubira.core.domain.usecase.users.InsertUser
import javax.inject.Inject

/**
 * Entry point of sign up with Firebase
 */
class SignUpEmail @Inject constructor(
    private val repository: IAuthRepository,
    private val insertUser: InsertUser
) {
    suspend operator fun invoke(
        name: String, email: String, password: String
    ): AuthResult {
        val authResult = repository.signUpWithEmail(name, email, password)

        return when (authResult) {
            is Success -> {
                insertUser(authResult.user)
                authResult
            }
            is Error -> authResult
        }
    }
}

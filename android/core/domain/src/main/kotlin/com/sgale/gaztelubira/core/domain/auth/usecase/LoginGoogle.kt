/*
 * Designed and developed by 2026 sgale (Sergio Galera)
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
import com.sgale.gaztelubira.core.domain.auth.repository.AuthRepository
import com.sgale.gaztelubira.core.domain.usecase.users.InsertUser
import com.sgale.gaztelubira.core.domain.usecase.users.IsUserInserted
import javax.inject.Inject

/**
 * Google does not tell a login from a sign up, so a first-time account is stored on the way in.
 */
class LoginGoogle @Inject constructor(
    private val repository: AuthRepository,
    private val isUserInserted: IsUserInserted,
    private val insertUser: InsertUser
) {
    suspend operator fun invoke(): AuthResult {
        val authResult = repository.loginWithGoogle()
        if (authResult !is Success) return authResult

        isUserInserted(authResult.user.uid)?.let { stored -> return Success(stored) }

        return if (insertUser(authResult.user)) authResult else Error()
    }
}

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

package com.sgale.gaztelubira.core.data.auth

import com.sgale.gaztelubira.core.domain.auth.AuthResult
import com.sgale.gaztelubira.core.domain.auth.AuthResult.Error
import com.sgale.gaztelubira.core.domain.auth.AuthResult.Success
import com.sgale.gaztelubira.core.domain.auth.IAuthRepository
import com.sgale.gaztelubira.core.domain.auth.IAuthRepository.Companion.LOGIN_ERROR
import com.sgale.gaztelubira.core.domain.auth.IAuthRepository.Companion.SIGN_UP_ERROR
import com.sgale.gaztelubira.core.domain.auth.usecase.LoginGoogle
import com.sgale.gaztelubira.core.domain.model.user.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl @Inject constructor(
    private val googleAuthUiProvider: GoogleAuthProvider,
    private val loginGoogle: LoginGoogle
): IAuthRepository {

    private val auth = Firebase.auth

    override fun logout() { auth.signOut() }

    override fun getUserSession() = auth.currentUser?.uid

    override suspend fun signUpWithEmail(
        name: String,
        email: String,
        password: String
    ): AuthResult =
        runCatching {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: error(SIGN_UP_ERROR)
            updateUserName(name, user)
            Success(
                UserModel(
                    uid = user.uid,
                    name = user.displayName,
                    email = user.email
                )
            )
        }.getOrElse { Error(it.message.orEmpty(), it) }

    override suspend fun loginWithEmail(
        email: String,
        password: String
    ): AuthResult =
        runCatching {
            auth.signInWithEmailAndPassword(email, password).await()
            val user = auth.currentUser ?: error(LOGIN_ERROR)
            Success(
                UserModel(
                    uid = user.uid,
                    name = user.displayName,
                    email = user.email
                )
            )
        }.getOrElse { Error(it.message.orEmpty(), it) }

    override suspend fun loginWithGoogle(): AuthResult {
        val googleUser = googleAuthUiProvider.signIn()
        return loginGoogle(googleUser)
    }

    private suspend fun updateUserName(name: String, user: FirebaseUser) {
        user.updateProfile(userProfileChangeRequest {
            displayName = name
        }).await()
    }
}

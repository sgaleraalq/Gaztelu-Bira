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

package com.sgale.gaztelubira.core.network.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.sgale.gaztelubira.core.domain.auth.AuthResult
import com.sgale.gaztelubira.core.domain.auth.AuthResult.Error
import com.sgale.gaztelubira.core.domain.auth.AuthResult.Success
import com.sgale.gaztelubira.core.domain.auth.repository.AuthRepository
import com.sgale.gaztelubira.core.domain.auth.repository.AuthRepository.Companion.LOGIN_ERROR
import com.sgale.gaztelubira.core.domain.auth.repository.AuthRepository.Companion.SIGN_UP_ERROR
import com.sgale.gaztelubira.core.domain.auth.repository.GoogleAuth
import com.sgale.gaztelubira.core.domain.model.user.User
import com.sgale.gaztelubira.core.domain.model.user.UserId
import com.sgale.gaztelubira.core.domain.model.user.UserRole
import com.sgale.gaztelubira.core.domain.model.user.UserRole.ADMIN
import com.sgale.gaztelubira.core.domain.model.user.UserRole.GUEST
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val ADMIN_CLAIM = "admin"

internal class AuthRepositoryImpl @Inject constructor(
    private val googleSignIn: GoogleAuth
) : AuthRepository {
    private val auth = Firebase.auth

    override fun logout() { auth.signOut() }

    override fun getUserSession() = auth.currentUser?.uid

    /**
     * The token is refreshed first, so a claim granted since the last sign-in counts from the next
     * launch. Offline, the cached token is the best there is; with no token at all, nobody is an
     * admin.
     */
    override suspend fun getUserRole(): UserRole {
        val user = auth.currentUser ?: return GUEST
        val claims = user.claims(forceRefresh = true) ?: user.claims(forceRefresh = false)
        return if (claims?.get(ADMIN_CLAIM) == true) ADMIN else GUEST
    }

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
                User(
                    uid = UserId(user.uid),
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
                User(
                    uid = UserId(user.uid),
                    name = user.displayName,
                    email = user.email
                )
            )
        }.getOrElse { Error(it.message.orEmpty(), it) }

    override suspend fun loginWithGoogle(): AuthResult =
        googleSignIn.signIn()?.let(::Success) ?: Error(LOGIN_ERROR)

    private suspend fun updateUserName(name: String, user: FirebaseUser) {
        user.updateProfile(userProfileChangeRequest {
            displayName = name
        }).await()
    }

    private suspend fun FirebaseUser.claims(forceRefresh: Boolean): Map<String, Any>? =
        try {
            getIdToken(forceRefresh).await().claims
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
}

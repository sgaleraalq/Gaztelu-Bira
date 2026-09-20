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

package com.sgale.gaztelubira.core.network.auth

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.sgale.gaztelubira.core.common.utils.TAG
import com.sgale.gaztelubira.core.domain.auth.IGoogleSignIn
import com.sgale.gaztelubira.core.domain.model.user.UserModel
import com.sgale.gaztelubira.core.domain.model.user.UserRole.GUEST
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Android's answer to [IGoogleSignIn]: Credential Manager asks the user for an
 * account and Firebase turns the resulting id token into a session.
 */
internal class AndroidGoogleSignIn @Inject constructor(
    private val context: Context,
    private val credentialManager: CredentialManager,
    @param:GoogleClientId private val googleClientId: String
) : IGoogleSignIn {
    private val firebaseAuth = Firebase.auth

    override suspend fun signIn(): UserModel? {
        return try {
            credentialManager
                .getCredential(context = context, request = credentialRequest())
                .credential
                .asGoogleId()
                ?.let { googleId -> authenticate(googleId) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Google sign in failed: ${e.message}")
            null
        }
    }

    private fun Credential.asGoogleId(): GoogleIdTokenCredential? =
        if (this is CustomCredential && type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            GoogleIdTokenCredential.createFrom(data)
        } else {
            Log.e(TAG, "Expected a Google id token, got $type")
            null
        }

    private suspend fun authenticate(googleId: GoogleIdTokenCredential): UserModel? {
        val credential = GoogleAuthProvider.getCredential(googleId.idToken, null)
        val user = firebaseAuth.signInWithCredential(credential).await().user ?: return null

        return UserModel(
            uid = user.uid,
            name = user.displayName,
            email = user.email,
            img = googleId.profilePictureUri?.toString(),
            role = GUEST
        )
    }

    private fun credentialRequest(): GetCredentialRequest =
        GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setAutoSelectEnabled(true)
                    .setServerClientId(googleClientId)
                    .build()
            )
            .build()
}

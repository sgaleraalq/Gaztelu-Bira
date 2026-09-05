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

package com.sgale.gaztelubira.core.data.auth

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.sgale.gaztelubira.core.domain.model.user.UserModel
import com.sgale.gaztelubira.core.domain.model.user.UserRole.Guest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.createFrom
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider.getCredential
import com.google.firebase.auth.auth
import javax.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class GoogleAuthProvider @Inject constructor(
    private val activityBridge: ActivityBridge,
    private val credentialManager: CredentialManager,
    @param:GoogleClientId private val googleClientId: String
) {
    private val firebaseAuth = Firebase.auth
    suspend fun signIn(): UserModel? = try {
        val credential = credentialManager.getCredential(
            context = activityBridge.requireActivity(),
            request = getCredentialRequest()
        ).credential
        handleSignIn(credential)
    } catch (e: Exception) {
        Log.e("sgalera", "CredentialManager failed $e")
        null
    }

    private suspend fun handleSignIn(credential: Credential): UserModel? = when {
        credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
            try {
                val googleIdTokenCredential = createFrom(credential.data)
                val firebaseCredential = getCredential(googleIdTokenCredential.idToken, null)

                val result = authenticateWithFirebase(firebaseCredential)

                result.getOrNull()?.let { fbUser: FirebaseUser ->
                    Log.i("sgalera", "Correct Google login")
                    UserModel(
                        uid = fbUser.uid,
                        name = fbUser.displayName,
                        email = fbUser.email,
                        img = googleIdTokenCredential.profilePictureUri.toString(),
                        role = Guest
                    )
                } ?: run {
                    Log.e("sgalera", "Error uploading to Firebase")
                    null
                }

            } catch (e: GoogleIdTokenParsingException) {
                Log.e("sgalera", "Error logging in $e")
                null
            }
        }

        else -> {
            Log.e("sgalera", "No credentials")
            null
        }
    }

    private suspend fun authenticateWithFirebase(credential: AuthCredential): Result<FirebaseUser?> =
        suspendCancellableCoroutine { cont ->
            firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener {
                    cont.resume(Result.success(it.user))
                }
                .addOnFailureListener { e ->
                    cont.resume(Result.failure(e))
                }
        }

    private fun getCredentialRequest(): GetCredentialRequest =
        GetCredentialRequest.Builder()
            .addCredentialOption(getGoogleIdOption())
            .build()

    private fun getGoogleIdOption(): GetGoogleIdOption =
        GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .setServerClientId(googleClientId)
            .build()
}

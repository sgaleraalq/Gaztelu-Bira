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

package com.sgale.gaztelubira.core.data.network.firestore

import com.sgale.gaztelubira.core.data.mappers.asModel
import com.sgale.gaztelubira.core.data.mappers.asResponse
import com.sgale.gaztelubira.core.data.network.response.UserResponse
import com.sgale.gaztelubira.core.domain.model.utils.GazteluBiraUtils
import com.sgale.gaztelubira.core.domain.auth.UserSession
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.model.user.UserModel
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.USERS
import com.sgale.gaztelubira.core.domain.repository.firestore.IFbUsers
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FbUsers @Inject constructor(
    private val firestore: FirebaseFirestore
) : IFbUsers {

    override suspend fun getUser(uid: FirebaseId): UserSession? =
        coroutineScope {
            val team = async { getUserTeam() }.await()
            val user = async { getUserInformation(uid) }.await()
            val playerId = async { getPlayerId(uid) }.await()

            if (user != null) {
                UserSession(user, team, playerId)
            } else {
                null
            }
        }

    override suspend fun insertUser(user: UserModel): Boolean {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(USERS).document(user.uid).set(user.asResponse())
                .addOnSuccessListener {
                    cancellableContinuation.resume(true)
                }.addOnFailureListener {
                    cancellableContinuation.resume(false)
                }
        }
    }

    override suspend fun isUserInserted(uid: FirebaseId): UserModel? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(USERS).document(uid).get()
                .addOnSuccessListener { snapshot ->
                    val userResponse = snapshot.toObject(UserResponse::class.java)
                    cancellableContinuation.resumeWith(Result.success(userResponse?.asModel()))
                }
                .addOnFailureListener {
                    cancellableContinuation.resume(null)
                }
        }
    }

    private suspend fun getUserInformation(uid: String): UserModel? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(USERS).document(uid).get()
                .addOnSuccessListener { snapshot ->
                    val userResponse = snapshot.toObject(UserResponse::class.java)
                    cancellableContinuation.resumeWith(Result.success(userResponse?.asModel()))
                }
                .addOnFailureListener {
                    cancellableContinuation.resume(null)
                }
        }
    }

    private suspend fun getPlayerId(uid: String): FirebaseId {
        return "TODO"
    }

    private fun getUserTeam(): TeamModel {
        return GazteluBiraUtils.GAZTELU_BIRA
    }
}

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

package com.sgale.gaztelubira.core.network.firebase.implementation.insert

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sgale.gaztelubira.core.domain.model.team.Team
import com.sgale.gaztelubira.core.domain.model.utils.TEAMS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.TeamTimestamp
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.INFORMATION
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult
import com.sgale.gaztelubira.core.network.firebase.response.team.TeamMapper.asResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

internal class InsertTeam @Inject constructor(
    firestore: FirebaseFirestore,
    gbSettings: IGBPreferences
) : Insert(firestore, gbSettings) {
    suspend operator fun invoke(team: Team): FirebaseInsertResult {
        return suspendCancellableCoroutine { continuation ->
            val timestamp = TeamTimestamp()
            firestore.collection("debug") // TODO
                .document(INFORMATION)
                .set(timestamp, SetOptions.merge())
            firestore.collection("debug") // TODO
                .document(INFORMATION)
                .collection(FirebaseConstants.TEAMS)
                .document(team.id)
                .set(team.asResponse())
                .addOnSuccessListener {
                    gbSettings.setTimestamp(timestamp.teamsInsertion, TEAMS_INSERTION)
                    continuation.resume(FirebaseInsertResult.TeamInserted)
                }.addOnFailureListener { error ->
                    Log.e("GBFirebase", "Error inserting team ${error.message}")
                    continuation.resume(FirebaseInsertResult.ErrorInsert(error.message))
                }
        }
    }
}

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

package com.sgale.gaztelubira.core.network.firebase.implementation.fetch

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.utils.MATCHES_INSERTION
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.INFORMATION
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.MATCHES
import com.sgale.gaztelubira.core.network.firebase.response.match.MatchMapper.asModel
import com.sgale.gaztelubira.core.network.firebase.response.match.MatchResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FetchMatches @Inject constructor(
    firestore: FirebaseFirestore,
    gbSettings: IGBPreferences
) : Fetch(firestore, gbSettings) {
    suspend fun fetchMatches(): List<Match> {
        return try {
//            val teamsMap = abstractDb.getTeamsMap()
            getTimestampAndSet(INFORMATION, MATCHES_INSERTION)
            firestore.collection(season)
                .document(INFORMATION)
                .collection(MATCHES)
                .get()
                .await()
                .toObjects(MatchResponse::class.java)
                .map { it.asModel() }
        } catch (e: Exception) {
            Log.e("GBFirebase", "Couldn't get data, error: ${e.message}")
            emptyList()
        }
    }
}

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
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.utils.STATS_INSERTION
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.MATCHES
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.STATS
import com.sgale.gaztelubira.core.network.firebase.response.match.MatchStatsMapper.asModel
import com.sgale.gaztelubira.core.network.firebase.response.match.MatchStatsResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FetchMatchesStats @Inject constructor(
    firestore: FirebaseFirestore,
    gbSettings: IGBPreferences
) : Fetch(firestore, gbSettings) {
    suspend operator fun invoke(): List<MatchStatsModel> =
        try {
            getTimestampAndSet(STATS, STATS_INSERTION)
            firestore.collection(season)
                .document(STATS)
                .collection(MATCHES)
                .get()
                .await()
                .toObjects(MatchStatsResponse::class.java)
                .map { it.asModel() }
        } catch (e: Exception) {
            Log.e("GBFirebase", "Couldn't get data, error: ${e.message}")
            emptyList()
        }
}

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

import com.google.firebase.firestore.FirebaseFirestore
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import javax.inject.Inject

internal class FetchPlayerStats @Inject constructor(
    firestore: FirebaseFirestore,
    gbSettings: IGBPreferences
) : Fetch(firestore, gbSettings) {
//    suspend operator fun invoke(): PlayerStats? {
//        return try {
//            val matches = firestore
//                .collection(gbSettings.getSeason())
//                .document(STATS)
//                .collection(PLAYERS)
//                .document(playerId)
//                .collection(MATCHES)
//                .get()
//                .await()
//
//            val statsByMatch = matches.documents.mapNotNull { match ->
//                val stats = match.toObject(PlayerStatsResponse::class.java)?.asStats()
//                if (stats != null) match.id to stats else null
//            }.toMap()
//
//            PlayerStats(
//                id = playerId,
//                player = ERROR_PLAYER, // TODO playersMap[playerId] ?: ErrorPlayer,
//                stats = statsByMatch,
//                percentage = 0.0
//            )
//        } catch (e: Exception) {
//            Log.e("GBFirebase", "Couldn't get data, error: ${e.message}")
//            null
//        }
//    }
}

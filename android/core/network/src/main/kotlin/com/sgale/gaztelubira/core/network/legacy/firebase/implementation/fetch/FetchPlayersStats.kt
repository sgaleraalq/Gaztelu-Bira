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

package com.sgale.gaztelubira.core.network.legacy.firebase.implementation.fetch

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.sgale.gaztelubira.core.domain.legacy.model.player.Player.Companion.ERROR_PLAYER
import com.sgale.gaztelubira.core.domain.legacy.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.legacy.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.FirebaseConstants.MATCHES
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.FirebaseConstants.PLAYERS
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.FirebaseConstants.STATS
import com.sgale.gaztelubira.core.network.legacy.firebase.response.player.PlayerStatsResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FetchPlayersStats @Inject constructor(
    firestore: FirebaseFirestore,
    gbSettings: IGBPreferences
) : Fetch(firestore, gbSettings) {
    suspend operator fun invoke(): List<PlayerStats> = coroutineScope {
        seasonCollection(STATS, PLAYERS)
            .get()
            .await()
            .documents
            .map { player -> async { player.asPlayerStats() } }
            .awaitAll()
    }

    private suspend fun DocumentSnapshot.asPlayerStats(): PlayerStats =
        PlayerStats(
            id = id,
            player = ERROR_PLAYER, // TODO playersMap[playerId] ?: ERROR_PLAYER,
            stats = reference.collection(MATCHES)
                .get()
                .await()
                .documents
                .mapNotNull { match ->
                    match.toObject(PlayerStatsResponse::class.java)
                        ?.asStats()
                        ?.let { stats -> match.id to stats }
                }.toMap(),
            percentage = 0.0
        )
}

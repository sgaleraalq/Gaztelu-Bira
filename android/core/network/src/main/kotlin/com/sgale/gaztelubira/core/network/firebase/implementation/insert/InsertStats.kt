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

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.stats.Stats
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.model.utils.MATCHES_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.MATCHES_STATS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.MatchesTimestamp
import com.sgale.gaztelubira.core.domain.model.utils.PLAYERS_STATS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.StatsTimestamp
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.INFORMATION
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult
import com.sgale.gaztelubira.core.network.firebase.response.match.MatchMapper.asResponse
import com.sgale.gaztelubira.core.network.firebase.response.match.MatchStatsMapper.asResponse
import com.sgale.gaztelubira.core.network.firebase.response.stats.StatsMapper.asResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach

internal class InsertStats @Inject constructor(
    firestore: FirebaseFirestore,
    gbSettings: IGBPreferences
) : Insert(firestore, gbSettings) {
    suspend operator fun invoke(
        match: Match,
        matchStats: MatchStatsModel,
        playerStats: Map<FirebaseId, Stats>
    ): FirebaseInsertResult {
        val matchId = match.id
        require(matchId.isNotBlank()) { "Match ID can't be blank" }

        return runCatching {
            val batch = firestore.batch()

            val statsTimestamp = StatsTimestamp()
            val matchesTimestamp = MatchesTimestamp()
            val matchStampRef = firestore.collection(season).document(INFORMATION)
            val statsStampRef = firestore.collection(season).document(FirebaseConstants.STATS)

            val matchDocRef = getMatchDocRef(false, matchId)
            val matchStatsDocRef = getMatchDocRef(true, matchId)

            batch.set(matchDocRef, match.copy(id = matchId).asResponse())
            batch.set(matchStatsDocRef, matchStats.copy(id = matchId).asResponse())

            playerStats.forEach { (id, stats) ->
                val playerDocRef = firestore
                    .collection(season)
                    .document(FirebaseConstants.STATS)
                    .collection(FirebaseConstants.PLAYERS)
                    .document(id)

                val playerStatsDocRef = playerDocRef
                    .collection(FirebaseConstants.MATCHES)
                    .document(matchId)

                batch.set(playerDocRef, mapOf("id" to id), SetOptions.merge())
                batch.set(playerStatsDocRef, stats.asResponse())
            }

            batch.set(matchStampRef, matchesTimestamp, SetOptions.merge())
            batch.set(statsStampRef, statsTimestamp)
            batch.commit().await()
            gbSettings.setTimestamp(matchesTimestamp.matchesInsertion, MATCHES_INSERTION)
            gbSettings.setTimestamp(statsTimestamp.statsInsertion, MATCHES_STATS_INSERTION)
            gbSettings.setTimestamp(statsTimestamp.statsInsertion, PLAYERS_STATS_INSERTION)
            FirebaseInsertResult.StatsInserted
        }.getOrElse { t ->
            val message = when (t) {
                is FirebaseFirestoreException -> "FirebaseInsertion [${t.code}] ${t.message}"
                else -> t.message
            }
            FirebaseInsertResult.ErrorInsert(message)
        }
    }

    private fun getMatchDocRef(
        isStats: Boolean,
        matchId: FirebaseId
    ): DocumentReference {
        return firestore
            .collection(season)
            .document(if (isStats) FirebaseConstants.STATS else INFORMATION)
            .collection(FirebaseConstants.MATCHES)
            .document(matchId)
    }
}

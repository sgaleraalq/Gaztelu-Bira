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

package com.sgale.gaztelubira.core.network.firebase.implementation.insert

import com.google.firebase.firestore.FirebaseFirestore
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
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.INFORMATION
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.MATCHES
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.PLAYERS
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.STATS
import com.sgale.gaztelubira.core.domain.repository.firestore.IInsert.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.repository.firestore.IInsert.FirebaseInsertResult.StatsInserted
import com.sgale.gaztelubira.core.network.firebase.response.match.MatchMapper.asResponse
import com.sgale.gaztelubira.core.network.firebase.response.match.MatchStatsMapper.asResponse
import com.sgale.gaztelubira.core.network.firebase.response.stats.StatsMapper.asResponse
import javax.inject.Inject

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

        val matchesTimestamp = MatchesTimestamp()
        val statsTimestamp = StatsTimestamp()

        val result = batchInsert(
            success = StatsInserted,
            writes = {
                set(seasonCollection(INFORMATION, MATCHES).document(matchId), match.asResponse())
                set(
                    seasonCollection(STATS, MATCHES).document(matchId),
                    matchStats.copy(id = matchId).asResponse() // the stats document is keyed by the match
                )

                playerStats.forEach { (playerId, stats) ->
                    val playerDoc = seasonCollection(STATS, PLAYERS).document(playerId)
                    set(playerDoc, mapOf(ID to playerId), SetOptions.merge())
                    set(playerDoc.collection(MATCHES).document(matchId), stats.asResponse())
                }

                set(seasonDocument(INFORMATION), matchesTimestamp, SetOptions.merge())
                set(seasonDocument(STATS), statsTimestamp)
            }
        )

        if (result == StatsInserted) {
            setTimestamp(matchesTimestamp.matchesInsertion, MATCHES_INSERTION)
            setTimestamp(statsTimestamp.statsInsertion, MATCHES_STATS_INSERTION)
            setTimestamp(statsTimestamp.statsInsertion, PLAYERS_STATS_INSERTION)
        }
        return result
    }

    private companion object {
        const val ID = "id"
    }
}

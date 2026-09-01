/*
 * Designed and developed by 2025 sgaleraalq (Sergio Galera)
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

import android.util.Log
import com.sgale.gaztelubira.core.data.mappers.asMatchResponse
import com.sgale.gaztelubira.core.data.mappers.asMatchStatsResponse
import com.sgale.gaztelubira.core.data.mappers.asPlayerResponse
import com.sgale.gaztelubira.core.data.mappers.asStatsResponse
import com.sgale.gaztelubira.core.data.mappers.asTeamResponse
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.Stats
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.model.utils.MATCHES_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.MATCHES_STATS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.MatchesTimestamp
import com.sgale.gaztelubira.core.domain.model.utils.PLAYERS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.PLAYERS_STATS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.PlayerTimestamp
import com.sgale.gaztelubira.core.domain.model.utils.StatsTimestamp
import com.sgale.gaztelubira.core.domain.model.utils.TEAMS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.TeamTimestamp
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.INFORMATION
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.MATCHES
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.PLAYERS
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.STATS
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.TEAMS
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult.ErrorInsert
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult.PlayerInserted
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult.StatsInserted
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult.TeamInserted
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions.merge
import com.google.firebase.firestore.firestore
import javax.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume

class FbInsertDataImpl @Inject constructor(
    private val gbSettings: IGBPreferences
) : IGBInsertDataFb {
    private val firestore = Firebase.firestore
    private var season = gbSettings.getSeason()

    override suspend fun insertNewPlayer(player: PlayerModel): FirebaseInsertResult {
        return suspendCancellableCoroutine { continuation ->
            val timestamp = PlayerTimestamp()
            firestore.collection(season)
                .document(INFORMATION)
                .set(timestamp, merge())
            firestore.collection(season)
                .document(INFORMATION)
                .collection(PLAYERS)
                .document(player.id)
                .set(player.asPlayerResponse())
                .addOnSuccessListener {
                    gbSettings.setTimestamp(timestamp.playersInsertion, PLAYERS_INSERTION)
                    continuation.resume(PlayerInserted)
                }.addOnFailureListener { error ->
                    Log.e("GBFirebase", "Error inserting player ${error.message}")
                    continuation.resume(ErrorInsert(error.message))
                }
        }
    }

    override suspend fun insertNewTeam(
        team: TeamModel
    ): FirebaseInsertResult {
        return suspendCancellableCoroutine { continuation ->
            val timestamp = TeamTimestamp()
            firestore.collection(season)
                .document(INFORMATION)
                .set(timestamp, merge())
            firestore.collection(season)
                .document(INFORMATION)
                .collection(TEAMS)
                .document(team.id)
                .set(team.asTeamResponse())
                .addOnSuccessListener {
                    gbSettings.setTimestamp(timestamp.teamsInsertion, TEAMS_INSERTION)
                    continuation.resume(TeamInserted)
                }.addOnFailureListener { error ->
                    Log.e("GBFirebase", "Error inserting team ${error.message}")
                    continuation.resume(ErrorInsert(error.message))
                }
        }
    }

    override suspend fun insertStats(
        match: MatchModel,
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
            val statsStampRef = firestore.collection(season).document(STATS)

            val matchDocRef = getMatchDocRef(false, matchId)
            val matchStatsDocRef = getMatchDocRef(true, matchId)

            batch.set(matchDocRef, match.copy(id = matchId).asMatchResponse())
            batch.set(matchStatsDocRef, matchStats.copy(id = matchId).asMatchStatsResponse())

            playerStats.forEach { (id, stats) ->
                val playerDocRef = firestore
                    .collection(season)
                    .document(STATS)
                    .collection(PLAYERS)
                    .document(id)

                val playerStatsDocRef = playerDocRef
                    .collection(MATCHES)
                    .document(matchId)

                batch.set(playerDocRef, mapOf("id" to id), merge())
                batch.set(playerStatsDocRef, stats.asStatsResponse())
            }

            batch.set(matchStampRef, matchesTimestamp, merge())
            batch.set(statsStampRef, statsTimestamp)
            batch.commit().await()
            gbSettings.setTimestamp(matchesTimestamp.matchesInsertion, MATCHES_INSERTION)
            gbSettings.setTimestamp(statsTimestamp.statsInsertion, MATCHES_STATS_INSERTION)
            gbSettings.setTimestamp(statsTimestamp.statsInsertion, PLAYERS_STATS_INSERTION)
            StatsInserted
        }.getOrElse { t ->
            val message = when (t) {
                is FirebaseFirestoreException -> "FirebaseInsertion [${t.code}] ${t.message}"
                else -> t.message
            }
            ErrorInsert(message)
        }
    }

    private fun getMatchDocRef(
        isStats: Boolean,
        matchId: FirebaseId
    ): DocumentReference {
        return firestore
            .collection(season)
            .document(if (isStats) STATS else INFORMATION)
            .collection(MATCHES)
            .document(matchId)
    }
}
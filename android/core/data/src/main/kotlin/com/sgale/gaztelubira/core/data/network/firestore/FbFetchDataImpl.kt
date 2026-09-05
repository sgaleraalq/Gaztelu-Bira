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

import android.util.Log
import com.sgale.gaztelubira.core.data.db.implementations.AbstractGBDb
import com.sgale.gaztelubira.core.data.mappers.asMatchModel
import com.sgale.gaztelubira.core.data.mappers.asMatchStatsModel
import com.sgale.gaztelubira.core.data.mappers.asPlayerModel
import com.sgale.gaztelubira.core.data.mappers.asTeamModel
import com.sgale.gaztelubira.core.data.network.response.MatchResponse
import com.sgale.gaztelubira.core.data.network.response.MatchStatsResponse
import com.sgale.gaztelubira.core.data.network.response.PlayerResponse
import com.sgale.gaztelubira.core.data.network.response.PlayerStatsResponse
import com.sgale.gaztelubira.core.data.network.response.TeamResponse
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.player.Stats
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.model.utils.ErrorPlayer
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseTimestamp
import com.sgale.gaztelubira.core.domain.model.utils.MATCHES_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.PLAYERS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.STATS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.TEAMS_INSERTION
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.INFORMATION
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.MATCHES
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.PLAYERS
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.STATS
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.TEAMS
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBFetchDataFb
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class FbFetchDataImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val abstractDb: AbstractGBDb,
    private val gbSettings: IGBPreferences
) : IGBFetchDataFb {

    private var season = gbSettings.getSeason()

    override fun getSeason(): String? {
        return try {
            val season = "2024_2025" // TODO
            this.season = season
            season
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun fetchMatches(): List<MatchModel> {
        return try {
            val teamsMap = abstractDb.getTeamsMap()
            getTimestampAndSet(INFORMATION, MATCHES_INSERTION)
            firestore.collection(season)
                .document(INFORMATION)
                .collection(MATCHES)
                .get()
                .await()
                .toObjects(MatchResponse::class.java)
                .asMatchModel(teamsMap)
        } catch (e: Exception) {
            Log.e("GBFirebase", "Couldn't get data, error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun fetchMatchesStats(): List<MatchStatsModel> {
        return try {
            val matchesMap = abstractDb.getMatchesMap()
            val playersMap = abstractDb.getPlayersMap()

            getTimestampAndSet(STATS, STATS_INSERTION)
            firestore.collection(season)
                .document(STATS)
                .collection(MATCHES)
                .get()
                .await()
                .toObjects(MatchStatsResponse::class.java)
                .map { it.asMatchStatsModel(matchesMap, playersMap) }
        } catch (e: Exception) {
            Log.e("GBFirebase", "Couldn't get data, error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun fetchPlayers(): List<PlayerModel> {
        return try {
            getTimestampAndSet(INFORMATION, PLAYERS_INSERTION)
            firestore.collection(season)
                .document(INFORMATION)
                .collection(PLAYERS)
                .get()
                .await()
                .toObjects(PlayerResponse::class.java)
                .asPlayerModel()
        } catch (e: Exception) {
            Log.e("GBFirebase", "Couldn't get data, error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun fetchPlayersStats(): List<PlayerStatsModel> {
        return try {
            getTimestampAndSet(STATS, STATS_INSERTION)
            val playersMap = abstractDb.getPlayersMap()
            val seasonRef = firestore
                .collection(season)
                .document(STATS)
                .collection(PLAYERS)

            val playersSnapshot = seasonRef.get().await()

            playersSnapshot.documents.mapNotNull { player ->
                println("sgalera ${playersMap[player.id]?.name}")
                val playerId = player.id
                val matchesSnapshot = player.reference
                    .collection(MATCHES)
                    .get()
                    .await()

                val statsByMatch: Map<String, Stats> = matchesSnapshot
                    .documents
                    .mapNotNull { match ->
                        val stats = match.toObject(PlayerStatsResponse::class.java)?.asStats()
                        if (stats != null) match.id to stats else null
                    }.toMap()

                PlayerStatsModel(
                    id = playerId,
                    player = playersMap[playerId] ?: ErrorPlayer,
                    stats = statsByMatch,
                    percentage = 0.0
                )
            }
        } catch (e: Exception) {
            Log.e("GBFirebase", "Couldn't get data, error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun fetchTeams(): List<TeamModel> {
        return try {
            getTimestampAndSet(INFORMATION, TEAMS_INSERTION)
            firestore.collection(season)
                .document(INFORMATION)
                .collection(TEAMS)
                .get()
                .await()
                .toObjects(TeamResponse::class.java)
                .asTeamModel()
        } catch (e: Exception) {
            Log.e("GBFirebase", "Couldn't get data, error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getTeam(id: FirebaseId): TeamModel? {
        return try {
            firestore.collection(season)
                .document(INFORMATION)
                .collection(TEAMS)
                .document(id)
                .get()
                .await()
                .toObject(TeamResponse::class.java)
                ?.asTeamModel()
        } catch (e: Exception) {
            Log.e("GBFirebase", "Couldn't get data, error: ${e.message}")
            null
        }
    }

    override suspend fun getTimestamp(docName: String, timestampName: String): FirebaseTimestamp {
        return try {
            val snapshot = firestore.collection(season).document(docName).get().await()
            snapshot.getLong(timestampName) ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    private fun getTimestampAndSet(
        docName: String,
        timestampName: String
    ) = runBlocking {
        gbSettings.setTimestamp(getTimestamp(docName, timestampName), timestampName)
    }
}

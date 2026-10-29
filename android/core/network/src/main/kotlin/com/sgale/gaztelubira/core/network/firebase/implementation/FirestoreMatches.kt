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

package com.sgale.gaztelubira.core.network.firebase.implementation

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.match.MatchId
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchEvent
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchPlayer
import com.sgale.gaztelubira.core.domain.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.repository.MatchRemote
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.EVENTS
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.LINEUP
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.MATCHES
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.SEASONS
import com.sgale.gaztelubira.core.network.firebase.mapDocuments
import com.sgale.gaztelubira.core.network.firebase.mapper.MatchEventMapper.asModel
import com.sgale.gaztelubira.core.network.firebase.mapper.MatchMapper.asModel
import com.sgale.gaztelubira.core.network.firebase.mapper.MatchMapper.asResponse
import com.sgale.gaztelubira.core.network.firebase.mapper.MatchPlayerMapper.asModel
import com.sgale.gaztelubira.core.network.firebase.mapper.MatchPlayerMapper.asResponse
import com.sgale.gaztelubira.core.network.firebase.response.MatchEventResponse
import com.sgale.gaztelubira.core.network.firebase.response.MatchPlayerResponse
import com.sgale.gaztelubira.core.network.firebase.response.MatchResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirestoreMatches @Inject constructor(
    private val firestore: FirebaseFirestore
) : MatchRemote {
    override suspend fun fetchMatches(seasonId: SeasonId): List<Match> =
        matches(seasonId)
            .get()
            .await()
            .mapDocuments { match ->
                match.toObject(MatchResponse::class.java)
                    ?.asModel(MatchId(match.id), seasonId)
            }

    override suspend fun fetchMatchEvents(seasonId: SeasonId, matchId: MatchId): List<MatchEvent> =
        matches(seasonId)
            .document(matchId.value)
            .collection(EVENTS)
            .get()
            .await()
            .mapDocuments { event ->
                event.toObject(MatchEventResponse::class.java)?.asModel(matchId, seasonId)
            }

    override suspend fun fetchMatchLineup(seasonId: SeasonId, matchId: MatchId): List<MatchPlayer> =
        matches(seasonId)
            .document(matchId.value)
            .collection(LINEUP)
            .get()
            .await()
            .mapDocuments { player ->
                player.toObject(MatchPlayerResponse::class.java)?.asModel(matchId, seasonId)
            }

    override suspend fun matchExists(seasonId: SeasonId, matchId: MatchId): Boolean =
        matches(seasonId)
            .document(matchId.value)
            .get()
            .await()
            .exists()

    override suspend fun insertMatch(match: Match, players: List<MatchPlayer>) {
        val document = matches(match.seasonId).document(match.id.value)
        val batch = firestore.batch().set(document, match.asResponse())

        players.forEach { player ->
            batch.set(document.collection(LINEUP).document(player.lineup.playerId.value), player.asResponse())
        }

        batch.commit().await()
    }

    private fun matches(season: SeasonId): CollectionReference =
        firestore
            .collection(SEASONS)
            .document(season.value)
            .collection(MATCHES)
}

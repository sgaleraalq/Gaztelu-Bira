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

package com.sgale.gaztelubira.core.network.migration.firebase.implementation

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.match.Match
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchEvent
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchLineup
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.migration.repository.MatchRemote
import com.sgale.gaztelubira.core.network.migration.firebase.FirebaseConstants.EVENTS
import com.sgale.gaztelubira.core.network.migration.firebase.FirebaseConstants.LINEUP
import com.sgale.gaztelubira.core.network.migration.firebase.FirebaseConstants.MATCHES
import com.sgale.gaztelubira.core.network.migration.firebase.FirebaseConstants.SEASONS
import com.sgale.gaztelubira.core.network.migration.firebase.mapper.MatchEventMapper.asModel
import com.sgale.gaztelubira.core.network.migration.firebase.mapper.MatchMapper.asModel
import com.sgale.gaztelubira.core.network.migration.firebase.mapper.MatchPlayerMapper.asModel
import com.sgale.gaztelubira.core.network.migration.firebase.response.MatchEventResponse
import com.sgale.gaztelubira.core.network.migration.firebase.response.MatchPlayerResponse
import com.sgale.gaztelubira.core.network.migration.firebase.response.MatchResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirestoreMatches @Inject constructor(
    private val firestore: FirebaseFirestore
) : MatchRemote {
    override suspend fun fetchMatches(seasonId: SeasonId): List<Match> =
        matches(seasonId)
            .get()
            .await()
            .documents
            .mapNotNull { match ->
                match.toObject(MatchResponse::class.java)
                    ?.asModel(MatchId(match.id), seasonId)
            }

    override suspend fun fetchMatchEvents(seasonId: SeasonId, matchId: MatchId): List<MatchEvent> =
        matches(seasonId)
            .document(matchId.value)
            .collection(EVENTS)
            .get()
            .await()
            .documents
            .mapNotNull { event ->
                event.toObject(MatchEventResponse::class.java)?.asModel()
            }

    override suspend fun fetchMatchPlayers(seasonId: SeasonId, matchId: MatchId): List<MatchLineup> =
        matches(seasonId)
            .document(matchId.value)
            .collection(LINEUP)
            .get()
            .await()
            .documents
            .mapNotNull { player ->
                player.toObject(MatchPlayerResponse::class.java)?.asModel()
            }

    private fun matches(season: SeasonId): CollectionReference =
        firestore
            .collection(SEASONS)
            .document(season.value)
            .collection(MATCHES)
}

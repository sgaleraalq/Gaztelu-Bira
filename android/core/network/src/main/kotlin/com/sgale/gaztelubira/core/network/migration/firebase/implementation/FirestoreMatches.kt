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
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.migration.repository.MatchRemote
import com.sgale.gaztelubira.core.network.migration.firebase.FirebaseConstants.MATCHES
import com.sgale.gaztelubira.core.network.migration.firebase.FirebaseConstants.SEASONS
import com.sgale.gaztelubira.core.network.migration.firebase.mapper.MatchMapper.asModel
import com.sgale.gaztelubira.core.network.migration.firebase.response.MatchResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirestoreMatches @Inject constructor(
    private val firestore: FirebaseFirestore
): MatchRemote {
    override suspend fun fetchMatches(seasonId: SeasonId): List<Match> =
        matches(seasonId)
            .get()
            .await()
            .documents
            .mapNotNull { match ->
                match.toObject(MatchResponse::class.java)
                    ?.asModel(MatchId(match.id), seasonId)
            }

    private fun matches(season: SeasonId): CollectionReference =
        firestore
            .collection(SEASONS)
            .document(season.value)
            .collection(MATCHES)
}

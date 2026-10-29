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
import com.sgale.gaztelubira.core.domain.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.model.season.Season
import com.sgale.gaztelubira.core.domain.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.model.season.squad.SeasonPlayer
import com.sgale.gaztelubira.core.domain.repository.SeasonRemote
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.SEASONS
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.SQUAD
import com.sgale.gaztelubira.core.network.firebase.mapDocuments
import com.sgale.gaztelubira.core.network.firebase.mapper.SeasonMapper.asModel
import com.sgale.gaztelubira.core.network.firebase.mapper.SeasonPlayerMapper.asModel
import com.sgale.gaztelubira.core.network.firebase.response.SeasonPlayerResponse
import com.sgale.gaztelubira.core.network.firebase.response.SeasonResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirestoreSeason @Inject constructor(
    private val firestore: FirebaseFirestore
): SeasonRemote {
    override suspend fun fetchSeasons(): List<Season> =
        seasons()
            .get()
            .await()
            .mapDocuments { season ->
                season.toObject(SeasonResponse::class.java)
                    ?.asModel(SeasonId(season.id))
            }

    override suspend fun fetchSquad(season: SeasonId): List<SeasonPlayer> =
        seasons()
            .document(season.value)
            .collection(SQUAD)
            .get()
            .await()
            .mapDocuments { player ->
                player.toObject(SeasonPlayerResponse::class.java)
                    ?.asModel(season, PlayerId(player.id))
            }

    private fun seasons(): CollectionReference = firestore.collection(SEASONS)
}

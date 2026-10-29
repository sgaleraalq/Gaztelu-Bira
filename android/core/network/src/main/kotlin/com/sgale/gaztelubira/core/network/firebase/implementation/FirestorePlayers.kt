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
import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.model.season.squad.SeasonPlayer
import com.sgale.gaztelubira.core.domain.repository.PlayerRemote
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.CHANGED_AT
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.PLAYERS
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.SEASONS
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.SQUAD
import com.sgale.gaztelubira.core.network.firebase.mapDocuments
import com.sgale.gaztelubira.core.network.firebase.mapper.PlayerMapper.asModel
import com.sgale.gaztelubira.core.network.firebase.mapper.PlayerMapper.asResponse
import com.sgale.gaztelubira.core.network.firebase.mapper.SeasonPlayerMapper.asResponse
import com.sgale.gaztelubira.core.network.firebase.playersStamp
import com.sgale.gaztelubira.core.network.firebase.response.PlayerResponse
import com.sgale.gaztelubira.core.network.firebase.seasonStamp
import com.sgale.gaztelubira.core.network.firebase.stampChange
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirestorePlayers @Inject constructor(
    private val firestore: FirebaseFirestore
) : PlayerRemote {

    override suspend fun fetchPlayer(id: PlayerId): Player? =
        players()
            .document(id.value)
            .get()
            .await()
            .toObject(PlayerResponse::class.java)
            ?.asModel(id)

    override suspend fun fetchPlayers(): List<Player> =
        players()
            .get()
            .await()
            .mapDocuments { document ->
                document.toObject(PlayerResponse::class.java)
                    ?.asModel(PlayerId(document.id))
            }

    override suspend fun fetchPlayersChangedAt(): Long? =
        firestore.playersStamp()
            .get()
            .await()
            .getTimestamp(CHANGED_AT)
            ?.toDate()
            ?.time

    override suspend fun playerExists(id: PlayerId): Boolean =
        players()
            .document(id.value)
            .get()
            .await()
            .exists()

    override suspend fun insertPlayer(player: Player, seasonPlayer: SeasonPlayer) {
        val squadEntry = firestore
            .collection(SEASONS)
            .document(seasonPlayer.seasonId.value)
            .collection(SQUAD)
            .document(seasonPlayer.id.value)

        firestore.batch()
            .set(players().document(player.id.value), player.asResponse())
            .set(squadEntry, seasonPlayer.asResponse())
            .stampChange(firestore.playersStamp())
            .stampChange(firestore.seasonStamp(seasonPlayer.seasonId))
            .commit()
            .await()
    }

    private fun players(): CollectionReference =
        firestore.collection(PLAYERS)
}

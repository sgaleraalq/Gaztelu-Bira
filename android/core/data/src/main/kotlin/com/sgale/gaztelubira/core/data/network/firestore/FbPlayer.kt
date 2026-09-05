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
import com.sgale.gaztelubira.core.data.mappers.PlayerMapper
import com.sgale.gaztelubira.core.data.mappers.asPlayerModel
import com.sgale.gaztelubira.core.data.network.response.PlayerResponse
import com.sgale.gaztelubira.core.data.network.response.PlayerStatsResponse
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.utils.ErrorPlayer
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.BODY
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.FACE
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.INFORMATION
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.MATCHES
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.PLAYERS
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.STATS
import com.sgale.gaztelubira.core.domain.repository.firestore.IFbPlayers
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class FbPlayer @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val abstractDb: AbstractGBDb,
    private val gbSettings: IGBPreferences
) : IFbPlayers {
    private val storage: FirebaseStorage = Firebase.storage

    override suspend fun fetchPlayerInformation(playerId: FirebaseId): PlayerModel? {
        return try {
            firestore.collection(gbSettings.getSeason())
                .document(INFORMATION)
                .collection(PLAYERS)
                .document(playerId)
                .get()
                .await()
                .toObject(PlayerResponse::class.java)
                ?.asPlayerModel()
        } catch (e: Exception) {
            Log.e("GBFirebase", "Couldn't get data, error: ${e.message}")
            null
        }
    }

    override suspend fun fetchPlayerStats(playerId: String): PlayerStatsModel? {
        return try {
            val playersMap = abstractDb.getPlayersMap()

            val matches = firestore
                .collection(gbSettings.getSeason())
                .document(STATS)
                .collection(PLAYERS)
                .document(playerId)
                .collection(MATCHES)
                .get()
                .await()

            val statsByMatch = matches.documents.mapNotNull { match ->
                val stats = match.toObject(PlayerStatsResponse::class.java)?.asStats()
                if (stats != null) match.id to stats else null
            }.toMap()

            PlayerStatsModel(
                id = playerId,
                player = playersMap[playerId] ?: ErrorPlayer,
                stats = statsByMatch,
                percentage = 0.0
            )
        } catch (e: Exception) {
            Log.e("GBFirebase", "Couldn't get data, error: ${e.message}")
            null
        }
    }

    override suspend fun insertNewPlayer(player: PlayerModel): Boolean {
        val playerResponse = PlayerMapper.asResponse(player)
        return try {
            firestore
                .collection(gbSettings.getSeason())
                .document(PLAYERS)
                .collection(INFORMATION)
                .document(playerResponse.id)
                .set(playerResponse)
                .await()
            true
        } catch (e: Exception) {
            Log.e("Firebase", "Error adding document", e)
            false
        }
    }

    override suspend fun insertPlayerImage(
        playerId: FirebaseId,
        image: ByteArray?,
        isFace: Boolean
    ): String {
        return if (image != null) {
            val reference = storage.reference
                .child("${gbSettings.getSeason()}/$PLAYERS/$playerId/${if (isFace) FACE else BODY}.jpg")

            reference.putBytes(image).await()
            reference.downloadUrl.await().toString()
        } else {
            ""
        }
    }
}
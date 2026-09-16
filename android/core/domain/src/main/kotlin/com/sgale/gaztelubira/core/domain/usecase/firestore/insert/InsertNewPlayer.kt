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

package com.sgale.gaztelubira.core.domain.usecase.firestore.insert

import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.repository.db.IGBPlayersDb
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.BODY
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.FACE
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.PLAYERS
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBFireStorage
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBFireStorage.ImageInsertionResult.Success
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult
import javax.inject.Inject

class InsertNewPlayer @Inject constructor(
    private val firestore: IGBInsertDataFb,
    private val storage: IGBFireStorage,
    private val playersDb: IGBPlayersDb
) {
    suspend operator fun invoke(
        player: Player
    ): FirebaseInsertResult {
        val faceImgPath = "$PLAYERS/$FACE/${player.id}"
        val bodyImgPath = "$PLAYERS/$BODY/${player.id}"

        val faceImgInserted = if (!player.faceImage.isNullOrBlank()) {
            storage.insertImage(faceImgPath, player.faceImage)
        } else {
            Success(null)
        }

        val bodyImgInserted = if (!player.bodyImage.isNullOrBlank()) {
            storage.insertImage(bodyImgPath, player.bodyImage)
        } else {
            Success(null)
        }

        var result: FirebaseInsertResult?

        if (faceImgInserted is Success && bodyImgInserted is Success) {
            val player = player.copy(
                faceImage = faceImgInserted.url,
                bodyImage = bodyImgInserted.url
            )

            playersDb.insertPlayer(player)
            result = firestore.insertNewPlayer(player)
        } else {
            result = FirebaseInsertResult.ErrorInsert("")
        }

        return result
    }
}

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

package com.sgale.gaztelubira.core.domain.usecase

import android.net.Uri
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.AlreadyExists
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.NoSeasonSelected
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.Unknown
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Inserted.Document
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Inserted.Image
import com.sgale.gaztelubira.core.domain.model.other.PictureType.BODY
import com.sgale.gaztelubira.core.domain.model.other.PictureType.FACE
import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.model.player.Position
import com.sgale.gaztelubira.core.domain.model.season.squad.SeasonPlayer
import com.sgale.gaztelubira.core.domain.repository.PlayerLocal
import com.sgale.gaztelubira.core.domain.repository.PlayerRemote
import com.sgale.gaztelubira.core.domain.repository.SeasonLocal
import com.sgale.gaztelubira.core.domain.repository.StorageRemote
import com.sgale.gaztelubira.core.domain.utils.toFirebaseId
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Creates a brand-new player and adds them to the selected season's squad.
 */
class InsertPlayer @Inject constructor(
    private val storage: StorageRemote,
    private val selectedSeason: SelectedSeason,
    private val playerLocal: PlayerLocal,
    private val playerRemote: PlayerRemote,
    private val seasonLocal: SeasonLocal
) {
    /**
     * @param faceImage Optional, null when the player has no face picture yet.
     * @param bodyImage Optional, null when the player has no body picture yet.
     */
    suspend operator fun invoke(
        name: String,
        nickname: String?,
        dorsal: Int,
        position: Position,
        faceImage: Uri?,
        bodyImage: Uri?
    ): FirebaseInsertResult {
        val season = selectedSeason().first() ?: return NoSeasonSelected
        val cleanName = name.trim()
        val id = PlayerId(cleanName.toFirebaseId())

        return try {
            if (playerRemote.playerExists(id)) return AlreadyExists

            val faceUrl = faceImage?.let { uri ->
                when (val upload = storage.insertPlayerImage(id, FACE, uri)) {
                    is Image -> upload.uri.toString()
                    else -> return upload
                }
            }
            val bodyUrl = bodyImage?.let { uri ->
                when (val upload = storage.insertPlayerImage(id, BODY, uri)) {
                    is Image -> upload.uri.toString()
                    else -> return upload
                }
            }

            val player = Player(
                id = id,
                name = cleanName,
                nickname = nickname?.trim()?.ifBlank { null },
                faceImage = faceUrl,
                bodyImage = bodyUrl
            )
            val seasonPlayer = SeasonPlayer(
                id = id,
                seasonId = season,
                dorsal = dorsal,
                position = position
            )

            playerRemote.insertPlayer(player, seasonPlayer)
            playerLocal.insertPlayer(player)
            seasonLocal.insertSeasonPlayers(listOf(seasonPlayer))
            Document
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Unknown(e.message.orEmpty())
        }
    }
}

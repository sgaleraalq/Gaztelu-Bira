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

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.Unknown
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Inserted.Image
import com.sgale.gaztelubira.core.domain.model.other.PictureType
import com.sgale.gaztelubira.core.domain.model.other.PictureType.BODY
import com.sgale.gaztelubira.core.domain.model.other.PictureType.FACE
import com.sgale.gaztelubira.core.domain.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.model.team.TeamId
import com.sgale.gaztelubira.core.domain.repository.StorageRemote
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.PLAYERS
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.TEAMS
import com.sgale.gaztelubira.core.network.firebase.image.ImageCompressor
import com.sgale.gaztelubira.core.network.firebase.image.ImageFormat
import com.sgale.gaztelubira.core.network.firebase.image.ImageFormat.JPEG
import com.sgale.gaztelubira.core.network.firebase.image.ImageFormat.PNG
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TEAM_LOGO_MAX_SIZE = 256
private const val PLAYER_FACE_MAX_SIZE = 256
private const val PLAYER_BODY_MAX_SIZE = 512

private const val FACE_FILE = "face"
private const val BODY_FILE = "body"

internal class FirebaseStorage @Inject constructor(
    private val fireStorage: FirebaseStorage,
    private val imageCompressor: ImageCompressor
) : StorageRemote {
    override suspend fun insertTeamLogo(id: TeamId, uri: Uri): FirebaseInsertResult =
        upload(path = "$TEAMS/$id", uri = uri, maxSize = TEAM_LOGO_MAX_SIZE, format = JPEG)

    /**
     * Faces go as JPEG on white, bodies as PNG so their transparent background is kept.
     */
    override suspend fun insertPlayerImage(
        id: PlayerId,
        type: PictureType,
        uri: Uri
    ): FirebaseInsertResult =
        when (type) {
            FACE -> upload("$PLAYERS/$id/$FACE_FILE", uri, PLAYER_FACE_MAX_SIZE, JPEG)
            BODY -> upload("$PLAYERS/$id/$BODY_FILE", uri, PLAYER_BODY_MAX_SIZE, PNG)
        }

    /**
     * @param path Storage path without extension, the format adds it.
     */
    private suspend fun upload(
        path: String,
        uri: Uri,
        maxSize: Int,
        format: ImageFormat
    ): FirebaseInsertResult =
        try {
            val image = imageCompressor.compress(uri, maxSize, format)
            val metadata = StorageMetadata.Builder()
                .setContentType(format.mimeType)
                .build()

            val ref = fireStorage.reference.child("$path.${format.extension}")
            ref.putBytes(image, metadata).await()
            val downloadUri = ref.downloadUrl.await()
            Image(downloadUri)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Unknown(e.message.orEmpty())
        }
}

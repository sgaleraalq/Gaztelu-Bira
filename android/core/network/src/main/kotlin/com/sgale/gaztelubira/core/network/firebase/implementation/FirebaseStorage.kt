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
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.Unknown
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Inserted.Image
import com.sgale.gaztelubira.core.domain.model.team.TeamId
import com.sgale.gaztelubira.core.domain.repository.StorageRemote
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.TEAMS
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirebaseStorage @Inject constructor(
    private val fireStorage: FirebaseStorage
) : StorageRemote {
    override suspend fun insertTeamLogo(id: TeamId, uri: Uri): FirebaseInsertResult =
        try {
            val ref = fireStorage.reference.child("$TEAMS/$id")
            ref.putFile(uri).await()
            val downloadUri = ref.downloadUrl.await()
            Image(downloadUri)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Unknown(e.message.orEmpty())
        }
}

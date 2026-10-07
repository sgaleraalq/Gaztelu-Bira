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

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.sgale.gaztelubira.core.domain.migration.repository.StorageRemote
import com.sgale.gaztelubira.core.domain.migration.util.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.migration.util.FirebaseInsertResult.Insertion.Image
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirebaseStorage @Inject constructor(
    private val fireStorage: FirebaseStorage
) : StorageRemote {
    override suspend fun insertImage(path: String, image: String): FirebaseInsertResult =
        try {
            val ref = fireStorage.reference.child(path)
            ref.putFile(image.toUri()).await()

            val downloadUri: Uri = ref.downloadUrl.await()
            Image(downloadUri)
        } catch (t: Throwable) {
            FirebaseInsertResult.Error(t.message.orEmpty())
        }

}

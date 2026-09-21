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

package com.sgale.gaztelubira.core.network.firebase.implementation.storage

import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.IGBFireStorage
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.IGBFireStorage.ImageInsertionResult
import jakarta.inject.Inject

internal class FirebaseStorage @Inject constructor(
    private val insertImage: InsertImage,
) : IGBFireStorage {
    override suspend fun insertImage(path: String, image: String): ImageInsertionResult =
        insertImage.invoke(path, image)
}

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

package com.sgale.gaztelubira.core.network.firebase.implementation.fetch

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import kotlinx.coroutines.tasks.await

internal abstract class Fetch(
    protected val firestore: FirebaseFirestore,
    protected val gbSettings: IGBPreferences
) {
    protected fun seasonDocument(
        document: String
    ): DocumentReference {
        val season = gbSettings.getSeason()
        require(season.isNotBlank()) { "Season is not set yet, cannot reach $document" }
        return firestore.collection(season).document(document)
    }

    protected fun seasonCollection(
        document: String,
        collection: String
    ): CollectionReference =
        seasonDocument(document).collection(collection)

    protected suspend fun <R : Any, M> fetchList(
        document: String,
        collection: String,
        type: Class<R>,
        asModel: (R) -> M
    ): List<M> = seasonCollection(document, collection)
        .get()
        .await()
        .toObjects(type)
        .map(asModel)
}

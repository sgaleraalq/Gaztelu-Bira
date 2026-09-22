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

package com.sgale.gaztelubira.core.network.firebase.implementation.insert

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.WriteBatch
import com.sgale.gaztelubira.core.common.utils.TAG
import com.sgale.gaztelubira.core.domain.legacy.model.utils.FirebaseCollection
import com.sgale.gaztelubira.core.domain.legacy.model.utils.FirebaseTimestamp
import com.sgale.gaztelubira.core.domain.legacy.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.IInsert.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.IInsert.FirebaseInsertResult.ErrorInsert
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

internal abstract class Insert(
    private val firestore: FirebaseFirestore,
    private val gbSettings: IGBPreferences
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

    // TODO players and teams are still written to the debug collection
    protected fun debugDocument(
        document: String
    ): DocumentReference =
        firestore.collection(DEBUG).document(document)

    /**
     * Writes everything or nothing: the entity and the timestamp that announces
     * it travel in the same batch, so no reader is told about data that failed
     * to land.
     */
    protected suspend fun batchInsert(
        success: FirebaseInsertResult,
        writes: WriteBatch.() -> Unit
    ): FirebaseInsertResult =
        try {
            firestore.batch().apply(writes).commit().await()
            success
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Couldn't insert data, error: ${e.insertError}")
            ErrorInsert(e.insertError)
        }

    /** Remembers what was just uploaded, so the next launch does not fetch it again. */
    protected fun setTimestamp(
        timestamp: FirebaseTimestamp,
        collection: FirebaseCollection
    ) = gbSettings.setTimestamp(timestamp, collection)

    private val Throwable.insertError: String?
        get() = when (this) {
            is FirebaseFirestoreException -> "FirebaseInsertion [$code] $message"
            else -> message
        }

    private companion object {
        const val DEBUG = "debug"
    }
}

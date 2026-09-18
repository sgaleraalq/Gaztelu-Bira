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

package com.sgale.gaztelubira.core.network.firebase.implementation.fetch

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.sgale.gaztelubira.core.common.utils.TAG
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseTimestamp
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FetchTimestamp @Inject constructor(
    firestore: FirebaseFirestore,
    gbSettings: IGBPreferences
) : Fetch(firestore, gbSettings) {
    suspend operator fun invoke(
        docName: String,
        timestampName: String
    ): FirebaseTimestamp {
        return try {
            val snapshot = firestore
                .collection(season)
                .document(docName)
                .get()
                .await()
            snapshot.getLong(timestampName) ?: 0L
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching firebaseTimestamp: ${e.message}")
            0L
        }
    }
}

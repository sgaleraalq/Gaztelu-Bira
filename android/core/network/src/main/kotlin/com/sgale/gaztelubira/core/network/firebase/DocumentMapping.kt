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

package com.sgale.gaztelubira.core.network.firebase

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.sgale.gaztelubira.core.common.utils.TAG

/**
 * Maps each document on its own: one that does not fit its response class, or carries a value the
 * model does not know, is logged and left out instead of failing the whole read — and with it the
 * sync that asked for it.
 *
 * Not inline on purpose: [map] cannot suspend, so the catch below never swallows a cancellation.
 */
internal fun <T : Any> QuerySnapshot.mapDocuments(map: (DocumentSnapshot) -> T?): List<T> =
    documents.mapNotNull { document ->
        try {
            map(document)
        } catch (e: Exception) {
            Log.w(TAG, "Skipped malformed document ${document.reference.path}", e)
            null
        }
    }

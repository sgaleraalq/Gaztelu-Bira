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

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import com.sgale.gaztelubira.core.domain.model.season.SeasonId
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.CHANGED_AT
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.META
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.PLAYERS
import com.sgale.gaztelubira.core.network.firebase.FirebaseConstants.SEASONS

/**
 * Devices only download a season, or the players, again when its `changedAt` moves, so every write
 * stamps what it touched, in the same batch. The upload scripts do the same (stamp.js).
 */
internal fun WriteBatch.stampChange(document: DocumentReference): WriteBatch =
    set(document, mapOf(CHANGED_AT to FieldValue.serverTimestamp()), SetOptions.merge())

internal fun FirebaseFirestore.seasonStamp(season: SeasonId): DocumentReference =
    collection(SEASONS).document(season.value)

internal fun FirebaseFirestore.playersStamp(): DocumentReference =
    collection(META).document(PLAYERS)

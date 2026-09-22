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


package com.sgale.gaztelubira.core.network.legacy.firebase.implementation.fetch

import com.google.firebase.firestore.FirebaseFirestore
import com.sgale.gaztelubira.core.domain.legacy.model.player.Player
import com.sgale.gaztelubira.core.domain.legacy.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.legacy.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.FirebaseConstants.INFORMATION
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.FirebaseConstants.PLAYERS
import com.sgale.gaztelubira.core.network.legacy.firebase.response.player.PlayerMapper.asModel
import com.sgale.gaztelubira.core.network.legacy.firebase.response.player.PlayerResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FetchPlayer @Inject constructor(
    firestore: FirebaseFirestore,
    gbSettings: IGBPreferences
) : Fetch(firestore, gbSettings) {
    suspend operator fun invoke(id: FirebaseId): Player? =
        seasonCollection(INFORMATION, PLAYERS)
            .document(id)
            .get()
            .await()
            .toObject(PlayerResponse::class.java)
            ?.asModel()
}

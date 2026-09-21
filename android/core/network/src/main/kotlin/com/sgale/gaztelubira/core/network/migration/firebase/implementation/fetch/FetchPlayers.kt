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

package com.sgale.gaztelubira.core.network.migration.firebase.implementation.fetch

import com.google.firebase.firestore.FirebaseFirestore
import com.sgale.gaztelubira.core.domain.migration.model.player.Player
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId
import com.sgale.gaztelubira.core.network.migration.firebase.FirebaseConstants
import com.sgale.gaztelubira.core.network.migration.firebase.response.player.PlayerMapper.asModel
import com.sgale.gaztelubira.core.network.migration.firebase.response.player.PlayerResponse
import kotlinx.coroutines.tasks.await

internal class FetchPlayers(
    private val firestore: FirebaseFirestore
) {
    suspend operator fun invoke(): List<Player> =
        firestore.collection(FirebaseConstants.PLAYERS)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(PlayerResponse::class.java)
                    ?.asModel(PlayerId(document.id))
            }
}

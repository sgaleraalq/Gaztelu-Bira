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

package com.sgale.gaztelubira.core.network.firebase.migrations.implementation.fetch

import com.google.firebase.firestore.FirebaseFirestore
import com.sgale.gaztelubira.core.domain.migration.model.player.Player
import com.sgale.gaztelubira.core.network.firebase.migrations.FirebaseConstants.PLAYERS
import com.sgale.gaztelubira.core.network.firebase.migrations.response.player.PlayerMapper.asModel
import com.sgale.gaztelubira.core.network.firebase.migrations.response.player.PlayerResponse
import kotlinx.coroutines.tasks.await

internal class FetchPlayers(
    private val firestore: FirebaseFirestore
) {
    suspend operator fun invoke(): List<Player> =
        firestore.collectionGroup(PLAYERS)
            .get()
            .await()
            .toObjects(PlayerResponse::class.java)
            .map { it.asModel() }
}

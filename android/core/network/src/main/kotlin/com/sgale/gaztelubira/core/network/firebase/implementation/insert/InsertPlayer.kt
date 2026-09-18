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

package com.sgale.gaztelubira.core.network.firebase.implementation.insert

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.model.utils.PLAYERS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.PlayerTimestamp
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.INFORMATION
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.PLAYERS
import com.sgale.gaztelubira.core.domain.repository.firestore.IInsert.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.repository.firestore.IInsert.FirebaseInsertResult.PlayerInserted
import com.sgale.gaztelubira.core.network.firebase.response.player.PlayerMapper.asResponse
import javax.inject.Inject

internal class InsertPlayer @Inject constructor(
    firestore: FirebaseFirestore,
    gbSettings: IGBPreferences
) : Insert(firestore, gbSettings) {
    suspend operator fun invoke(player: Player): FirebaseInsertResult {
        val timestamp = PlayerTimestamp()
        val information = debugDocument(INFORMATION)

        val result = batchInsert(
            success = PlayerInserted,
            writes = {
                set(information, timestamp, SetOptions.merge())
                set(information.collection(PLAYERS).document(player.id), player.asResponse())
            }
        )

        if (result == PlayerInserted) {
            setTimestamp(timestamp.playersInsertion, PLAYERS_INSERTION)
        }
        return result
    }
}

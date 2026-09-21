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

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sgale.gaztelubira.core.domain.model.team.Team
import com.sgale.gaztelubira.core.domain.model.utils.TEAMS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.TeamTimestamp
import com.sgale.gaztelubira.core.domain.legacy.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.FirebaseConstants.INFORMATION
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.FirebaseConstants.TEAMS
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.IInsert.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.IInsert.FirebaseInsertResult.TeamInserted
import com.sgale.gaztelubira.core.network.firebase.response.team.TeamMapper.asResponse
import javax.inject.Inject

internal class InsertTeam @Inject constructor(
    firestore: FirebaseFirestore,
    gbSettings: IGBPreferences
) : Insert(firestore, gbSettings) {
    suspend operator fun invoke(team: Team): FirebaseInsertResult {
        val timestamp = TeamTimestamp()
        val information = debugDocument(INFORMATION)

        val result = batchInsert(
            success = TeamInserted,
            writes = {
                set(information, timestamp, SetOptions.merge())
                set(information.collection(TEAMS).document(team.id), team.asResponse())
            }
        )

        if (result == TeamInserted) {
            setTimestamp(timestamp.teamsInsertion, TEAMS_INSERTION)
        }
        return result
    }
}

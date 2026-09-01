/*
 * Designed and developed by 2025 sgaleraalq (Sergio Galera)
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

package com.sgale.gaztelubira.core.domain.usecase.firestore.insert

import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.repository.db.IGBTeamsDb
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.TEAMS
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBFireStorage
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBFireStorage.ImageInsertionResult
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBFireStorage.ImageInsertionResult.Success
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.utils.CommonImage
import javax.inject.Inject

class InsertNewTeam @Inject constructor(
    private val firestore: IGBInsertDataFb,
    private val storage: IGBFireStorage,
    private val teamsDb: IGBTeamsDb
) {
    suspend operator fun invoke(
        img: CommonImage?,
        teamName: String,
        teamId: String,
        onFailure: () -> Unit
    ): FirebaseInsertResult {
        val imgPath = "$TEAMS/$teamId"

        val insertionResult: ImageInsertionResult = if (img != null) {
            storage.insertImage(imgPath, img)
        } else {
            Success(null)
        }

        var result: FirebaseInsertResult?

        if (insertionResult is Success) {
            val team = TeamModel(
                id = teamId,
                name = teamName,
                logo = insertionResult.url
            )

            teamsDb.insertTeam(team)
            result = firestore.insertNewTeam(team)
        } else {
            onFailure()
            result = FirebaseInsertResult.ErrorInsert("")
        }
        return result
    }
}

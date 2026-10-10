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

package com.sgale.gaztelubira.core.domain.usecase

import android.net.Uri
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.AlreadyExists
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.NoSeasonSelected
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.Unknown
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Inserted.Document
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Inserted.Image
import com.sgale.gaztelubira.core.domain.model.team.Team
import com.sgale.gaztelubira.core.domain.model.team.TeamId
import com.sgale.gaztelubira.core.domain.repository.StorageRemote
import com.sgale.gaztelubira.core.domain.repository.TeamLocal
import com.sgale.gaztelubira.core.domain.repository.TeamRemote
import com.sgale.gaztelubira.core.domain.utils.toFirebaseId
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class InsertTeam @Inject constructor(
    private val storage: StorageRemote,
    private val selectedSeason: SelectedSeason,
    private val teamLocal: TeamLocal,
    private val teamRemote: TeamRemote
) {
    suspend operator fun invoke(name: String, image: Uri): FirebaseInsertResult {
        val season = selectedSeason().first() ?: return NoSeasonSelected
        val cleanName = name.trim()
        val id = TeamId(cleanName.toFirebaseId())

        return try {
            if (teamRemote.teamExists(season, id)) return AlreadyExists

            val upload = storage.insertTeamLogo(id, image)
            if (upload !is Image) return upload

            val team = Team(
                id = id,
                seasonId = season,
                name = cleanName,
                logo = upload.uri.toString()
            )

            teamRemote.insertTeam(season, team)
            teamLocal.insertTeam(team)
            Document
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Unknown(e.message.orEmpty())
        }
    }
}

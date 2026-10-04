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

package com.sgale.gaztelubira.core.network.migration.firebase.implementation

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sgale.gaztelubira.core.domain.migration.model.TeamId
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.migration.model.team.Team
import com.sgale.gaztelubira.core.domain.migration.repository.TeamRemote
import com.sgale.gaztelubira.core.network.migration.firebase.FirebaseConstants.SEASONS
import com.sgale.gaztelubira.core.network.migration.firebase.FirebaseConstants.TEAMS
import com.sgale.gaztelubira.core.network.migration.firebase.mapper.TeamMapper.asModel
import com.sgale.gaztelubira.core.network.migration.firebase.response.TeamResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirestoreTeam @Inject constructor(
    private val firestore: FirebaseFirestore
): TeamRemote {
    override suspend fun fetchTeams(seasonId: SeasonId): List<Team> =
        teams(seasonId)
            .get()
            .await()
            .documents
            .mapNotNull { team ->
                team.toObject(TeamResponse::class.java)
                    ?.asModel(TeamId(team.id))
            }

    private fun teams(season: SeasonId): CollectionReference =
        firestore
            .collection(SEASONS)
            .document(season.value)
            .collection(TEAMS)
}

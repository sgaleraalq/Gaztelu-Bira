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

package com.sgale.gaztelubira.core.data.db.implementations

import com.sgale.gaztelubira.core.data.db.GBDatabase
import com.sgale.gaztelubira.core.data.mappers.TeamMapper
import com.sgale.gaztelubira.core.data.mappers.asTeamEntity
import com.sgale.gaztelubira.core.data.mappers.asTeamModel
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.repository.db.IGBTeamsDb
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GBTeamsDb @Inject constructor(
    db: GBDatabase
): AbstractGBDb(db), IGBTeamsDb {
    private val teamsDao = db.getTeamsDao()

    override suspend fun deleteTeam(id: FirebaseId) {
        teamsDao.deleteItem(id)
    }
    override suspend fun insertTeam(team: TeamModel) =
        teamsDao.insert(team.asTeamEntity())

    override suspend fun insertTeams(teams: List<TeamModel>) =
        insertList(
            items = teams,
            mapper = TeamMapper::asEntity,
            dao = teamsDao
        )

    override suspend fun getTeam(
        id: FirebaseId
    ): TeamModel? = teamsDao.getItem(id)?.asTeamModel()

    override fun getTeamsList(): Flow<List<TeamModel>> =
        getFlow(
            source = teamsDao.getListAsFlow(),
            mapper = { it.asTeamModel() },
            keySelector = { it.name }
        )
}

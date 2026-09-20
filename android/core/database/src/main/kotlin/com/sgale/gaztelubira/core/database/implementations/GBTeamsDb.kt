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

package com.sgale.gaztelubira.core.database.implementations

import com.sgale.gaztelubira.core.database.GBDatabase
import com.sgale.gaztelubira.core.database.entities.team.TeamMapper.asEntity
import com.sgale.gaztelubira.core.database.entities.team.TeamMapper.asModel
import com.sgale.gaztelubira.core.domain.model.team.Team
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.repository.db.IGBTeamsDb
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GBTeamsDb @Inject constructor(
    db: GBDatabase
): AbstractGBDb(db), IGBTeamsDb {
    private val teamsDao = db.getTeamsDao()

    override suspend fun deleteTeam(id: FirebaseId) {
        teamsDao.deleteItem(id)
    }
    override suspend fun insertTeam(team: Team) =
        teamsDao.insert(team.asEntity())

    override suspend fun insertTeams(teams: List<Team>) =
        insertList(
            items = teams,
            mapper = { it.asEntity() },
            dao = teamsDao
        )

    override suspend fun getTeam(
        id: FirebaseId
    ): Team? = teamsDao.getItem(id)?.asModel()

    override fun getTeamsList(): Flow<List<Team>> =
        getFlow(
            source = teamsDao.getListAsFlow(),
            mapper = { it.asModel() },
            keySelector = { it.name }
        )
}

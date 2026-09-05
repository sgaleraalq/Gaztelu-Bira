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

package com.sgale.gaztelubira.core.data.db.implementations

import com.sgale.gaztelubira.core.data.db.GBDatabase
import com.sgale.gaztelubira.core.data.mappers.MatchMapper
import com.sgale.gaztelubira.core.data.mappers.asMatchEntity
import com.sgale.gaztelubira.core.data.mappers.asMatchModel
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.repository.db.IGBMatchesDb
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GBMatchesDb @Inject constructor(
    db: GBDatabase
) : AbstractGBDb(db), IGBMatchesDb {
    private val matchesDao = db.getMatchesDao()

    override suspend fun deleteMatch(id: FirebaseId) {
        matchesDao.deleteItem(id)
    }

    override suspend fun fetchMatches(): List<MatchModel> =
        matchesDao.getMatches().map { it.asMatchModel(getTeamsMap()) }

    override suspend fun insertMatch(match: MatchModel) =
        matchesDao.insert(match.asMatchEntity())

    override suspend fun insertMatches(matches: List<MatchModel>) =
        insertList(
            items = matches,
            mapper = MatchMapper::asEntity,
            dao = matchesDao
        )

    override fun getMatchesListAsFlow(): Flow<List<MatchModel>> =
        getFlow(
            source = matchesDao.getListAsFlow(),
            mapper = { it.asMatchModel(getTeamsMap()) },
            keySelector = { it.date }
        )

    override suspend fun getNumberOfJourneys(): Int =
        matchesDao.getNumberOfJourneys().size
}

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
import com.sgale.gaztelubira.core.database.entities.match.MatchMapper.asEntity
import com.sgale.gaztelubira.core.database.entities.match.MatchMapper.asModel
import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.repository.db.IGBMatchesDb
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GBMatchesDb @Inject constructor(
    db: GBDatabase
) : AbstractGBDb(db), IGBMatchesDb {
    private val matchesDao = db.getMatchesDao()

    override suspend fun deleteMatch(id: FirebaseId) {
        matchesDao.deleteItem(id)
    }

    override suspend fun fetchMatches(): List<Match> =
        matchesDao.getMatches().map { it.asModel() }

    override suspend fun insertMatch(match: Match) =
        matchesDao.insert(match.asEntity())

    override suspend fun insertMatches(
        matches: List<Match>
    ) = insertList(
        items = matches,
        mapper = { it.asEntity() },
        dao = matchesDao
    )

    override fun getMatchesListAsFlow(): Flow<List<Match>> =
        getFlow(
            source = matchesDao.getListAsFlow(),
            mapper = { it.asModel() },
            keySelector = { it.date }
        )

    override suspend fun getNumberOfJourneys(): Int =
        matchesDao.getNumberOfJourneys().size
}

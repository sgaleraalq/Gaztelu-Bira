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
import com.sgale.gaztelubira.core.data.mappers.MatchStatsMapper
import com.sgale.gaztelubira.core.data.mappers.asMatchStatsEntity
import com.sgale.gaztelubira.core.data.mappers.asMatchStatsModel
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.repository.db.IGBMatchesStatsDb
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GBMatchesStatsDb @Inject constructor(
    db: GBDatabase
) : AbstractGBDb(db), IGBMatchesStatsDb {
    private val matchesStatsDao = db.getMatchesStatsDao()

    override suspend fun deleteMatch(id: FirebaseId) {
        matchesStatsDao.deleteItem(id)
    }

    override suspend fun getMatchStats(id: FirebaseId) =
        matchesStatsDao.getItem(id)?.asMatchStatsModel(
            playersMap = getPlayersMap(),
            matchesMap = getMatchesMap()
        )

    override suspend fun insertMatch(match: MatchStatsModel) {
        matchesStatsDao.insert(match.asMatchStatsEntity())
    }

    override suspend fun insertMatchesStatsFromFB(matches: List<MatchStatsModel>) {
        insertList(
            items = matches,
            mapper = MatchStatsMapper::asEntity,
            dao = matchesStatsDao
        )
    }

    override fun getMatchesStatsListAsFlow(): Flow<List<MatchStatsModel>> =
        getFlow(
            source = matchesStatsDao.getListAsFlow(),
            mapper = { it.asMatchStatsModel(getMatchesMap(), getPlayersMap()) },
            keySelector = { it.id }
        )
}
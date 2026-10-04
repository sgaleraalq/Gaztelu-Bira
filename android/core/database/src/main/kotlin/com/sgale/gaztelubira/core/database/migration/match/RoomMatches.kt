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

package com.sgale.gaztelubira.core.database.migration.match

import com.sgale.gaztelubira.core.database.migration.match.entity.MatchMapper.asEntity
import com.sgale.gaztelubira.core.database.migration.match.entity.MatchMapper.asModel
import com.sgale.gaztelubira.core.domain.migration.model.match.Match
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.migration.repository.MatchLocal
import javax.inject.Inject

internal class RoomMatches @Inject constructor(
    private val matchDao: MatchDao
): MatchLocal {
    override suspend fun getMatches(seasonId: SeasonId): List<Match> =
        matchDao.getMatches(seasonId).map { it.asModel() }

    override suspend fun insertMatches(matches: List<Match>) {
        matchDao.insertMatches(matches.map { it.asEntity() })
    }
}
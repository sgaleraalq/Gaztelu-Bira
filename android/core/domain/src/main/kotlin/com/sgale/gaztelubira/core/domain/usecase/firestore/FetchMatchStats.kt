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

package com.sgale.gaztelubira.core.domain.usecase.firestore

import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.repository.db.IGBMatchesStatsDb
import javax.inject.Inject

class FetchMatchStats @Inject constructor(
    private val matchesStatsDb: IGBMatchesStatsDb,
//    private val repository: IFbMatches
) {
    suspend operator fun invoke(
        matchId: FirebaseId
    ): MatchStatsModel? {
        val dbMatchStats = matchesStatsDb.getMatchStats(matchId)
        return dbMatchStats
    }
}

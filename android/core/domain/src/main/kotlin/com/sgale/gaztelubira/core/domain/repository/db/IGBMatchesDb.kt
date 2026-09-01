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

package com.sgale.gaztelubira.core.domain.repository.db

import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import kotlinx.coroutines.flow.Flow

interface IGBMatchesDb {
    suspend fun deleteMatch(id: FirebaseId)
    suspend fun fetchMatches(): List<MatchModel>
    suspend fun insertMatch(match: MatchModel)
    suspend fun insertMatches(matches: List<MatchModel>)
    suspend fun getNumberOfJourneys(): Int
    fun getMatchesListAsFlow(): Flow<List<MatchModel>>
}

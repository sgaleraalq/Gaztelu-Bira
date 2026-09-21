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

package com.sgale.gaztelubira.core.domain.repository.firestore

import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.model.stats.Stats
import com.sgale.gaztelubira.core.domain.model.team.Team
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId

interface IInsert {
    interface FirebaseInsertResult {
        data object PlayerInserted: FirebaseInsertResult
        data object TeamInserted: FirebaseInsertResult
        data object StatsInserted: FirebaseInsertResult
        data class ErrorInsert(val msg: String?): FirebaseInsertResult
    }

    suspend fun insertPlayer(player: Player): FirebaseInsertResult
    suspend fun insertTeam(team: Team): FirebaseInsertResult
    suspend fun insertStats(
        match: Match,
        matchStats: MatchStatsModel,
        playerStats: Map<FirebaseId, Stats>
    ): FirebaseInsertResult
}

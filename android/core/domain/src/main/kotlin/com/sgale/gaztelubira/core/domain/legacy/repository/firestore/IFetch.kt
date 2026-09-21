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

package com.sgale.gaztelubira.core.domain.legacy.repository.firestore

import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.model.team.Team
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseTimestamp

interface IFetch {
    fun getSeason(): String?

    /**
     * Individual
     */
    suspend fun fetchPlayer(id: FirebaseId): Player?
    suspend fun fetchPlayerStats(id: FirebaseId): PlayerStats?
    suspend fun fetchTeam(id: FirebaseId): Team?

    /**
     * Lists
     */
    suspend fun fetchMatches(): List<Match>
    suspend fun fetchMatchesStats(): List<MatchStatsModel>
    suspend fun fetchPlayers(): List<Player>
    suspend fun fetchPlayersStats(): List<PlayerStats>
    suspend fun fetchTeams(): List<Team>


    /**
     * Other
     */
    suspend fun fetchTimestamp(docName: String, timestampName: String): FirebaseTimestamp
}

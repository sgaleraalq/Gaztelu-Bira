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

package com.sgale.gaztelubira.core.domain.repository.firestore

import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseTimestamp

interface IGBFetchDataFb {
    fun getSeason(): String?
    suspend fun fetchMatches(): List<MatchModel>
    suspend fun fetchMatchesStats(): List<MatchStatsModel>
    suspend fun fetchPlayers(): List<PlayerModel>
    suspend fun fetchPlayersStats(): List<PlayerStatsModel>
    suspend fun fetchTeams(): List<TeamModel>

    /**
     * Individual
     */
    suspend fun getTeam(id: FirebaseId): TeamModel?
    suspend fun getTimestamp(docName: String, timestampName: String): FirebaseTimestamp
}

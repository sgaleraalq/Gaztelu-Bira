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

package com.sgale.gaztelubira.core.network.firebase.implementation.fetch

import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.model.team.Team
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseTimestamp
import com.sgale.gaztelubira.core.domain.repository.firestore.IFetch
import javax.inject.Inject

internal class FirebaseFetch @Inject constructor(
    private val matches: FetchMatches,
    private val matchesStats: FetchMatchesStats,
    private val player: FetchPlayer,
    private val players: FetchPlayers,
    private val playerStats: FetchPlayerStats,
    private val playersStats: FetchPlayersStats,
    private val team: FetchTeam,
    private val teams: FetchTeams,
    private val timestamp: FetchTimestamp
) : IFetch {

    override fun getSeason(): String = CURRENT_SEASON // TODO read it from remote config

    /**
     * Individual
     */
    override suspend fun fetchPlayer(id: FirebaseId): Player? =
        player(id)

    override suspend fun fetchPlayerStats(id: FirebaseId): PlayerStats? =
        playerStats(id)

    override suspend fun fetchTeam(id: FirebaseId): Team? =
        team(id)

    /**
     * Lists
     */
    override suspend fun fetchMatches(): List<Match> =
        matches()

    override suspend fun fetchMatchesStats(): List<MatchStatsModel> =
        matchesStats()

    override suspend fun fetchPlayers(): List<Player> =
        players()

    override suspend fun fetchPlayersStats(): List<PlayerStats> =
        playersStats()

    override suspend fun fetchTeams(): List<Team> =
        teams()

    /**
     * Other
     */
    override suspend fun fetchTimestamp(docName: String, timestampName: String): FirebaseTimestamp =
        timestamp(docName, timestampName)

    private companion object {
        const val CURRENT_SEASON = "2024_2025"
    }
}

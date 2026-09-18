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

package com.sgale.gaztelubira.core.network.firebase.implementation

import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.model.team.Team
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseTimestamp
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBFetchDataFb
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchMatches
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchMatchesStats
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchPlayerStats
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchPlayers
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchTeam
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchTeams
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchTimestamp
import javax.inject.Inject

internal class FbFetchDataImpl @Inject constructor(
    private val fetchMatches: FetchMatches,
    private val fetchMatchesStats: FetchMatchesStats,
    private val fetchPlayers: FetchPlayers,
    private val fetchPlayerStats: FetchPlayerStats,
    private val fetchTeams: FetchTeams,
    private val fetchTeam: FetchTeam,
    private val fetchTimestamp: FetchTimestamp,
) : IGBFetchDataFb {
    private lateinit var season: String

    override fun getSeason(): String? {
        return try {
            val season = "2024_2025" // TODO
            this.season = season
            season
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun fetchMatches(): List<Match> =
        fetchMatches.invoke()

    override suspend fun fetchMatchesStats(): List<MatchStatsModel> =
        fetchMatchesStats.invoke()

    override suspend fun fetchPlayers(): List<Player> =
        fetchPlayers.invoke()

    override suspend fun fetchPlayersStats(): List<PlayerStats> =
        fetchPlayerStats.invoke()

    override suspend fun fetchTeams(): List<Team> =
        fetchTeams.invoke()

    override suspend fun fetchTeam(id: FirebaseId): Team? =
        fetchTeam.invoke(id)

    override suspend fun fetchTimestamp(docName: String, timestampName: String): FirebaseTimestamp =
        fetchTimestamp.invoke(docName, timestampName)
}

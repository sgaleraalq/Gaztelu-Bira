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

package com.sgale.gaztelubira.core.domain.usecase.firestore.insert

import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.Stats
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.repository.db.IGBMatchesDb
import com.sgale.gaztelubira.core.domain.repository.db.IGBMatchesStatsDb
import com.sgale.gaztelubira.core.domain.repository.db.IGBPlayersDb
import com.sgale.gaztelubira.core.domain.repository.db.IGBPlayersStatsDb
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult.StatsInserted
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class InsertNewMatch @Inject constructor(
    private val firestore: IGBInsertDataFb,
    private val matchesDb: IGBMatchesDb,
    private val playersDb: IGBPlayersDb,
    private val matchesStatsDb: IGBMatchesStatsDb,
    private val playerStatsDb: IGBPlayersStatsDb
) {
    suspend operator fun invoke(
        match: MatchModel,
        matchStats: MatchStatsModel
    ): FirebaseInsertResult = coroutineScope {
        val playerStats = createPlayerStats(matchStats)

        val statsInserted = firestore.insertStats(
            match = match,
            matchStats = matchStats,
            playerStats = playerStats
        )

        if (statsInserted == StatsInserted) {
            val insertMatchDeferred = async { matchesDb.insertMatch(match) }
            val insertMatchStatsDeferred = async { matchesStatsDb.insertMatch(matchStats) }
            val insertPlayerStatsDeferred = async { playerStatsDb.insertStats(match.id, playerStats) }

            insertMatchDeferred.await()
            insertMatchStatsDeferred.await()
            insertPlayerStatsDeferred.await()
        }

        statsInserted
    }

    private suspend fun createPlayerStats(
        matchStats: MatchStatsModel
    ): Map<FirebaseId, Stats> =
        playersDb.getPlayers().associate { player ->
            player.id to createStats(player, matchStats)
        }

    private fun createStats(
        player: PlayerModel,
        matchStats: MatchStatsModel
    ) = Stats(
        assists = matchStats.stats.assists.count { it == player },
        cleanSheets = matchStats.stats.cleanSheets.count { it == player },
        fails = matchStats.stats.fails.count { it == player },
        gamesPlayed = getGamesPlayed(
            player.id,
            matchStats.lineUpPlayers,
            matchStats.benchPlayers,
            matchStats.managers
        ),
        goals = matchStats.stats.goals.count { it == player },
        goalsProvoked = matchStats.stats.goalsProvoked.count { it == player },
        penaltiesProvoked = matchStats.stats.penaltiesProvoked.count { it == player },
        redCards = matchStats.stats.redCards.count { it == player },
        saves = matchStats.stats.saves.count { it == player },
        yellowCards = matchStats.stats.yellowCards.count { it == player }
    )

    private fun getGamesPlayed(
        playerId: FirebaseId,
        lineUpPlayers: Map<Int, PlayerModel?>,
        benchPlayers: List<PlayerModel>,
        managers: List<PlayerModel>
    ): Int {
        val inLineUp = lineUpPlayers.values.any { it?.id == playerId }
        val inBench = benchPlayers.any { it.id == playerId }
        val isManager = managers.any { it.id == playerId }

        return if (inLineUp || inBench || isManager) 1 else 0
    }
}

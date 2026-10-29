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

package com.sgale.gaztelubira.activity

import android.util.Log
import com.sgale.gaztelubira.core.common.utils.TAG
import com.sgale.gaztelubira.core.domain.InitAppHandler
import com.sgale.gaztelubira.core.domain.model.match.MatchId
import com.sgale.gaztelubira.core.domain.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.repository.AppVersionRemote
import com.sgale.gaztelubira.core.domain.repository.MatchLocal
import com.sgale.gaztelubira.core.domain.repository.MatchRemote
import com.sgale.gaztelubira.core.domain.repository.PlayerLocal
import com.sgale.gaztelubira.core.domain.repository.PlayerRemote
import com.sgale.gaztelubira.core.domain.repository.Preferences
import com.sgale.gaztelubira.core.domain.repository.SeasonLocal
import com.sgale.gaztelubira.core.domain.repository.SeasonRemote
import com.sgale.gaztelubira.core.domain.repository.TeamLocal
import com.sgale.gaztelubira.core.domain.repository.TeamRemote
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.lang.System.currentTimeMillis
import javax.inject.Inject

/**
 * Keeps the local cache in step with Firestore.
 *
 * The first sync brings everything. After that, each launch reads the season list and the players'
 * stamp (a handful of reads) and only downloads again a season, or the players, whose `changedAt`
 * no longer matches the one stored with it. Whoever writes stamps what they touched.
 */
internal class FirstTime @Inject constructor(
    private val appVersion: AppVersionRemote,
    private val matchLocal: MatchLocal,
    private val matchRemote: MatchRemote,
    private val preferences: Preferences,
    private val playerLocal: PlayerLocal,
    private val playerRemote: PlayerRemote,
    private val seasonLocal: SeasonLocal,
    private val seasonRemote: SeasonRemote,
    private val teamLocal: TeamLocal,
    private val teamRemote: TeamRemote
): InitAppHandler {
    override suspend fun updateAvailable(): Boolean =
        !appVersion.isSupported()

    /**
     * Everything else hangs from a season, so without any stored there is nothing to show.
     */
    override suspend fun hasLocalData(): Boolean =
        seasonLocal.getSeasons().isNotEmpty()

    override suspend fun firstTimeInit(): Result<Boolean> =
        try {
            sync(everything = true)
            Result.success(true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun initApp() {
        try {
            sync(everything = false)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.w(TAG, "Refresh failed, the cached data stays", e)
        }
    }

    /**
     * A season is stored with its new `changedAt` only once its data is in, so a sync cut halfway
     * leaves it marked as stale and the next launch tries again.
     *
     * @param everything Downloads every season and the players, whatever their stamps say.
     */
    private suspend fun sync(everything: Boolean) {
        val playersChangedAt = playerRemote.fetchPlayersChangedAt()
        val playersChanged = everything || playersChangedAt != preferences.playersChangedAt()
        if (playersChanged) {
            playerLocal.insertPlayers(playerRemote.fetchPlayers())
            preferences.setPlayersChangedAt(playersChangedAt)
        }

        val stored = seasonLocal.getSeasons()
        val storedChangedAt = stored.associate { it.id to it.changedAt }
        val seasons = seasonRemote.fetchSeasons()

        seasons
            .filter { everything || it.id !in storedChangedAt || it.changedAt != storedChangedAt[it.id] }
            .forEach { season -> syncSeason(season.id) }
        seasonLocal.insertSeasons(seasons)

        if (playersChanged || seasons.toSet() != stored.toSet()) {
            preferences.setLastSync(currentTimeMillis())
        }
    }

    /**
     * The matches go in parallel: each one is two reads of its own, and one after another they made
     * the wait as long as the season.
     */
    private suspend fun syncSeason(seasonId: SeasonId) {
        seasonLocal.insertSeasonPlayers(seasonRemote.fetchSquad(seasonId))
        teamLocal.insertTeams(teamRemote.fetchTeams(seasonId))

        val matches = matchRemote.fetchMatches(seasonId)
        matchLocal.insertMatches(matches)

        coroutineScope {
            matches.map { match -> async { syncMatch(seasonId, match.id) } }.awaitAll()
        }
    }

    private suspend fun syncMatch(seasonId: SeasonId, matchId: MatchId) {
        matchLocal.insertMatchEvents(matchId, matchRemote.fetchMatchEvents(seasonId, matchId))

        val lineup = matchRemote.fetchMatchLineup(seasonId, matchId)
        matchLocal.insertMatchPlayers(matchId, lineup.map { it.lineup })
        playerLocal.insertMatchStats(matchId, lineup.map { it.stats })
    }
}

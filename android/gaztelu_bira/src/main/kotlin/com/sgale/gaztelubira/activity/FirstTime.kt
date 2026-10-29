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
import com.sgale.gaztelubira.core.domain.model.season.Season
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
 * The first sync brings every season. After that, each launch only brings back what can still
 * change: the players, the season list, the current season and the one on screen. A season that is
 * over is not edited anymore, and syncing all of them on every launch costs about two thousand
 * reads per user.
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
            sync { true }
            Result.success(true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun initApp() {
        try {
            val shown = preferences.resolveSelectedSeason()
            sync { season -> season.isCurrent || season.id == shown }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.w(TAG, "Refresh failed, the cached data stays", e)
        }
    }

    /**
     * The players and the season list come every time: they are small, and every season points at
     * them.
     *
     * @param refresh Which seasons get their squad, teams and matches downloaded again.
     */
    private suspend fun sync(refresh: (Season) -> Boolean) {
        playerLocal.insertPlayers(playerRemote.fetchPlayers())

        val seasons = seasonRemote.fetchSeasons()
        seasonLocal.insertSeasons(seasons)
        seasons.filter(refresh).forEach { season -> syncSeason(season.id) }

        preferences.setLastSync(currentTimeMillis())
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

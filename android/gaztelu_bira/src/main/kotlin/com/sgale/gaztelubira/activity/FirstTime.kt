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
import com.sgale.gaztelubira.core.domain.legacy.repository.InitAppHandler
import com.sgale.gaztelubira.core.domain.migration.repository.MatchLocal
import com.sgale.gaztelubira.core.domain.migration.repository.MatchRemote
import com.sgale.gaztelubira.core.domain.migration.repository.PlayerLocal
import com.sgale.gaztelubira.core.domain.migration.repository.PlayerRemote
import com.sgale.gaztelubira.core.domain.migration.repository.Preferences
import com.sgale.gaztelubira.core.domain.migration.repository.SeasonLocal
import com.sgale.gaztelubira.core.domain.migration.repository.SeasonRemote
import com.sgale.gaztelubira.core.domain.migration.repository.TeamLocal
import com.sgale.gaztelubira.core.domain.migration.repository.TeamRemote
import javax.inject.Inject

internal class FirstTime @Inject constructor(
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
        false

    override suspend fun firstTimeInit(): Result<Boolean> = runCatching {
        val players = playerRemote.fetchPlayers()
        playerLocal.insertPlayers(players)

        val seasons = seasonRemote.fetchSeasons()
        seasonLocal.insertSeasons(seasons)

        seasons.forEach { season ->
            seasonLocal.insertSeasonPlayers(seasonRemote.fetchSquad(season.id))
            matchLocal.insertMatches(matchRemote.fetchMatches(season.id))
            teamLocal.insertTeams(teamRemote.fetchTeams(season.id))
        }

        seasons.firstOrNull { it.isCurrent }?.let { preferences.selectSeason(it.id) }
        preferences.setFirstTime(false)

        Log.i(TAG, "These are the players: $players")
        Log.i(TAG, "These are the seasons: $seasons")
        true
    }

    override suspend fun initApp() {

    }
}

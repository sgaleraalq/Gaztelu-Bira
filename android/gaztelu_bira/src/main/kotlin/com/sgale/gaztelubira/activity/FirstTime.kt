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
import com.sgale.gaztelubira.core.domain.migration.repository.player.PlayerLocal
import com.sgale.gaztelubira.core.domain.migration.repository.player.PlayerRemote
import com.sgale.gaztelubira.core.domain.migration.repository.season.SeasonLocal
import com.sgale.gaztelubira.core.domain.migration.repository.season.SeasonRemote
import com.sgale.gaztelubira.core.domain.legacy.repository.InitAppHandler
import com.sgale.gaztelubira.core.domain.migration.repository.preferences.Preferences
import javax.inject.Inject

internal class FirstTime @Inject constructor(
    private val preferences: Preferences,
    private val playerRemote: PlayerRemote,
    private val playerLocal: PlayerLocal,
    private val seasonRemote: SeasonRemote,
    private val seasonLocal: SeasonLocal
): InitAppHandler {
    override suspend fun updateAvailable(): Boolean =
        false

    override suspend fun firstTimeInit(): Result<Boolean> = runCatching {
        val players = playerRemote.fetchPlayers()
        playerLocal.insertPlayers(players)

        val seasons = seasonRemote.fetchSeasons()
        val currentSeason = seasons.firstOrNull { it.isCurrent }

        currentSeason?.let {
            preferences.selectSeason(it.id)
        }

        seasonLocal.insertSeasons(seasons)

        seasons.forEach { season ->
            seasonLocal.insertSeasonPlayers(seasonRemote.fetchSquad(season.id))
        }


        Log.i(TAG, "These are the players: $players")
        Log.i(TAG, "These are the seasons: $seasons")
        true
    }

    override suspend fun initApp() {

    }
}

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

package com.sgale.gaztelubira.core.data

import com.sgale.gaztelubira.core.domain.model.utils.MATCHES_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.MATCHES_STATS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.PLAYERS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.PLAYERS_STATS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.STATS_INSERTION
import com.sgale.gaztelubira.core.domain.model.utils.TEAMS_INSERTION
import com.sgale.gaztelubira.core.domain.repository.InitAppHandler
import com.sgale.gaztelubira.core.domain.repository.db.IGBMatchesDb
import com.sgale.gaztelubira.core.domain.repository.db.IGBMatchesStatsDb
import com.sgale.gaztelubira.core.domain.repository.db.IGBPlayersDb
import com.sgale.gaztelubira.core.domain.repository.db.IGBPlayersStatsDb
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.repository.db.IGBTeamsDb
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.INFORMATION
import com.sgale.gaztelubira.core.domain.repository.firestore.FirebaseConstants.STATS
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBFetchDataFb
import com.sgale.gaztelubira.core.domain.usecase.CanAccessApp
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

class AppHandler @Inject constructor(
    private val fireRepository: IGBFetchDataFb,
    private val canAccessApp: CanAccessApp,
    private val preferences: IGBPreferences,
    private val matchesDb: IGBMatchesDb,
    private val matchesStatsDb: IGBMatchesStatsDb,
    private val playersDb: IGBPlayersDb,
    private val playersStatsDb: IGBPlayersStatsDb,
    private val teamsDb: IGBTeamsDb
) : InitAppHandler {

    override suspend fun updateAvailable(): Boolean =
        canAccessApp()

    override suspend fun firstTimeInit(): Result<Boolean> =
        initAppFirstTime()

    override suspend fun initApp() {
        notFirstTimeInit()
    }

    private suspend fun initAppFirstTime(): Result<Boolean> = runCatching {
        println("App Handler: First time joining app")
        coroutineScope {
            fetchBasicInformation()
            fetchComplexInformation()
            preferences.setFirstTime(false)
            true
        }
    }

    private suspend fun fetchBasicInformation() = runCatching {
        coroutineScope {
            val season = fireRepository.getSeason()
            if (season != null) {
                preferences.setSeason(season)
            } else {
                return@coroutineScope
            }

            val playersDeferred = async { fireRepository.fetchPlayers() }
            val teamsDeferred = async { fireRepository.fetchTeams() }
            val players = playersDeferred.await()
            val teams = teamsDeferred.await()
            playersDb.insertPlayers(players)
            teamsDb.insertTeams(teams)
        }
    }

    private suspend fun fetchComplexInformation() = runCatching {
        coroutineScope {
            val matchesDeferred = async { fireRepository.fetchMatches() }
            val matchesStatsDeferred = async { fireRepository.fetchMatchesStats() }
            val playerStatsDeferred = async { fireRepository.fetchPlayersStats() }

            val matches = matchesDeferred.await()
            val matchesStats = matchesStatsDeferred.await()
            val playerStats = playerStatsDeferred.await()

            matchesDb.insertMatches(matches)
            matchesStatsDb.insertMatchesStatsFromFB(matchesStats)
            playersStatsDb.insertStatsFromFB(playerStats)
        }
    }

    private suspend fun notFirstTimeInit() = runCatching {
        println("App Handler: Not first time joining app")
        coroutineScope {
            updateMatches()
            updateMatchesStats()
            updatePlayers()
            updatePlayersStats()
            updateTeams()
        }
    }

    private suspend fun updateMatches() {
        val lastUpdate = preferences.getTimestamp(MATCHES_INSERTION)
        val firebaseUpdate = fireRepository.getTimestamp(INFORMATION, MATCHES_INSERTION)

        if (lastUpdate < firebaseUpdate) {
            println("GazteluBiraFetch: Matches need to update")
            preferences.setTimestamp(firebaseUpdate, MATCHES_INSERTION)
            syncItems(
                fetchRemote = { fireRepository.fetchMatches() },
                fetchLocal = { matchesDb.getMatchesListAsFlow().first() },
                deleteItem = { matchesDb.deleteMatch(it) },
                insertItem = { matchesDb.insertMatch(it) },
                getId = { it.id }
            )
        } else {
            println("GazteluBiraFetch: Matches no need to update")
        }
    }

    private suspend fun updateMatchesStats() {
        val lastUpdate = preferences.getTimestamp(MATCHES_STATS_INSERTION)
        val firebaseUpdate = fireRepository.getTimestamp(STATS, STATS_INSERTION)

        if (lastUpdate < firebaseUpdate) {
            println("GazteluBiraFetch: Matches stats need to update")
            preferences.setTimestamp(firebaseUpdate, MATCHES_STATS_INSERTION)
            syncItems(
                fetchRemote = { fireRepository.fetchMatchesStats() },
                fetchLocal = { matchesStatsDb.getMatchesStatsListAsFlow().first() },
                deleteItem = { matchesStatsDb.deleteMatch(it) },
                insertItem = { matchesStatsDb.insertMatch(it) },
                getId = { it.id }
            )
        } else {
            println("GazteluBiraFetch: Matches stats no need to update")
        }
    }

    private suspend fun updatePlayers() {
        val lastUpdate = preferences.getTimestamp(PLAYERS_INSERTION)
        val firebaseUpdate = fireRepository.getTimestamp(INFORMATION, PLAYERS_INSERTION)

        if (lastUpdate < firebaseUpdate) {
            println("GazteluBiraFetch: Players need to update")
            preferences.setTimestamp(firebaseUpdate, PLAYERS_INSERTION)
            syncItems(
                fetchRemote = { fireRepository.fetchPlayers() },
                fetchLocal = { playersDb.getPlayersListAsFlow().first() },
                deleteItem = { playersDb.deletePlayer(it) },
                insertItem = { playersDb.insertPlayer(it) },
                getId = { it.id }
            )
        } else {
            println("GazteluBiraFetch: Players no need to update")
        }
    }

    private suspend fun updatePlayersStats() {
        val lastUpdate = preferences.getTimestamp(PLAYERS_STATS_INSERTION)
        val firebaseUpdate = fireRepository.getTimestamp(STATS, STATS_INSERTION)

        if (lastUpdate < firebaseUpdate) {
            println("GazteluBiraFetch: Players stats need to update")
            preferences.setTimestamp(firebaseUpdate, PLAYERS_STATS_INSERTION)
            syncItems(
                fetchRemote = { fireRepository.fetchPlayersStats() },
                fetchLocal = { playersStatsDb.getPlayersStatsListAsFlow().first() },
                deleteItem = { playersStatsDb.deletePlayer(it) },
                insertItem = { playersStatsDb.insertPlayer(it) },
                getId = { it.id }
            )
        } else {
            println("GazteluBiraFetch: Players stats no need to update")
        }
    }

    private suspend fun updateTeams() {
        val lastUpdate = preferences.getTimestamp(TEAMS_INSERTION)
        val firebaseUpdate = fireRepository.getTimestamp(INFORMATION, TEAMS_INSERTION)

        if (lastUpdate < firebaseUpdate) {
            println("GazteluBiraFetch: Teams need to update")
            preferences.setTimestamp(firebaseUpdate, TEAMS_INSERTION)
            syncItems(
                fetchRemote = { fireRepository.fetchTeams() },
                fetchLocal = { teamsDb.getTeamsList().first() },
                deleteItem = { teamsDb.deleteTeam(it) },
                insertItem = { teamsDb.insertTeam(it) },
                getId = { it.id }
            )
        } else {
            println("GazteluBiraFetch: Teams no need to update")
        }
    }

    private suspend fun <T> syncItems(
        fetchRemote: suspend () -> List<T>,
        fetchLocal: suspend () -> List<T>,
        deleteItem: suspend (String) -> Unit,
        insertItem: suspend (T) -> Unit,
        getId: (T) -> String
    ) {
        val remoteItems = fetchRemote()
        val localItems = fetchLocal()

        val removedItems = localItems.filter { local ->
            remoteItems.none { remote -> getId(remote) == getId(local) }
        }

        removedItems.forEach { item ->
            deleteItem(getId(item))
        }

        val changedItems = remoteItems.filter { remote ->
            localItems.any { local -> getId(remote) == getId(local) }
        }

        changedItems.forEach { item ->
            insertItem(item)
        }
    }
}

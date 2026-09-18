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
import com.sgale.gaztelubira.core.domain.repository.firestore.IFetch
import com.sgale.gaztelubira.core.domain.usecase.CanAccessApp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AppHandler @Inject constructor(
    private val fireRepository: IFetch,
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
            markSynced(INFORMATION, PLAYERS_INSERTION)
            markSynced(INFORMATION, TEAMS_INSERTION)
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
            markSynced(INFORMATION, MATCHES_INSERTION)
            markSynced(STATS, STATS_INSERTION, MATCHES_STATS_INSERTION)
            markSynced(STATS, STATS_INSERTION, PLAYERS_STATS_INSERTION)
        }
    }

    private suspend fun notFirstTimeInit() = runCatching {
        println("App Handler: Not first time joining app")
        coroutineScope {
            update("Matches") { updateMatches() }
            update("Matches stats") { updateMatchesStats() }
            update("Players") { updatePlayers() }
            update("Players stats") { updatePlayersStats() }
            update("Teams") { updateTeams() }
        }
    }

    private suspend fun updateMatches() {
        val lastUpdate = preferences.getTimestamp(MATCHES_INSERTION)
        val firebaseUpdate = fireRepository.fetchTimestamp(INFORMATION, MATCHES_INSERTION)

        if (lastUpdate < firebaseUpdate) {
            println("GazteluBiraFetch: Matches need to update")
            syncItems(
                fetchRemote = { fireRepository.fetchMatches() },
                fetchLocal = { matchesDb.getMatchesListAsFlow().first() },
                deleteItem = { matchesDb.deleteMatch(it) },
                insertItem = { matchesDb.insertMatch(it) },
                getId = { it.id }
            )
            preferences.setTimestamp(firebaseUpdate, MATCHES_INSERTION)
        } else {
            println("GazteluBiraFetch: Matches no need to update")
        }
    }

    private suspend fun updateMatchesStats() {
        val lastUpdate = preferences.getTimestamp(MATCHES_STATS_INSERTION)
        val firebaseUpdate = fireRepository.fetchTimestamp(STATS, STATS_INSERTION)

        if (lastUpdate < firebaseUpdate) {
            println("GazteluBiraFetch: Matches stats need to update")
            syncItems(
                fetchRemote = { fireRepository.fetchMatchesStats() },
                fetchLocal = { matchesStatsDb.getMatchesStatsListAsFlow().first() },
                deleteItem = { matchesStatsDb.deleteMatch(it) },
                insertItem = { matchesStatsDb.insertMatch(it) },
                getId = { it.id }
            )
            preferences.setTimestamp(firebaseUpdate, MATCHES_STATS_INSERTION)
        } else {
            println("GazteluBiraFetch: Matches stats no need to update")
        }
    }

    private suspend fun updatePlayers() {
        val lastUpdate = preferences.getTimestamp(PLAYERS_INSERTION)
        val firebaseUpdate = fireRepository.fetchTimestamp(INFORMATION, PLAYERS_INSERTION)

        if (lastUpdate < firebaseUpdate) {
            println("GazteluBiraFetch: Players need to update")
            syncItems(
                fetchRemote = { fireRepository.fetchPlayers() },
                fetchLocal = { playersDb.getPlayersListAsFlow().first() },
                deleteItem = { playersDb.deletePlayer(it) },
                insertItem = { playersDb.insertPlayer(it) },
                getId = { it.id }
            )
            preferences.setTimestamp(firebaseUpdate, PLAYERS_INSERTION)
        } else {
            println("GazteluBiraFetch: Players no need to update")
        }
    }

    private suspend fun updatePlayersStats() {
        val lastUpdate = preferences.getTimestamp(PLAYERS_STATS_INSERTION)
        val firebaseUpdate = fireRepository.fetchTimestamp(STATS, STATS_INSERTION)

        if (lastUpdate < firebaseUpdate) {
            println("GazteluBiraFetch: Players stats need to update")
            syncItems(
                fetchRemote = { fireRepository.fetchPlayersStats() },
                fetchLocal = { playersStatsDb.getPlayersStatsListAsFlow().first() },
                deleteItem = { playersStatsDb.deletePlayer(it) },
                insertItem = { playersStatsDb.insertPlayer(it) },
                getId = { it.id }
            )
            preferences.setTimestamp(firebaseUpdate, PLAYERS_STATS_INSERTION)
        } else {
            println("GazteluBiraFetch: Players stats no need to update")
        }
    }

    private suspend fun updateTeams() {
        val lastUpdate = preferences.getTimestamp(TEAMS_INSERTION)
        val firebaseUpdate = fireRepository.fetchTimestamp(INFORMATION, TEAMS_INSERTION)

        if (lastUpdate < firebaseUpdate) {
            println("GazteluBiraFetch: Teams need to update")
            syncItems(
                fetchRemote = { fireRepository.fetchTeams() },
                fetchLocal = { teamsDb.getTeamsList().first() },
                deleteItem = { teamsDb.deleteTeam(it) },
                insertItem = { teamsDb.insertTeam(it) },
                getId = { it.id }
            )
            preferences.setTimestamp(firebaseUpdate, TEAMS_INSERTION)
        } else {
            println("GazteluBiraFetch: Teams no need to update")
        }
    }

    /**
     * One entity failing must not stop the others: each fetch reaches the
     * network on its own and an error there is not a reason to skip the rest.
     */
    private suspend fun update(name: String, block: suspend () -> Unit) {
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            println("GazteluBiraFetch: $name update failed, error: ${e.message}")
        }
    }

    /**
     * Remembers which remote version the local copy matches. It runs once the
     * data is stored: marking it before would claim a failed fetch as synced.
     */
    private suspend fun markSynced(
        document: String,
        remoteTimestamp: String,
        localTimestamp: String = remoteTimestamp
    ) = preferences.setTimestamp(
        fireRepository.fetchTimestamp(document, remoteTimestamp),
        localTimestamp
    )

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

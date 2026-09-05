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

package com.sgale.gaztelubira.core.data.mappers

import com.sgale.gaztelubira.core.data.db.entities.MatchEntity
import com.sgale.gaztelubira.core.data.db.entities.MatchStatsEntity
import com.sgale.gaztelubira.core.data.db.entities.PlayerStatsEntity
import com.sgale.gaztelubira.core.data.db.entities.StatsMatchEntity
import com.sgale.gaztelubira.core.data.network.response.MatchResponse
import com.sgale.gaztelubira.core.data.network.response.MatchStatsResponse
import com.sgale.gaztelubira.core.data.network.response.PlayerStatsResponse
import com.sgale.gaztelubira.core.data.network.response.StatsResponse
import com.sgale.gaztelubira.core.domain.model.utils.GazteluBiraUtils.GAZTELU_BIRA
import com.sgale.gaztelubira.core.domain.model.utils.GazteluBiraUtils.GAZTELU_BIRA_ID
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.match.MatchStats
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.match.MatchType.Companion.fromString
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.player.Stats
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.model.utils.ErrorMatch
import com.sgale.gaztelubira.core.domain.model.utils.ErrorPlayer
import com.sgale.gaztelubira.core.domain.model.utils.ErrorTeam
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId

/**
 * Match Model
 */
fun List<MatchResponse>.asMatchModel(map: Map<FirebaseId, TeamModel>) =
    this.map { it.asMatchModel(map) }

fun MatchResponse.asMatchModel(
    teamMap: Map<FirebaseId, TeamModel>
) = MatchModel(
    id = id,
    date = date,
    matchName = matchName,
    matchType = fromString(matchType),
    localTeam = getTeamModelFromMap(teamMap, localTeam),
    visitorTeam = getTeamModelFromMap(teamMap, visitorTeam),
    localGoals = localGoals,
    visitorGoals = visitorGoals
)

fun MatchEntity.asMatchModel(
    teamMap: Map<FirebaseId, TeamModel>
) = MatchModel(
    id = id,
    date = date,
    matchName = matchName,
    matchType = fromString(matchType),
    localTeam = getTeamModelFromMap(teamMap, localTeam),
    visitorTeam = getTeamModelFromMap(teamMap, visitorTeam),
    localGoals = localGoals,
    visitorGoals = visitorGoals
)

private fun getTeamModelFromMap(
    map: Map<FirebaseId, TeamModel>,
    id: FirebaseId
) = if (id == GAZTELU_BIRA_ID) GAZTELU_BIRA else map[id] ?: ErrorTeam

/**
 * Match Stats
 */
fun MatchStatsResponse.asMatchStatsModel(
    matchesMap: Map<FirebaseId, MatchModel>,
    playersMap: Map<FirebaseId, PlayerModel>
) = MatchStatsModel(
    id = id,
    location = location,
    description = description,
    matchModel = matchesMap[id] ?: ErrorMatch,
    formation = formation,
    lineUpPlayers = lineUpPlayers
        .mapKeys { it.key.toInt() }
        .mapValues { playersMap[it.value] },
    benchPlayers = benchPlayers.mapNotNull { playersMap[it] },
    managers = managers.mapNotNull { playersMap[it] },
    stats = stats.asStatsMatchModel(playersMap)
)

fun MatchStatsEntity.asMatchStatsModel(
    matchesMap: Map<FirebaseId, MatchModel>,
    playersMap: Map<FirebaseId, PlayerModel>
) = MatchStatsModel(
    id = id,
    location = location,
    description = description,
    matchModel = matchesMap[id] ?: ErrorMatch,
    formation = formation,
    lineUpPlayers = lineUpPlayers.mapValues { playersMap[it.value] },
    benchPlayers = benchPlayers.mapNotNull { playersMap[it] },
    managers = managers.mapNotNull { playersMap[it] },
    stats = stats.asStatsMatchModel(playersMap)
)

/**
 * Player Stats
 */
fun PlayerStatsEntity.asPlayerStatsModel(
    playersMap: Map<FirebaseId, PlayerModel>,
    rules: String = "" // TODO
) = PlayerStatsModel(
    id = id,
    player = playersMap[id] ?: ErrorPlayer,
    stats = stats
)

fun StatsResponse.asStatsMatchModel(
    playersMap: Map<FirebaseId, PlayerModel>
) = MatchStats(
    assists = assists.mapNotNull { playersMap[it] },
    cleanSheets = cleanSheets.mapNotNull { playersMap[it] },
    fails = fails.mapNotNull { playersMap[it] },
    goals = goals.mapNotNull { playersMap[it] },
    goalsProvoked = goalsProvoked.mapNotNull { playersMap[it] },
    penaltiesProvoked = penaltiesProvoked.mapNotNull { playersMap[it] },
    redCards = redCards.mapNotNull { playersMap[it] },
    saves = saves.mapNotNull { playersMap[it] },
    yellowCards = yellowCards.mapNotNull { playersMap[it] }
)

fun StatsMatchEntity.asStatsMatchModel(
    playersMap: Map<FirebaseId, PlayerModel>
) = MatchStats(
    assists = assists.mapNotNull { playersMap[it] },
    cleanSheets = cleanSheets.mapNotNull { playersMap[it] },
    fails = fails.mapNotNull { playersMap[it] },
    goals = goals.mapNotNull { playersMap[it] },
    goalsProvoked = goalsProvoked.mapNotNull { playersMap[it] },
    penaltiesProvoked = penaltiesProvoked.mapNotNull { playersMap[it] },
    redCards = redCards.mapNotNull { playersMap[it] },
    saves = saves.mapNotNull { playersMap[it] },
    yellowCards = yellowCards.mapNotNull { playersMap[it] }
)

fun Stats.asStatsResponse() =
    PlayerStatsResponse(
        assists = assists,
        cleanSheets = cleanSheets,
        fails = fails,
        gamesPlayed = gamesPlayed,
        goals = goals,
        goalsProvoked = goalsProvoked,
        penaltiesProvoked = penaltiesProvoked,
        redCards = redCards,
        saves = saves,
        yellowCards = yellowCards
    )

/**
 * Other
 */
fun MatchStats.asStatsMatchResponse() =
    StatsResponse(
        assists = assists.map { it.id },
        cleanSheets = cleanSheets.map { it.id },
        fails = fails.map { it.id },
        goals = goals.map { it.id },
        goalsProvoked = goalsProvoked.map { it.id },
        penaltiesProvoked = penaltiesProvoked.map { it.id },
        redCards = redCards.map { it.id },
        saves = saves.map { it.id },
        yellowCards = yellowCards.map { it.id }
    )

fun MatchStats.asStatsMatchEntity() =
    StatsMatchEntity(
        assists = assists.map { it.id },
        cleanSheets = cleanSheets.map { it.id },
        fails = fails.map { it.id },
        goals = goals.map { it.id },
        goalsProvoked = goalsProvoked.map { it.id },
        penaltiesProvoked = penaltiesProvoked.map { it.id },
        redCards = redCards.map { it.id },
        saves = saves.map { it.id },
        yellowCards = yellowCards.map { it.id }
    )

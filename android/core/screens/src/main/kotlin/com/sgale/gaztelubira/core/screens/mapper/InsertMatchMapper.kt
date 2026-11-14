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

package com.sgale.gaztelubira.core.screens.mapper

import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.match.MatchCompetition
import com.sgale.gaztelubira.core.domain.model.match.MatchCompetition.Cup
import com.sgale.gaztelubira.core.domain.model.match.MatchCompetition.League
import com.sgale.gaztelubira.core.domain.model.match.MatchId
import com.sgale.gaztelubira.core.domain.model.match.MatchInformation
import com.sgale.gaztelubira.core.domain.model.match.MatchScore
import com.sgale.gaztelubira.core.domain.model.match.Score
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchLineupPlayer
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchPlayer
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchPlayerState
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchPlayerState.BENCH
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchPlayerState.MANAGER
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchPlayerState.STARTER
import com.sgale.gaztelubira.core.domain.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.model.team.TeamFormation
import com.sgale.gaztelubira.core.domain.model.team.TeamId
import com.sgale.gaztelubira.core.domain.utils.GazteluBiraUtils.GAZTELU_BIRA_ID
import com.sgale.gaztelubira.core.screens.mapper.MatchUiMapper.toLineUpFormation
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.CUP
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.LEAGUE
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.model.GBStat.ASSISTS
import com.sgale.gaztelubira.multiplatform.model.GBStat.BLUE_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBStat.CLEAN_SHEETS
import com.sgale.gaztelubira.multiplatform.model.GBStat.FAILS
import com.sgale.gaztelubira.multiplatform.model.GBStat.GOALS
import com.sgale.gaztelubira.multiplatform.model.GBStat.GOALS_PROVOKED
import com.sgale.gaztelubira.multiplatform.model.GBStat.PENALTIES_PROVOKED
import com.sgale.gaztelubira.multiplatform.model.GBStat.RED_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBStat.SAVES
import com.sgale.gaztelubira.multiplatform.model.GBStat.YELLOW_CARDS
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset.UTC

private const val LEAGUE_ID = "l"
private const val CUP_ID = "cup"

/**
 * From the filled-in form to the match the domain stores.
 */
internal object InsertMatchMapper {

    /**
     * The form can only offer what the domain can store, so the list is the domain's own
     * formations seen from the UI side.
     */
    val supportedFormations: List<LineUpFormation> =
        TeamFormation.entries.map { it.toLineUpFormation() }

    /**
     * @param seasonMatches The matches the season already has, which is what the next cup match
     * is numbered after.
     */
    fun InsertMatchUiState.toMatch(
        seasonId: SeasonId,
        seasonMatches: List<Match>
    ): Match {
        val competition = toCompetition()
        val rivalId = TeamId(rival?.id.orEmpty())
        val teamId = TeamId(GAZTELU_BIRA_ID)
        val teamGoals = teamGoals.toIntOrNull() ?: 0
        val rivalGoals = rivalGoals.toIntOrNull() ?: 0

        return Match(
            id = competition.toMatchId(seasonId, seasonMatches),
            seasonId = seasonId,
            competition = competition,
            information = MatchInformation(
                date = toEpochSeconds(),
                description = description.trim(),
                location = location.trim()
            ),
            match = MatchScore(
                formation = TeamFormation.entries.first { it.toLineUpFormation() == formation },
                isLocal = isLocal,
                localTeam = if (isLocal) teamId else rivalId,
                visitorTeam = if (isLocal) rivalId else teamId,
                score = Score(
                    local = if (isLocal) teamGoals else rivalGoals,
                    visitor = if (isLocal) rivalGoals else teamGoals
                )
            )
        )
    }

    /**
     * One entry per person called up. A slot is stored one-based, which is how the match detail
     * reads it back.
     */
    fun InsertMatchUiState.toMatchPlayers(match: Match): List<MatchPlayer> {
        val starters = formation.positions.indices.mapNotNull { index ->
            lineUp[index]?.let { player -> toMatchPlayer(player, match, STARTER, index + 1) }
        }
        val bench = bench.map { toMatchPlayer(it, match, BENCH) }
        val managers = managers.map { toMatchPlayer(it, match, MANAGER) }
        return (starters + bench + managers).distinctBy { it.lineup.playerId }
    }

    private fun InsertMatchUiState.toMatchPlayer(
        player: GBPlayer,
        match: Match,
        state: MatchPlayerState,
        formationPosition: Int? = null
    ): MatchPlayer {
        val playerId = PlayerId(player.id)
        return MatchPlayer(
            lineup = MatchLineupPlayer(
                playerId = playerId,
                matchId = match.id,
                seasonId = match.seasonId,
                state = state,
                formationPosition = formationPosition
            ),
            stats = PlayerStats(
                playerId = playerId,
                matchId = match.id,
                assists = count(ASSISTS, player),
                cleanSheets = count(CLEAN_SHEETS, player).toDouble(),
                fails = count(FAILS, player),
                goals = count(GOALS, player),
                goalsProvoked = count(GOALS_PROVOKED, player),
                penaltiesProvoked = count(PENALTIES_PROVOKED, player),
                saves = count(SAVES, player),
                blueCards = count(BLUE_CARDS, player),
                redCards = count(RED_CARDS, player),
                yellowCards = count(YELLOW_CARDS, player)
            )
        )
    }

    /**
     * How many times [player] is listed under [stat].
     */
    private fun InsertMatchUiState.count(stat: GBStat, player: GBPlayer): Int =
        stats[stat].orEmpty().count { it.id == player.id }

    private fun InsertMatchUiState.toCompetition(): MatchCompetition =
        when (type) {
            LEAGUE -> League(journey = journey)
            CUP -> Cup(name = cupName.trim(), round = cupRound.trim().ifBlank { null })
        }

    /**
     * Deterministic on purpose: `2026_2027_l05` for league matchday 5, `2026_2027_cup01` for the
     * first cup match. Two people publishing the same matchday write the same document.
     */
    private fun MatchCompetition.toMatchId(
        seasonId: SeasonId,
        seasonMatches: List<Match>
    ): MatchId {
        val suffix = when (this) {
            is League -> LEAGUE_ID + journey.twoDigits()
            is Cup -> CUP_ID + (seasonMatches.count { it.competition is Cup } + 1).twoDigits()
        }
        return MatchId("${seasonId.value}_$suffix")
    }

    /**
     * The date picker gives the day as midnight UTC; the kick-off is that calendar day at the
     * chosen time, on the clock of whoever is typing it in.
     */
    private fun InsertMatchUiState.toEpochSeconds(): Long {
        val day = Instant.ofEpochMilli(date ?: 0L).atZone(UTC).toLocalDate()
        return day.atTime(LocalTime.of(hour, minute))
            .atZone(ZoneId.systemDefault())
            .toEpochSecond()
    }

    private fun Int.twoDigits(): String = toString().padStart(2, '0')
}

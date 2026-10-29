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
import com.sgale.gaztelubira.core.domain.model.match.MatchCompetition.Cup
import com.sgale.gaztelubira.core.domain.model.match.MatchCompetition.League
import com.sgale.gaztelubira.core.domain.model.match.MatchInformation
import com.sgale.gaztelubira.core.domain.model.match.MatchScore
import com.sgale.gaztelubira.core.domain.model.match.MatchScore.Companion.isGBLocal
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
import com.sgale.gaztelubira.core.domain.model.team.TeamId
import com.sgale.gaztelubira.core.domain.utils.matchIdOf
import com.sgale.gaztelubira.core.screens.mapper.MatchUiMapper.toTeamFormation
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

/**
 * Only called once the form is valid, so both teams and both scores are there.
 */
internal object InsertMatchUiMapper {
    /**
     * @param date Epoch millis; the domain keeps seconds.
     * @param cupNumber Which cup match of the season this would be, from 1.
     */
    fun InsertMatchUiState.toMatch(season: SeasonId, date: Long, cupNumber: Int): Match {
        val competition = when (type) {
            LEAGUE -> League(journey)
            CUP -> Cup(name = cupName.trim(), round = cupRound.trim().ifBlank { null })
        }
        val local = TeamId(requireNotNull(localTeam).id)
        val visitor = TeamId(requireNotNull(visitorTeam).id)

        return Match(
            id = matchIdOf(season, competition, cupNumber),
            seasonId = season,
            competition = competition,
            information = MatchInformation(
                date = date / 1_000,
                description = description.trim(),
                location = location.trim()
            ),
            match = MatchScore(
                formation = formation.toTeamFormation(),
                isLocal = local.isGBLocal(),
                localTeam = local,
                visitorTeam = visitor,
                score = Score(requireNotNull(localGoals), requireNotNull(visitorGoals))
            )
        )
    }

    /**
     * Everyone called up gets a row, even with nothing to their name: a row of zeros means they
     * were there.
     */
    fun InsertMatchUiState.toMatchPlayers(match: Match): List<MatchPlayer> =
        lineUp.map { (index, player) -> toMatchPlayer(player, match, STARTER, index + 1) } +
            bench.map { toMatchPlayer(it, match, BENCH, null) } +
            managers.map { toMatchPlayer(it, match, MANAGER, null) }

    private fun InsertMatchUiState.toMatchPlayer(
        player: GBPlayer,
        match: Match,
        role: MatchPlayerState,
        formationPosition: Int?
    ): MatchPlayer {
        val id = PlayerId(player.id)
        fun times(given: GBStat): Int = stat(given).count { it.id == player.id }

        return MatchPlayer(
            lineup = MatchLineupPlayer(
                playerId = id,
                matchId = match.id,
                seasonId = match.seasonId,
                state = role,
                formationPosition = formationPosition
            ),
            stats = PlayerStats(
                playerId = id,
                matchId = match.id,
                assists = times(ASSISTS),
                cleanSheets = times(CLEAN_SHEETS).toDouble(),
                fails = times(FAILS),
                goals = times(GOALS),
                goalsProvoked = times(GOALS_PROVOKED),
                penaltiesProvoked = times(PENALTIES_PROVOKED),
                saves = times(SAVES),
                blueCards = times(BLUE_CARDS),
                redCards = times(RED_CARDS),
                yellowCards = times(YELLOW_CARDS)
            )
        )
    }
}

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

package com.sgale.gaztelubira.core.network.firebase.mapper

import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchLineupPlayer
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchPlayer
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchPlayerState
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchPlayerState.UNDEFINED
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.network.firebase.NetworkMapper
import com.sgale.gaztelubira.core.network.firebase.response.MatchPlayerResponse

internal object MatchPlayerMapper : NetworkMapper<MatchPlayer, MatchPlayerResponse, MatchId, SeasonId> {
    override fun MatchPlayer.asResponse(): MatchPlayerResponse =
        MatchPlayerResponse(
            playerId = lineup.playerId.value,
            matchId = lineup.matchId.value,
            seasonId = lineup.seasonId.value,
            role = lineup.state.name,
            formationPosition = lineup.formationPosition,
            assists = stats.assists,
            cleanSheets = stats.cleanSheets,
            fails = stats.fails,
            goals = stats.goals,
            goalsProvoked = stats.goalsProvoked,
            penaltiesProvoked = stats.penaltiesProvoked,
            saves = stats.saves,
            blueCards = stats.blueCards,
            redCards = stats.redCards,
            yellowCards = stats.yellowCards
        )

    override fun MatchPlayerResponse.asModel(id: MatchId, parentId: SeasonId?): MatchPlayer =
        MatchPlayer(
            lineup = MatchLineupPlayer(
                playerId = PlayerId(playerId),
                matchId = id,
                seasonId = requireNotNull(parentId) { "Lineup of match $id needs the season it belongs to" },
                state = MatchPlayerState.entries.find { it.name == role } ?: UNDEFINED,
                formationPosition = formationPosition
            ),
            stats = PlayerStats(
                playerId = PlayerId(playerId),
                matchId = id,
                assists = assists,
                cleanSheets = cleanSheets,
                fails = fails,
                goals = goals,
                goalsProvoked = goalsProvoked,
                penaltiesProvoked = penaltiesProvoked,
                saves = saves,
                blueCards = blueCards,
                redCards = redCards,
                yellowCards = yellowCards
            )
        )
}

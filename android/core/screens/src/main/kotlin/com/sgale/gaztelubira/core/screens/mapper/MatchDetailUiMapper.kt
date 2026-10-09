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

import com.sgale.gaztelubira.core.domain.model.match.detail.MatchDetail
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchLineupPlayer
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchPlayerState
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchPlayerState.BENCH
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchPlayerState.MANAGER
import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.model.player.PlayerStats
import com.sgale.gaztelubira.core.screens.mapper.MatchUiMapper.toLineUpFormation
import com.sgale.gaztelubira.core.screens.mapper.PlayerUiMapper.toGBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBTeam
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailInformation
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailLineUp
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailStats

internal object MatchDetailUiMapper {
    fun MatchDetail.toMatchDetailInformation(
        localTeam: GBTeam,
        visitorTeam: GBTeam
    ) = MatchDetailInformation(
        local = localTeam,
        visitor = visitorTeam,
        date = match.information.date,
        description = match.information.description,
        location = match.information.location
    )

    fun MatchDetail.toMatchDetailLineUp(
        players: Map<PlayerId, Player>
    ) = MatchDetailLineUp(
        benchPlayers = this.players.toGBPlayers(BENCH, players),
        managers = this.players.toGBPlayers(MANAGER, players),
        matchFormation = match.match.formation.toLineUpFormation(),
        players = this.players
            .mapNotNull { p -> p.formationPosition?.let { it - 1 to p.playerId.toGBPlayer(players) } }
            .toMap()
    )

    fun List<PlayerStats>.toMatchDetailStats(
        players: Map<PlayerId, Player>
    ): MatchDetailStats {
        fun playersWith(stat: (PlayerStats) -> Number) =
            filter { stat(it).toDouble() > 0 }.map { it.playerId.toGBPlayer(players) }

        return MatchDetailStats(
            assists = playersWith { it.assists },
            blueCards = playersWith { it.blueCards },
            cleanSheets = playersWith { it.cleanSheets },
            fails = playersWith { it.fails },
            goals = playersWith { it.goals },
            goalsProvoked = playersWith { it.goalsProvoked },
            penaltiesProvoked = playersWith { it.penaltiesProvoked },
            redCards = playersWith { it.redCards },
            saves = playersWith { it.saves },
            yellowCards = playersWith { it.yellowCards }
        )
    }

    private fun List<MatchLineupPlayer>.toGBPlayers(
        filterState: MatchPlayerState,
        players: Map<PlayerId, Player>
    ): List<GBPlayer> = filter { it.state == filterState }.map { it.playerId.toGBPlayer(players) }
}

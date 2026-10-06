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
package com.sgale.gaztelubira.core.database.migration.match.entity

import com.sgale.gaztelubira.core.database.DatabaseMapper
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchPlayer
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchPlayerState
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchPlayerStats

internal object MatchPlayerMapper : DatabaseMapper<MatchPlayer, MatchPlayerEntity> {
    override fun MatchPlayer.asEntity(): MatchPlayerEntity =
        MatchPlayerEntity(
            matchId = matchId,
            playerId = playerId,
            seasonId = seasonId,
            state = state.name,
            formationPosition = formationPosition,
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

    override fun MatchPlayerEntity.asModel(): MatchPlayer =
        MatchPlayer(
            playerId = playerId,
            matchId = matchId,
            seasonId = seasonId,
            state = MatchPlayerState.entries.find { it.name == state } ?: MatchPlayerState.UNDEFINED,
            formationPosition = formationPosition,
            stats = MatchPlayerStats(
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

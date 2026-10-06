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

package com.sgale.gaztelubira.core.network.migration.firebase.mapper

import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchLineup
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchPlayerState
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchPlayerState.UNDEFINED
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.network.NetworkMapper
import com.sgale.gaztelubira.core.network.migration.firebase.response.MatchPlayerResponse

internal object MatchPlayerMapper : NetworkMapper<MatchLineup, MatchPlayerResponse> {
    override fun MatchLineup.asResponse(): MatchPlayerResponse =
        MatchPlayerResponse(
            playerId = playerId.value,
            matchId = matchId.value,
            seasonId = seasonId.value,
            role = state.name,
            formationPosition = formationPosition
        )

    override fun MatchPlayerResponse.asModel(): MatchLineup =
        MatchLineup(
            playerId = PlayerId(playerId),
            matchId = MatchId(matchId),
            seasonId = SeasonId(seasonId),
            state = MatchPlayerState.entries.find { it.name == role } ?: UNDEFINED,
            formationPosition = formationPosition
        )
}

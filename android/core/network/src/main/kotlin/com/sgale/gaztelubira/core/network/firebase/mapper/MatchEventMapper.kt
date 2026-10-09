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

import com.sgale.gaztelubira.core.domain.model.match.MatchId
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchEvent
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchEventType
import com.sgale.gaztelubira.core.domain.model.season.SeasonId
import com.sgale.gaztelubira.core.network.firebase.NetworkMapper
import com.sgale.gaztelubira.core.network.firebase.response.MatchEventResponse

internal object MatchEventMapper : NetworkMapper<MatchEvent, MatchEventResponse, MatchId, SeasonId> {
    override fun MatchEvent.asResponse(): MatchEventResponse =
        MatchEventResponse(
            matchId = matchId.value,
            seasonId = seasonId.value,
            order = order,
            type = type?.name.orEmpty(),
            ownGoal = ownGoal,
            scoredFor = scoredFor,
            assist = assist,
            scorer = scorer,
            provokedBy = provokedBy,
            scored = scored,
            taker = taker
        )

    override fun MatchEventResponse.asModel(
        id: MatchId,
        parentId: SeasonId?
    ): MatchEvent = MatchEvent(
        matchId = id,
        seasonId = requireNotNull(parentId) { "Events of match $id need the season it belongs to" },
        order = order,
        type = MatchEventType.valueOf(type),
        ownGoal = ownGoal,
        scoredFor = scoredFor,
        assist = assist,
        scorer = scorer,
        provokedBy = provokedBy,
        scored = scored,
        taker = taker
    )
}

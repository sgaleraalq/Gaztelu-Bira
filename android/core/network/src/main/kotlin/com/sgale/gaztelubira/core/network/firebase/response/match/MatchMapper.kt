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

package com.sgale.gaztelubira.core.network.firebase.response.match

import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.match.MatchType
import com.sgale.gaztelubira.core.network.NetworkMapper

internal object MatchMapper : NetworkMapper<MatchResponse, Match> {
    override fun asResponse(
        domain: Match
    ) = MatchResponse(
        id = domain.id,
        date = domain.date,
        matchName = domain.matchName,
        matchType = domain.matchType.name,
        localTeam = domain.localTeam.id,
        localGoals = domain.localGoals,
        visitorTeam = domain.visitorTeam.id,
        visitorGoals = domain.visitorGoals
    )

    override fun asModel(
        response: MatchResponse
    ) = Match(
        id = response.id,
        date = response.date,
        matchName = response.matchName,
        matchType = MatchType.valueOf(response.matchType),
        localTeam = response.localTeam.id,
        localGoals = response.localGoals,
        visitorTeam = response.visitorTeam.id,
        visitorGoals = response.visitorGoals
    )
}

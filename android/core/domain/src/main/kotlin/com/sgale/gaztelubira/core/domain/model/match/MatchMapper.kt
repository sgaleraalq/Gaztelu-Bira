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

package com.sgale.gaztelubira.core.domain.model.match

import com.sgale.gaztelubira.core.domain.model.team.TeamMapper.toGBMatchResult
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.utils.toDate
import com.sgale.gaztelubira.multiplatform.model.GBMatch
import com.sgale.gaztelubira.multiplatform.model.GBMatchResult
import com.sgale.gaztelubira.multiplatform.model.GBMatchTeam
import com.sgale.gaztelubira.multiplatform.model.GBMatchType
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.CUP
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.LEAGUE

object MatchMapper {
    fun MatchModel.toGBMatch(
        appTeam: TeamModel?,
        result: MatchResult
    ): GBMatch =
        GBMatch(
            id = id,
            name = matchName,
            type = matchType.toGBMatchType(),
            date = date.toDate(),
            localTeam = localTeam.toGBMatchTeam(),
            visitorTeam = visitorTeam.toGBMatchTeam(),
            localGoals = localGoals,
            visitorGoals = visitorGoals,
            result = if (appTeam == null) GBMatchResult.UNDEFINED else result.toGBMatchResult()
        )

    private fun TeamModel.toGBMatchTeam(): GBMatchTeam =
        GBMatchTeam(
            name = name,
            logo = logo
        )

    private fun MatchType.toGBMatchType(): GBMatchType =
        when (this) {
            MatchType.League -> LEAGUE
            MatchType.Cup -> CUP
        }
}

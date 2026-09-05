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

package com.sgale.gaztelubira.core.screens.home.tabs.matches

import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.match.MatchResult
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.Defeat
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.Draw
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.Undefined
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.Victory
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import javax.inject.Inject

class GetMatchResultUseCase @Inject constructor() {
    operator fun invoke(
        match: MatchModel,
        appTeam: TeamModel?
    ): MatchResult {
        if (appTeam == null) return Undefined

        val isLocal = match.localTeam.id == appTeam.id
        val goalsFor = if (isLocal) match.localGoals else match.visitorGoals
        val goalsAgainst = if (isLocal) match.visitorGoals else match.localGoals

        return when {
            goalsFor > goalsAgainst -> Victory
            goalsFor < goalsAgainst -> Defeat
            else -> Draw
        }
    }
}

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

package com.sgale.gaztelubira.core.preview

import com.sgale.gaztelubira.core.domain.model.utils.GazteluBiraUtils.GAZTELU_BIRA
import com.sgale.gaztelubira.core.preview.TeamProvider.provideRandomTeam
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.match.MatchType
import com.sgale.gaztelubira.core.domain.utils.generateRandomUUID
import kotlin.random.Random

object MatchProvider {

    const val JOURNEY = "Journey"

    fun provideMatchesList(matches: Int): List<MatchModel> = List(matches) {
        provideMatch(it + 1)
    }

    private fun randomLongFrom2025to2026(): Long {
        val start = 1735689600000L
        val end = 1767225600000L
        return Random.nextLong(start, end)
    }
    private fun provideMatch(journey: Int): MatchModel {
        val gazteluLocal = (0..1).random() == 0

        return MatchModel(
            date = randomLongFrom2025to2026(),
            id = generateRandomUUID(),
            matchName = "$JOURNEY $journey",
            matchType = MatchType.entries.random(),
            localTeam = if (gazteluLocal) GAZTELU_BIRA else provideRandomTeam(),
            visitorTeam = if (!gazteluLocal) GAZTELU_BIRA else provideRandomTeam() ,
            localGoals = (0..5).random(),
            visitorGoals = (0..5).random()
        )
    }
}

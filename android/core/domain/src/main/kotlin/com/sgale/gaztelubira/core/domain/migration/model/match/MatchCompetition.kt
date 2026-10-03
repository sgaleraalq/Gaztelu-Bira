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

package com.sgale.gaztelubira.core.domain.migration.model.match

import com.sgale.gaztelubira.core.domain.legacy.model.match.MatchType
import com.sgale.gaztelubira.core.domain.legacy.model.match.MatchType.CUP
import com.sgale.gaztelubira.core.domain.legacy.model.match.MatchType.LEAGUE

sealed interface MatchCompetition {
    data class League(
        val journey: Int
    ): MatchCompetition

    data class Cup(
        val name: String,
        val round: String?
    ): MatchCompetition

    companion object {
        fun MatchCompetition.asMatchType(): MatchType =
            when (this) {
                is Cup -> CUP
                is League -> LEAGUE
            }

        fun MatchCompetition.resolveJourney(): Int =
            when (this) {
                is Cup -> 0
                is League -> journey
            }

        fun MatchCompetition.resolveName(): String =
            when (this) {
                is Cup -> name
                is League -> ""
            }

        fun MatchCompetition.resolveRound(): String =
            when (this) {
                is Cup -> round.orEmpty()
                is League -> ""
            }
    }
}

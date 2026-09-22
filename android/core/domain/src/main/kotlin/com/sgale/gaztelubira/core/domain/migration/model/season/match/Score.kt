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

package com.sgale.gaztelubira.core.domain.migration.model.season.match

import com.sgale.gaztelubira.core.domain.legacy.model.match.MatchResult
import com.sgale.gaztelubira.core.domain.legacy.model.match.MatchResult.DEFEAT
import com.sgale.gaztelubira.core.domain.legacy.model.match.MatchResult.DRAW
import com.sgale.gaztelubira.core.domain.legacy.model.match.MatchResult.VICTORY
import com.sgale.gaztelubira.core.domain.legacy.model.match.MatchSide
import com.sgale.gaztelubira.core.domain.legacy.model.match.MatchSide.LOCAL

data class Score(
    val local: Int,
    val visitor: Int
) {
    fun goalsFor(side: MatchSide): Int = if (side == LOCAL) local else visitor

    fun goalsAgainst(side: MatchSide): Int = if (side == LOCAL) visitor else local

    fun resultFor(side: MatchSide): MatchResult = when {
        goalsFor(side) > goalsAgainst(side) -> VICTORY
        goalsFor(side) < goalsAgainst(side) -> DEFEAT
        else -> DRAW
    }
}

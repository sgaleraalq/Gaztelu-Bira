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

package com.sgale.gaztelubira.core.domain.model.match

import com.sgale.gaztelubira.core.domain.model.player.Player

data class MatchStats(
    val assists: List<Player>,
    val cleanSheets: List<Player>,
    val fails: List<Player>,
    val goals: List<Player>,
    val goalsProvoked: List<Player>,
    val penaltiesProvoked: List<Player>,
    val redCards: List<Player>,
    val saves: List<Player>,
    val yellowCards: List<Player>
) {
    companion object {
        val EMPTY_MATCH_STATS = MatchStats(
            assists = emptyList(),
            cleanSheets = emptyList(),
            fails = emptyList(),
            goals = emptyList(),
            goalsProvoked = emptyList(),
            penaltiesProvoked = emptyList(),
            redCards = emptyList(),
            saves = emptyList(),
            yellowCards = emptyList()
        )
    }
}

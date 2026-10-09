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

package com.sgale.gaztelubira.core.screens.home.tabs.stats

import com.sgale.gaztelubira.core.domain.model.match.MatchId
import com.sgale.gaztelubira.core.domain.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.model.season.squad.SquadPlayer

internal data class SeasonStats(
    val squad: List<SquadPlayer>,
    val stats: Map<PlayerId, List<PlayerStats>>,
    val lastMatchId: MatchId?
) {
    companion object {
        val EMPTY = SeasonStats(
            squad = emptyList(),
            stats = emptyMap(),
            lastMatchId = null
        )
    }
}

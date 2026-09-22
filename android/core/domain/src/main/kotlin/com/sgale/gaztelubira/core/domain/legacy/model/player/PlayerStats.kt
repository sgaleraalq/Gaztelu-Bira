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

package com.sgale.gaztelubira.core.domain.legacy.model.player

import androidx.compose.runtime.Immutable
import com.sgale.gaztelubira.core.domain.legacy.model.player.Player.Companion.ERROR_PLAYER
import com.sgale.gaztelubira.core.domain.legacy.model.stats.Stats
import com.sgale.gaztelubira.core.domain.legacy.model.utils.FirebaseId

@Immutable
data class PlayerStats(
    val id: FirebaseId,
    val player: Player,
    val stats: Map<FirebaseId, Stats>, // Map match to stats
    val percentage: Double = 0.0
) {
    companion object {
        val ERROR_PLAYER_STATS = PlayerStats(
            id = "",
            player = ERROR_PLAYER,
            stats = emptyMap()
        )
    }
}

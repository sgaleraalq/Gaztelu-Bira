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

package com.sgale.gaztelubira.multiplatform.ui.detail.player

import com.sgale.gaztelubira.multiplatform.model.GBPlayer

data class PlayerDetailUiState(
    val isManager: Boolean = false,
    val player: GBPlayer = GBPlayer(),
    val winRate: PlayerWinRate = PlayerWinRate(),
    val stats: PlayerStats? = null
) {
    data class PlayerWinRate(
        val wins: Int = 0,
        val draws: Int = 0,
        val loses: Int = 0
    )

    data class PlayerStats(
        val tbd: String
    )
}

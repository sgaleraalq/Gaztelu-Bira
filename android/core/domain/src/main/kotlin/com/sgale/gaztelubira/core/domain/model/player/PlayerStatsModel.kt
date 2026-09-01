/*
 * Designed and developed by 2025 sgaleraalq (Sergio Galera)
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

package com.sgale.gaztelubira.core.domain.model.player

import androidx.compose.runtime.Stable
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId

@Stable
data class PlayerStatsModel(
    val id: FirebaseId,
    val player: PlayerModel,
    val stats: Map<FirebaseId, Stats>, // Map match to stats
    val percentage: Double = 0.0
)

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

package com.sgale.gaztelubira.core.domain.model.match

import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId

data class MatchStatsModel(
    val id: FirebaseId,
    val location: String,
    val description: String,
    val matchModel: MatchModel,
    val formation: String,
    val lineUpPlayers: Map<Int, PlayerModel?>,
    val benchPlayers: List<PlayerModel>,
    val managers: List<PlayerModel>,
    val stats: MatchStats
)

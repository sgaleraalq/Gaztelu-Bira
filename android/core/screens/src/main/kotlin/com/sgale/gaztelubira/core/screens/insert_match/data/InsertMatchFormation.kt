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

package com.sgale.gaztelubira.core.screens.insert_match.data

import androidx.compose.runtime.Stable
import com.sgale.gaztelubira.core.designsystem.model.LineUpFormation
import com.sgale.gaztelubira.core.designsystem.model.LineUpFormation.FourThreeThree
import com.sgale.gaztelubira.core.designsystem.model.LineUpPosition
import com.sgale.gaztelubira.core.designsystem.model.LineUpPosition.Goalkeeper
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchFormation.ManagerPosition.First
import com.sgale.gaztelubira.core.screens.insert_match.data.PlayerState.LineUp

@Stable
data class InsertMatchFormation(
    val state: PlayerState = LineUp,
    val formation: LineUpFormation = FourThreeThree,
    val lineUp: Map<Int, PlayerModel?> = (0..10).associateWith { null },
    val benchPlayers: List<PlayerModel> = emptyList(),
    val managers: Pair<PlayerModel?, PlayerModel?> = Pair(null, null),
    val selectedPosition: LineUpPosition = Goalkeeper,
    val selectedManager: ManagerPosition = First
) {
    enum class ManagerPosition {
        First, Second
    }

    fun getLineUpPlayers() = lineUp
}

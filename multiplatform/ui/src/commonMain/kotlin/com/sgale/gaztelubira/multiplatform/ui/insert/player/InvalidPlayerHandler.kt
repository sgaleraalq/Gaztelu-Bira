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

package com.sgale.gaztelubira.multiplatform.ui.insert.player

import com.sgale.gaztelubira.multiplatform.ui.insert.InvalidInformationHandler
import com.sgale.gaztelubira.multiplatform.ui.insert.InvalidInformationReason
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.invalid_player_dorsal
import com.sgale.gaztelubira.multiplatform.ui.resources.invalid_player_name
import com.sgale.gaztelubira.multiplatform.ui.resources.invalid_player_position

internal class InvalidPlayerHandler(
    state: InsertPlayerUiState
) : InvalidInformationHandler<InsertPlayerUiState>(state) {
    override val requiredFields: List<InvalidInformationReason> = listOfNotNull(
        InvalidInformationReason(
            value = state.playerName,
            reason = Res.string.invalid_player_name
        ),
        InvalidInformationReason(
            value = state.playerPosition,
            reason = Res.string.invalid_player_position
        ),
        InvalidInformationReason(
            value = state.playerDorsal.takeIf { it > 0 }?.toString().orEmpty(),
            reason = Res.string.invalid_player_dorsal
        )
    )
}

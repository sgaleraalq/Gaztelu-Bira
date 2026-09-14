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

package com.sgale.gaztelubira.multiplatform.ui.insert.player

import com.sgale.gaztelubira.multiplatform.ui.insert.InsertDataUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Default
import com.sgale.gaztelubira.multiplatform.ui.insert.InvalidInformationHandler
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.None
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.PictureType
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.PictureType.Face
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.PlayerPosition

data class InsertPlayerUiState(
    val playerId: String,
    val playerName: String = "",
    val dorsal: Int = 0,
    val position: PlayerPosition? = null,
    val faceImage: String = "",
    val bodyImage: String = "",
    /** Which box a picked picture lands in — set when the user taps one of the two rows. */
    val selectedPicture: PictureType = Face,
    val availableDorsals: List<Int> = emptyList(),
    val dialog: InsertPlayerDialog = None,
    override val state: InsertingDataState = Default
) : InsertDataUiState {
    override val handler: InvalidInformationHandler
        get() = InvalidPlayerHandler(this)

    internal fun imageOf(type: PictureType): String = when (type) {
        Face -> faceImage
        PictureType.Body -> bodyImage
    }
}

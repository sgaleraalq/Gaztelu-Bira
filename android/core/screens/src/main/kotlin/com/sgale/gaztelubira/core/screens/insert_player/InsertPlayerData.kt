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

package com.sgale.gaztelubira.core.screens.insert_player

import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.Position
import com.sgale.gaztelubira.core.domain.utils.CommonImage
import com.sgale.gaztelubira.core.domain.utils.getActualTimeAsLong
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerData.PictureType.Face

data class InsertPlayerData(
    val id: String = getActualTimeAsLong().toString(),
    val name: String = "",
    val dorsal: Int = 0,
    val position: Position? = null,
    val faceImage: CommonImage? = null,
    val bodyImage: CommonImage? = null,
    val lastSelected: PictureType = Face,
    val useSameImage: Boolean = false,
    val canInsert: Boolean = true
) {
    enum class PictureType { Face, Body }

    fun toPlayerModel(): PlayerModel =
        PlayerModel(
            id = id,
            name = name,
            dorsal = dorsal,
            position = position,
            faceImage = "",
            bodyImage = ""
        )

    fun notManager(): Boolean =
        position != null && position != Position.Manager
}

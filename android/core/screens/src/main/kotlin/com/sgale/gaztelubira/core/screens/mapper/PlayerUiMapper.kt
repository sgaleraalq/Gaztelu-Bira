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

package com.sgale.gaztelubira.core.screens.mapper

import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.model.player.Position
import com.sgale.gaztelubira.core.domain.model.player.Position.DEFENDER
import com.sgale.gaztelubira.core.domain.model.player.Position.FORWARD
import com.sgale.gaztelubira.core.domain.model.player.Position.GOALKEEPER
import com.sgale.gaztelubira.core.domain.model.player.Position.MANAGER
import com.sgale.gaztelubira.core.domain.model.player.Position.MIDFIELDER
import com.sgale.gaztelubira.core.domain.model.player.Position.UNDEFINED
import com.sgale.gaztelubira.core.domain.model.season.squad.SquadPlayer
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBPosition

internal object PlayerUiMapper {
    fun Player.toGBPlayer(dorsal: Int? = null): GBPlayer =
        GBPlayer(
            id = id.value,
            name = displayName(),
            image = faceImage,
            dorsal = dorsal
        )

    fun SquadPlayer.toGBPlayer(): GBPlayer = player.toGBPlayer(dorsal)

    /**
     * For ids that point to a player that may not be loaded, it still shows the id
     */
    fun PlayerId.toGBPlayer(players: Map<PlayerId, Player>): GBPlayer =
        players[this]?.toGBPlayer() ?: GBPlayer(id = value)

    fun Player.displayName(): String = nickname ?: name

    fun Position.toGBPosition(): GBPosition =
        when (this) {
            UNDEFINED -> GBPosition.UNDEFINED
            MANAGER -> GBPosition.MANAGER
            GOALKEEPER -> GBPosition.GOALKEEPER
            DEFENDER -> GBPosition.DEFENDER
            MIDFIELDER -> GBPosition.MIDFIELDER
            FORWARD -> GBPosition.FORWARD
        }
}

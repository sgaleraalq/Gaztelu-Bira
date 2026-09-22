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

package com.sgale.gaztelubira.core.network.firebase.response.player

import com.sgale.gaztelubira.core.domain.legacy.model.player.Player
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position
import com.sgale.gaztelubira.core.network.NetworkMapper

internal object PlayerMapper: NetworkMapper<Player, PlayerResponse> {
    override fun Player.asResponse() = 
        PlayerResponse(
            id = id,
            name = name,
            dorsal = dorsal,
            position = position.name,
            faceImage = faceImage.orEmpty(),
            bodyImage = bodyImage.orEmpty()
        )

    override fun PlayerResponse.asModel() =
        Player(
            id = id,
            name = name,
            dorsal = dorsal,
            position = Position.valueOf(position),
            faceImage = faceImage.ifBlank { null },
            bodyImage = bodyImage.ifBlank { null }
        )
}

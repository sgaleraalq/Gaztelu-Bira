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

package com.sgale.gaztelubira.core.network.firebase.response.migrations.player

import com.google.firebase.Timestamp
import com.sgale.gaztelubira.core.domain.migration.model.player.Player
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId
import com.sgale.gaztelubira.core.network.firebase.response.migrations.NetworkMapper
import java.util.Date

internal object PlayerMapper: NetworkMapper<Player, PlayerResponse> {
    override fun Player.asResponse() =
        PlayerResponse(
            name = name,
            bodyImage = bodyImage.orEmpty(),
            faceImage = faceImage.orEmpty(),
            updatedAt = Timestamp(Date())
        )

    override fun PlayerResponse.asModel(id: PlayerId) =
        Player(
            id = id,
            name = name,
            bodyImage = bodyImage,
            faceImage = faceImage
        )
}

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

package com.sgale.gaztelubira.core.data.mappers

import com.sgale.gaztelubira.core.data.db.entities.PlayerEntity
import com.sgale.gaztelubira.core.data.network.response.PlayerResponse
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.Position.Companion.mapPosition

object PlayerMapper :
    Mapper<PlayerResponse, PlayerModel, PlayerEntity>
{
    override fun asResponse(domain: PlayerModel) =
        PlayerResponse(
            id = domain.id,
            name = domain.name,
            dorsal = domain.dorsal,
            position = domain.position?.name ?: "",
            faceImage = domain.faceImage ?: "",
            bodyImage = domain.bodyImage ?: ""
        )

    override fun asEntity(domain: PlayerModel) =
        PlayerEntity(
            id = domain.id,
            name = domain.name,
            dorsal = domain.dorsal,
            position = domain.position,
            faceImage = domain.faceImage ?: "",
            bodyImage = domain.bodyImage ?: ""
        )

    override fun entityAsDomain(entity: PlayerEntity) =
        PlayerModel(
            id = entity.id,
            name = entity.name,
            dorsal = entity.dorsal,
            position = entity.position,
            faceImage = entity.faceImage.ifBlank { null },
            bodyImage = entity.bodyImage.ifBlank { null }
        )

    override fun responseAsModel(response: PlayerResponse): PlayerModel =
        PlayerModel(
            id = response.id,
            name = response.name,
            dorsal = response.dorsal,
            position = mapPosition(response.position),
            faceImage = response.faceImage.ifBlank { null },
            bodyImage = response.bodyImage.ifBlank { null }
        )
}

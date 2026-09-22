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

package com.sgale.gaztelubira.core.network.migration.firebase.season.squad

import com.sgale.gaztelubira.core.domain.legacy.model.player.Position
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.migration.model.season.squad.SeasonPlayer

/**
 * This one does not implement [com.sgale.gaztelubira.core.network.migration.firebase.NetworkMapper]:
 * a squad row is identified by two ids, the season it belongs to and the player it is about,
 * and both come from the path rather than from the document.
 */
internal object SeasonPlayerMapper {
    fun SeasonPlayerResponse.asModel(
        seasonId: SeasonId,
        playerId: PlayerId
    ) = SeasonPlayer(
        seasonId = seasonId,
        id = playerId,
        dorsal = dorsal,
        position = Position.mapPosition(position)
    )
}

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

package com.sgale.gaztelubira.core.domain.repository

import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.model.season.squad.SeasonPlayer

interface PlayerRemote {
    suspend fun fetchPlayer(id: PlayerId): Player?
    suspend fun fetchPlayers(): List<Player>

    /**
     * When the players last changed, in epoch millis; null if nothing ever stamped them.
     */
    suspend fun fetchPlayersChangedAt(): Long?
    suspend fun playerExists(id: PlayerId): Boolean

    /**
     * Writes the player and their squad entry for [SeasonPlayer.seasonId] atomically,
     * so a player never ends up created but missing from the season.
     */
    suspend fun insertPlayer(player: Player, seasonPlayer: SeasonPlayer)
}

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

package com.sgale.gaztelubira.core.domain.migration.repository.player

import com.sgale.gaztelubira.core.domain.migration.model.player.Player
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId

/**
 * Players as they live in the remote source. Kept apart from [PlayerLocal]
 * because a local-first app decides on purpose where it reads and where it
 * writes — that decision belongs to the use case, not to a repository that
 * hides both.
 */
interface PlayerRemote {
    /** @return null when no player has that id. */
    suspend fun fetchPlayer(id: PlayerId): Player?

    suspend fun fetchPlayers(): List<Player>
}

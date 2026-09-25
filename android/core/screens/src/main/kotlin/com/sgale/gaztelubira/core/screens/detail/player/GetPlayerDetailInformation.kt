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

package com.sgale.gaztelubira.core.screens.detail.player

import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.migration.repository.player.PlayerLocal
import com.sgale.gaztelubira.core.domain.migration.repository.season.SeasonLocal
import com.sgale.gaztelubira.core.domain.migration.usecase.season.SelectedSeason
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetPlayerDetailInformation @Inject constructor(
    private val selectedSeason: SelectedSeason,
    private val playerLocal: PlayerLocal,
    private val seasonLocal: SeasonLocal,
) {
    suspend operator fun invoke(
        playerId: PlayerId
    ): PlayerDetailState? {
        val seasonId = selectedSeason().first() ?: return null
        val player = playerLocal.getPlayer(playerId)
        val seasonPlayer = seasonLocal.getSeasonPlayer(playerId, seasonId)
        return PlayerDetailState(player, seasonPlayer)
    }
}

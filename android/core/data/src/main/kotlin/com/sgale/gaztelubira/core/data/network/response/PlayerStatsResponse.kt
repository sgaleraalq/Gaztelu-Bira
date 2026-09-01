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

package com.sgale.gaztelubira.core.data.network.response

import com.sgale.gaztelubira.core.domain.model.player.Stats
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PlayerStatsResponse(
    val assists: Int = 0,
    val cleanSheets: Int = 0,
    val fails: Int = 0,
    val gamesPlayed: Int = 0,
    val goals: Int = 0,
    val goalsProvoked: Int = 0,
    val penaltiesProvoked: Int = 0,
    val redCards: Int = 0,
    val saves: Int = 0,
    val yellowCards: Int = 0
) {
    fun asStats() = Stats(
        assists = assists,
        cleanSheets = cleanSheets,
        fails = fails,
        gamesPlayed = gamesPlayed,
        goals = goals,
        goalsProvoked = goalsProvoked,
        penaltiesProvoked = penaltiesProvoked,
        redCards = redCards,
        saves = saves,
        yellowCards = yellowCards
    )
}

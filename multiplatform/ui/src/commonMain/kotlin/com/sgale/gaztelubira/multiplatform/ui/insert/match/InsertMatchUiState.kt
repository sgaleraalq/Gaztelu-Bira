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

package com.sgale.gaztelubira.multiplatform.ui.insert.match

import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation.FOUR_FOUR_TWO
import com.sgale.gaztelubira.multiplatform.model.GBMatchType
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.LEAGUE
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.model.GBStat.BLUE_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBStat.CLEAN_SHEETS
import com.sgale.gaztelubira.multiplatform.model.GBStat.GAMES_PLAYED
import com.sgale.gaztelubira.multiplatform.model.GBStat.PERCENTAGE
import com.sgale.gaztelubira.multiplatform.model.GBStat.RED_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBStat.YELLOW_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBTeam
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertDataUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Default
import com.sgale.gaztelubira.multiplatform.ui.insert.InvalidInformationHandler
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog.None
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.Main
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot.Bench
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot.LineUp
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot.Manager

internal const val BENCH_LIMIT = 6
internal const val MANAGERS_LIMIT = 2
private const val MAX_YELLOW_CARDS = 2

/**
 * The stats inserted player by player. Percentage and games played are worked out from the rest.
 */
val INSERTABLE_STATS: List<GBStat> = GBStat.entries - listOf(PERCENTAGE, GAMES_PLAYED)

/**
 * @param team The app's own side; [rival] is the other one, picked from [rivals].
 * @param squad Players that can be called up, [staff] the managers.
 * @param lineUp Players on the pitch, by their index in [formation].
 * @param stats Each stat with one entry per time a player got it, so a brace is the player twice.
 */
data class InsertMatchUiState(
    val date: String = "",
    val type: GBMatchType = LEAGUE,
    val journey: Int = 1,
    val cupName: String = "",
    val cupRound: String = "",
    val isLocal: Boolean = true,
    val team: GBTeam? = null,
    val rival: GBTeam? = null,
    val localGoals: Int? = null,
    val visitorGoals: Int? = null,
    val rivals: List<GBTeam> = emptyList(),
    val squad: List<GBPlayer> = emptyList(),
    val staff: List<GBPlayer> = emptyList(),
    val formation: LineUpFormation = FOUR_FOUR_TWO,
    val lineUp: Map<Int, GBPlayer> = emptyMap(),
    val bench: List<GBPlayer> = emptyList(),
    val managers: List<GBPlayer> = emptyList(),
    val location: String = "",
    val description: String = "",
    val stats: Map<GBStat, List<GBPlayer>> = emptyMap(),
    val section: InsertMatchSection = Main,
    val dialog: InsertMatchDialog = None,
    override val state: InsertingDataState = Default
) : InsertDataUiState {
    override val handler: InvalidInformationHandler<*>
        get() = InvalidMatchHandler(this)

    val localTeam: GBTeam?
        get() = if (isLocal) team else rival

    val visitorTeam: GBTeam?
        get() = if (isLocal) rival else team

    val goalsFor: Int?
        get() = if (isLocal) localGoals else visitorGoals

    /**
     * Only who was called up can get a stat.
     */
    val calledUp: List<GBPlayer>
        get() = lineUp.values + bench

    fun stat(stat: GBStat): List<GBPlayer> = stats[stat].orEmpty()

    /**
     * Nobody can be in two places of the same match.
     */
    fun availableFor(slot: PlayerSlot): List<GBPlayer> {
        val taken = (lineUp.values + bench + managers).mapTo(HashSet()) { it.id }
        return when (slot) {
            is LineUp, Bench -> squad
            Manager -> staff
        }.filterNot { it.id in taken }
    }

    fun availableFor(stat: GBStat): List<GBPlayer> {
        val given = stat(stat)
        return calledUp.filter { player ->
            val times = given.count { it.id == player.id }
            when (stat) {
                CLEAN_SHEETS, RED_CARDS, BLUE_CARDS -> times == 0
                YELLOW_CARDS -> times < MAX_YELLOW_CARDS
                else -> true
            }
        }
    }
}

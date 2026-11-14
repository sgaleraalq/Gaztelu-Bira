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
import com.sgale.gaztelubira.multiplatform.model.GBTeam
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertDataUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Default
import com.sgale.gaztelubira.multiplatform.ui.insert.InvalidInformationHandler
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog.None
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.INFORMATION
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.LINE_UP

/**
 * @param journey The league matchday this match would be, worked out by whoever owns the data.
 * @param team Gaztelu Bira, whichever side it plays on.
 * @param rivals The teams the rival can be picked from.
 * @param teamGoals Gaztelu Bira's goals as typed: empty until the user fills them in, so "not
 * typed yet" and "zero" stay apart.
 * @param date Midnight UTC of the match day, in milliseconds. Null until one is picked.
 * @param formations The formations on offer. The platform decides, since it knows which ones it
 * can store.
 * @param lineUp The starting eleven by slot of [formation]. A slot nobody fills is absent.
 * @param players The squad that can be called up, staff aside.
 * @param staff The coaches that can be called up.
 * @param stats Who did what. A player listed twice under a stat did it twice.
 */
data class InsertMatchUiState(
    val seasonName: String = "",
    val journey: Int = 0,
    val type: GBMatchType = LEAGUE,
    val cupName: String = "",
    val cupRound: String = "",
    val isLocal: Boolean = true,
    val team: GBTeam? = null,
    val rival: GBTeam? = null,
    val rivals: List<GBTeam> = emptyList(),
    val teamGoals: String = "",
    val rivalGoals: String = "",
    val date: Long? = null,
    val hour: Int = DEFAULT_HOUR,
    val minute: Int = 0,
    val location: String = "",
    val description: String = "",
    val formation: LineUpFormation = FOUR_FOUR_TWO,
    val formations: List<LineUpFormation> = LineUpFormation.entries,
    val lineUp: Map<Int, GBPlayer> = emptyMap(),
    val bench: List<GBPlayer> = emptyList(),
    val managers: List<GBPlayer> = emptyList(),
    val players: List<GBPlayer> = emptyList(),
    val staff: List<GBPlayer> = emptyList(),
    val stats: Map<GBStat, List<GBPlayer>> = emptyMap(),
    val expanded: Set<InsertMatchSection> = setOf(INFORMATION, LINE_UP),
    val dialog: InsertMatchDialog = None,
    override val state: InsertingDataState = Default
) : InsertDataUiState {

    override val handler: InvalidInformationHandler<*>
        get() = InvalidMatchHandler(this)

    /**
     * Everyone called up to play, starters first: the only ones a stat can be given to.
     */
    val calledUp: List<GBPlayer>
        get() = formation.positions.indices.mapNotNull { lineUp[it] } + bench

    val isLineUpComplete: Boolean
        get() = formation.positions.indices.all { it in lineUp }

    companion object {
        const val DEFAULT_HOUR = 12

        /**
         * The stats that can be given by hand. The rest of [GBStat] are worked out from these.
         */
        val INSERTABLE_STATS = listOf(
            GBStat.GOALS,
            GBStat.ASSISTS,
            GBStat.GOALS_PROVOKED,
            GBStat.PENALTIES_PROVOKED,
            GBStat.SAVES,
            GBStat.CLEAN_SHEETS,
            GBStat.FAILS,
            GBStat.YELLOW_CARDS,
            GBStat.RED_CARDS,
            GBStat.BLUE_CARDS
        )
    }
}

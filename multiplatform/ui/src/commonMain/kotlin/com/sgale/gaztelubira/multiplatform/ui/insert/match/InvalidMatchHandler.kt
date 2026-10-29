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

import com.sgale.gaztelubira.multiplatform.model.GBMatchType.CUP
import com.sgale.gaztelubira.multiplatform.model.GBStat.GOALS
import com.sgale.gaztelubira.multiplatform.model.GBStat.GOALS_PROVOKED
import com.sgale.gaztelubira.multiplatform.ui.insert.InvalidInformationHandler
import com.sgale.gaztelubira.multiplatform.ui.insert.InvalidInformationReason
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_invalid_cup_name
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_invalid_goals
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_invalid_lineup
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_invalid_rival
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_invalid_score

/**
 * [InvalidInformationReason] treats a blank value as missing, so the checks that are not a text
 * field hand in either this or nothing.
 */
private const val FILLED = "filled"

internal class InvalidMatchHandler(
    state: InsertMatchUiState
) : InvalidInformationHandler<InsertMatchUiState>(state) {
    override val requiredFields: List<InvalidInformationReason> = listOfNotNull(
        InvalidInformationReason(
            value = state.rival?.id.orEmpty(),
            reason = Res.string.insert_match_invalid_rival
        ),
        InvalidInformationReason(
            value = filledIf(state.localGoals != null && state.visitorGoals != null),
            reason = Res.string.insert_match_invalid_score
        ),
        InvalidInformationReason(
            value = state.cupName,
            reason = Res.string.insert_match_invalid_cup_name
        ).takeIf { state.type == CUP },
        InvalidInformationReason(
            value = filledIf(state.lineUp.size == state.formation.positions.size),
            reason = Res.string.insert_match_invalid_lineup
        ),
        InvalidInformationReason(
            value = filledIf(state.goalsAddUp()),
            reason = Res.string.insert_match_invalid_goals
        )
    )

    private fun filledIf(condition: Boolean): String = if (condition) FILLED else ""

    /**
     * Every goal of the app's side belongs to someone: a scorer, or a player who provoked it.
     */
    private fun InsertMatchUiState.goalsAddUp(): Boolean {
        val scored = goalsFor ?: return true
        return stat(GOALS).size + stat(GOALS_PROVOKED).size == scored
    }
}

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
import com.sgale.gaztelubira.multiplatform.ui.insert.InvalidInformationHandler
import com.sgale.gaztelubira.multiplatform.ui.insert.InvalidInformationReason
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_invalid_cup_name
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_invalid_date
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_invalid_line_up
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_invalid_rival
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_invalid_score

private const val FILLED = "filled"

/**
 * A field counts as missing when its value is blank, so the ones that are not text are turned into
 * one: blank while they are missing, anything else once they are there.
 */
internal class InvalidMatchHandler(
    state: InsertMatchUiState
) : InvalidInformationHandler<InsertMatchUiState>(state) {

    override val requiredFields: List<InvalidInformationReason> = listOfNotNull(
        InvalidInformationReason(
            value = state.rival?.id.orEmpty(),
            reason = Res.string.insert_match_invalid_rival
        ),
        InvalidInformationReason(
            value = state.cupName,
            reason = Res.string.insert_match_invalid_cup_name
        ).takeIf { state.type == CUP },
        InvalidInformationReason(
            value = if (state.teamGoals.isBlank()) "" else state.rivalGoals,
            reason = Res.string.insert_match_invalid_score
        ),
        InvalidInformationReason(
            value = state.date?.toString().orEmpty(),
            reason = Res.string.insert_match_invalid_date
        ),
        InvalidInformationReason(
            value = if (state.isLineUpComplete) FILLED else "",
            reason = Res.string.insert_match_invalid_line_up
        )
    )
}

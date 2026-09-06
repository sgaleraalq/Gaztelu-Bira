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

package com.sgale.gaztelubira.multiplatform.model

import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.ASSISTS
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.CLEAN_SHEETS
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.FAILS
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.GOALS
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.MIN_RED_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.MIN_YELLOW_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.PENALTIES_PROVOKED
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.SAVES

data class GBPunctuation(
    val goals: Int = 1,
    val assists: Int = 1,
    val cleanSheets: Int = 1,
    val penaltiesProvoked: Int = 1,
    val saves: Int = 1,
    val fails: Int = -1,
    val minYellowCards: Int = 5,
    val minRedCards: Int = 1
) {
    enum class PunctuationField {
        GOALS,
        ASSISTS,
        CLEAN_SHEETS,
        PENALTIES_PROVOKED,
        SAVES,
        FAILS,
        MIN_YELLOW_CARDS,
        MIN_RED_CARDS
    }

    fun updateValue(value: Int, field: PunctuationField): GBPunctuation =
        when (field) {
            GOALS -> copy(goals = value)
            ASSISTS -> copy(assists = value)
            CLEAN_SHEETS -> copy(cleanSheets = value)
            PENALTIES_PROVOKED -> copy(penaltiesProvoked = value)
            SAVES -> copy(saves = value)
            FAILS -> copy(fails = value)
            MIN_YELLOW_CARDS -> copy(minYellowCards = value)
            MIN_RED_CARDS -> copy(minRedCards = value)
        }
}

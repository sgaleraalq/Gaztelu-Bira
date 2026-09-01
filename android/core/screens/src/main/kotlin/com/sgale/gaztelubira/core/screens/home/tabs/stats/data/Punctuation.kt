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

package com.sgale.gaztelubira.core.screens.home.tabs.stats.data

import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.Assists
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.CleanSheets
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.Fails
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.Goals
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.MinRedCards
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.MinYellowCards
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.PenaltiesProvoked
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.Saves

data class Punctuation(
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
        Goals,
        Assists,
        CleanSheets,
        PenaltiesProvoked,
        Saves,
        Fails,
        MinYellowCards,
        MinRedCards
    }

    fun updateValue(
        value: Int,
        field: PunctuationField
    ): Punctuation {
        return when (field) {
            Goals -> this.copy(goals = value)
            Assists -> this.copy(assists = value)
            CleanSheets -> this.copy(cleanSheets = value)
            PenaltiesProvoked -> this.copy(penaltiesProvoked = value)
            Saves -> this.copy(saves = value)
            Fails -> this.copy(fails = value)
            MinYellowCards -> this.copy(minYellowCards = value)
            MinRedCards -> this.copy(minRedCards = value)
        }
    }
}

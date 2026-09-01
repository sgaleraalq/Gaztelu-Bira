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

package com.sgale.gaztelubira.core.domain.model.match

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.sgale.gaztelubira.core.domain.R
import com.sgale.gaztelubira.core.domain.model.utils.GBConstants.CUP
import com.sgale.gaztelubira.core.domain.model.utils.GBConstants.LEAGUE

enum class MatchType(
    @StringRes val type: Int,
    @DrawableRes val icon: Int
) {
    League(R.string.league, R.drawable.ic_league),
    Cup(R.string.cup, R.drawable.ic_cup);

    fun asString(): String {
        return when(this){
            League -> LEAGUE
            Cup -> CUP
        }
    }

    companion object {
        fun fromString(name: String): MatchType {
            return when(name) {
                LEAGUE -> League
                CUP -> Cup
                else -> League
            }
        }
    }
}

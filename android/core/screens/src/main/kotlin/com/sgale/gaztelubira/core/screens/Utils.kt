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

package com.sgale.gaztelubira.core.screens

import android.content.Context
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult.DEFEAT
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult.DRAW
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult.VICTORY
import com.sgale.gaztelubira.core.domain.migration.model.team.Team
import com.sgale.gaztelubira.multiplatform.model.GBMatchResult
import com.sgale.gaztelubira.multiplatform.model.GBTeam

internal fun showToast(
    context: Context,
    msg: String
) {
    makeText(context, msg, LENGTH_SHORT).show()
}

internal fun MatchResult.toGBMatchResult() =
    when (this) {
        DEFEAT -> GBMatchResult.DEFEAT
        DRAW -> GBMatchResult.DRAW
        VICTORY -> GBMatchResult.VICTORY
    }

internal fun Team.toGBTeam(): GBTeam =
    GBTeam(
        id = id.value,
        name = name,
        logo = logo
    )

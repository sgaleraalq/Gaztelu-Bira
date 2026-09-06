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

import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.assists
import com.sgale.gaztelubira.multiplatform.ui.resources.clean_sheets
import com.sgale.gaztelubira.multiplatform.ui.resources.fails
import com.sgale.gaztelubira.multiplatform.ui.resources.games_played
import com.sgale.gaztelubira.multiplatform.ui.resources.goals
import com.sgale.gaztelubira.multiplatform.ui.resources.goals_provoked
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_assists
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_clean_sheets
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_fail
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_games_played
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_goal
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_goal_provoked
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_penalties
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_percentage
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_red_card
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_saves
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_yellow_card
import com.sgale.gaztelubira.multiplatform.ui.resources.penalties_provoked
import com.sgale.gaztelubira.multiplatform.ui.resources.percentage
import com.sgale.gaztelubira.multiplatform.ui.resources.red_cards
import com.sgale.gaztelubira.multiplatform.ui.resources.saves
import com.sgale.gaztelubira.multiplatform.ui.resources.yellow_cards
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class GBStat(
    val label: StringResource,
    val icon: DrawableResource
) {
    PERCENTAGE(
        label = Res.string.percentage,
        icon = Res.drawable.ic_percentage
    ),
    GOALS(
        label = Res.string.goals,
        icon = Res.drawable.ic_goal
    ),
    GOALS_PROVOKED(
        label = Res.string.goals_provoked,
        icon = Res.drawable.ic_goal_provoked
    ),
    ASSISTS(
        label = Res.string.assists,
        icon = Res.drawable.ic_assists
    ),
    CLEAN_SHEETS(
        label = Res.string.clean_sheets,
        icon = Res.drawable.ic_clean_sheets
    ),
    PENALTIES_PROVOKED(
        label = Res.string.penalties_provoked,
        icon = Res.drawable.ic_penalties
    ),
    SAVES(
        label = Res.string.saves,
        icon = Res.drawable.ic_saves
    ),
    FAILS(
        label = Res.string.fails,
        icon = Res.drawable.ic_fail
    ),
    YELLOW_CARDS(
        label = Res.string.yellow_cards,
        icon = Res.drawable.ic_yellow_card
    ),
    RED_CARDS(
        label = Res.string.red_cards,
        icon = Res.drawable.ic_red_card
    ),
    GAMES_PLAYED(
        label = Res.string.games_played,
        icon = Res.drawable.ic_games_played
    )
}

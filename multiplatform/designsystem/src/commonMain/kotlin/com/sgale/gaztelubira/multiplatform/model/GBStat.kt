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

package com.sgale.gaztelubira.multiplatform.model

import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.common_assists
import com.sgale.gaztelubira.multiplatform.ui.resources.common_blue_cards
import com.sgale.gaztelubira.multiplatform.ui.resources.common_clean_sheets
import com.sgale.gaztelubira.multiplatform.ui.resources.common_fails
import com.sgale.gaztelubira.multiplatform.ui.resources.common_games_played
import com.sgale.gaztelubira.multiplatform.ui.resources.common_goals
import com.sgale.gaztelubira.multiplatform.ui.resources.common_goals_provoked
import com.sgale.gaztelubira.multiplatform.ui.resources.common_penalties_provoked
import com.sgale.gaztelubira.multiplatform.ui.resources.common_percentage
import com.sgale.gaztelubira.multiplatform.ui.resources.common_red_cards
import com.sgale.gaztelubira.multiplatform.ui.resources.common_saves
import com.sgale.gaztelubira.multiplatform.ui.resources.common_yellow_cards
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_assists
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_blue_card
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
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class GBStat(
    val label: StringResource,
    val icon: DrawableResource
) {
    PERCENTAGE(
        label = Res.string.common_percentage,
        icon = Res.drawable.ic_percentage
    ),
    GOALS(
        label = Res.string.common_goals,
        icon = Res.drawable.ic_goal
    ),
    GOALS_PROVOKED(
        label = Res.string.common_goals_provoked,
        icon = Res.drawable.ic_goal_provoked
    ),
    ASSISTS(
        label = Res.string.common_assists,
        icon = Res.drawable.ic_assists
    ),
    CLEAN_SHEETS(
        label = Res.string.common_clean_sheets,
        icon = Res.drawable.ic_clean_sheets
    ),
    PENALTIES_PROVOKED(
        label = Res.string.common_penalties_provoked,
        icon = Res.drawable.ic_penalties
    ),
    SAVES(
        label = Res.string.common_saves,
        icon = Res.drawable.ic_saves
    ),
    FAILS(
        label = Res.string.common_fails,
        icon = Res.drawable.ic_fail
    ),
    YELLOW_CARDS(
        label = Res.string.common_yellow_cards,
        icon = Res.drawable.ic_yellow_card
    ),
    RED_CARDS(
        label = Res.string.common_red_cards,
        icon = Res.drawable.ic_red_card
    ),
    BLUE_CARDS(
        label = Res.string.common_blue_cards,
        icon = Res.drawable.ic_blue_card
    ),
    GAMES_PLAYED(
        label = Res.string.common_games_played,
        icon = Res.drawable.ic_games_played
    )
}

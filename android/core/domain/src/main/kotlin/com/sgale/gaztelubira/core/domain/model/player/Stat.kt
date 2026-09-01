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

package com.sgale.gaztelubira.core.domain.model.player

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import gbmultiplatform.domain.generated.resources.Res
import gbmultiplatform.domain.generated.resources.assists
import gbmultiplatform.domain.generated.resources.clean_sheets
import gbmultiplatform.domain.generated.resources.fails
import gbmultiplatform.domain.generated.resources.games_played
import gbmultiplatform.domain.generated.resources.goals
import gbmultiplatform.domain.generated.resources.goals_provoked
import gbmultiplatform.domain.generated.resources.ic_assists
import gbmultiplatform.domain.generated.resources.ic_clean_sheets
import gbmultiplatform.domain.generated.resources.ic_fail
import gbmultiplatform.domain.generated.resources.ic_games_played
import gbmultiplatform.domain.generated.resources.ic_goal
import gbmultiplatform.domain.generated.resources.ic_goal_provoked
import gbmultiplatform.domain.generated.resources.ic_penalties
import gbmultiplatform.domain.generated.resources.ic_percentage
import gbmultiplatform.domain.generated.resources.ic_red_card
import gbmultiplatform.domain.generated.resources.ic_saves
import gbmultiplatform.domain.generated.resources.ic_yellow_card
import gbmultiplatform.domain.generated.resources.insert_assists_players
import gbmultiplatform.domain.generated.resources.insert_clean_sheet_players
import gbmultiplatform.domain.generated.resources.insert_fails_players
import gbmultiplatform.domain.generated.resources.insert_goal_players
import gbmultiplatform.domain.generated.resources.insert_goal_provoked_players
import gbmultiplatform.domain.generated.resources.insert_penalties_provoked_players
import gbmultiplatform.domain.generated.resources.insert_red_card_players
import gbmultiplatform.domain.generated.resources.insert_saves_players
import gbmultiplatform.domain.generated.resources.insert_yellow_card_players
import gbmultiplatform.domain.generated.resources.penalties_provoked
import gbmultiplatform.domain.generated.resources.percentage
import gbmultiplatform.domain.generated.resources.red_cards
import gbmultiplatform.domain.generated.resources.saves
import gbmultiplatform.domain.generated.resources.yellow_cards

enum class Stat(
    @StringRes val statName: Int,
    @StringRes val statTitle: Int?,
    @DrawableRes val icon: Int,
    val isInsertable: Boolean = true
) {
    Percentage(
        statName = Res.string.percentage,
        statTitle = null,
        icon = Res.drawable.ic_percentage,
        isInsertable = false
    ),
    Goals(
        statName = Res.string.goals,
        statTitle = Res.string.insert_goal_players,
        icon = Res.drawable.ic_goal
    ),
    GoalsProvoked(
        statName = Res.string.goals_provoked,
        statTitle = Res.string.insert_goal_provoked_players,
        icon = Res.drawable.ic_goal_provoked
    ),
    Assists(
        statName = Res.string.assists,
        statTitle = Res.string.insert_assists_players,
        icon = Res.drawable.ic_assists
    ),
    CleanSheets(
        statName = Res.string.clean_sheets,
        statTitle = Res.string.insert_clean_sheet_players,
        icon = Res.drawable.ic_clean_sheets
    ),
    PenaltiesProvoked(
        statName = Res.string.penalties_provoked,
        statTitle = Res.string.insert_penalties_provoked_players,
        icon = Res.drawable.ic_penalties
    ),
    Saves(
        statName = Res.string.saves,
        statTitle = Res.string.insert_saves_players,
        icon = Res.drawable.ic_saves
    ),
    Fails(
        statName = Res.string.fails,
        statTitle = Res.string.insert_fails_players,
        icon = Res.drawable.ic_fail
    ),
    YellowCards(
        statName = Res.string.yellow_cards,
        statTitle = Res.string.insert_yellow_card_players,
        icon = Res.drawable.ic_yellow_card
    ),
    RedCards(
        statName = Res.string.red_cards,
        statTitle = Res.string.insert_red_card_players,
        icon = Res.drawable.ic_red_card
    ),
    GamesPlayed(
        statName = Res.string.games_played,
        statTitle = null,
        icon = Res.drawable.ic_games_played,
        isInsertable = false
    )
}

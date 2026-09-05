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

package com.sgale.gaztelubira.core.domain.model.player

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.sgale.gaztelubira.core.domain.R

enum class Stat(
    @StringRes val statName: Int,
    @StringRes val statTitle: Int?,
    @DrawableRes val icon: Int,
    val isInsertable: Boolean = true
) {
    Percentage(
        statName = R.string.percentage,
        statTitle = null,
        icon = R.drawable.ic_percentage,
        isInsertable = false
    ),
    Goals(
        statName = R.string.goals,
        statTitle = R.string.insert_goal_players,
        icon = R.drawable.ic_goal
    ),
    GoalsProvoked(
        statName = R.string.goals_provoked,
        statTitle = R.string.insert_goal_provoked_players,
        icon = R.drawable.ic_goal_provoked
    ),
    Assists(
        statName = R.string.assists,
        statTitle = R.string.insert_assists_players,
        icon = R.drawable.ic_assists
    ),
    CleanSheets(
        statName = R.string.clean_sheets,
        statTitle = R.string.insert_clean_sheet_players,
        icon = R.drawable.ic_clean_sheets
    ),
    PenaltiesProvoked(
        statName = R.string.penalties_provoked,
        statTitle = R.string.insert_penalties_provoked_players,
        icon = R.drawable.ic_penalties
    ),
    Saves(
        statName = R.string.saves,
        statTitle = R.string.insert_saves_players,
        icon = R.drawable.ic_saves
    ),
    Fails(
        statName = R.string.fails,
        statTitle = R.string.insert_fails_players,
        icon = R.drawable.ic_fail
    ),
    YellowCards(
        statName = R.string.yellow_cards,
        statTitle = R.string.insert_yellow_card_players,
        icon = R.drawable.ic_yellow_card
    ),
    RedCards(
        statName = R.string.red_cards,
        statTitle = R.string.insert_red_card_players,
        icon = R.drawable.ic_red_card
    ),
    GamesPlayed(
        statName = R.string.games_played,
        statTitle = null,
        icon = R.drawable.ic_games_played,
        isInsertable = false
    )
}

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
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.LEAGUE
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.AddPlayer
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.AddStat
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.CupName
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.CupRound
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Description
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Formation
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.LocalGoals
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Location
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.RemovePlayer
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.RemoveStat
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Rival
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.ToggleSide
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.ToggleType
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.VisitorGoals
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot.Bench
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot.LineUp
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot.Manager

private val GOALS_RANGE = 0..99

/**
 * The whole form logic, kept here rather than in a view model so every platform fills the match in
 * the same way.
 */
fun InsertMatchUiState.update(field: InsertMatchField): InsertMatchUiState =
    when (field) {
        ToggleType -> copy(type = if (type == LEAGUE) CUP else LEAGUE)

        /**
         * The goals travel with the teams: what the app's side scored stays its own.
         */
        ToggleSide -> copy(isLocal = !isLocal, localGoals = visitorGoals, visitorGoals = localGoals)

        is Rival -> copy(rival = field.team)
        is LocalGoals -> if (field.goals.isValidGoals()) copy(localGoals = field.goals) else this
        is VisitorGoals -> if (field.goals.isValidGoals()) copy(visitorGoals = field.goals) else this
        is CupName -> copy(cupName = field.name)
        is CupRound -> copy(cupRound = field.round)
        is Location -> copy(location = field.location)
        is Description -> copy(description = field.description)
        is Formation -> copy(formation = field.formation)
        is AddPlayer -> withPlayer(field.slot, field.player)
        is RemovePlayer -> withoutPlayer(field.player)
        is AddStat -> copy(stats = stats + (field.stat to stat(field.stat) + field.player))
        is RemoveStat -> withoutStat(field.stat, field.player)
    }

private fun Int?.isValidGoals(): Boolean = this == null || this in GOALS_RANGE

private fun InsertMatchUiState.withPlayer(slot: PlayerSlot, player: GBPlayer): InsertMatchUiState =
    when (slot) {
        is LineUp -> {
            val replaced = lineUp[slot.index]
            copy(lineUp = lineUp + (slot.index to player)).withoutStatsOf(replaced)
        }
        Bench -> if (bench.size < BENCH_LIMIT) copy(bench = bench + player) else this
        Manager -> if (managers.size < MANAGERS_LIMIT) copy(managers = managers + player) else this
    }

/**
 * A player leaves the match from wherever they were, and their stats go with them.
 */
private fun InsertMatchUiState.withoutPlayer(player: GBPlayer): InsertMatchUiState =
    copy(
        lineUp = lineUp.filterValues { it.id != player.id },
        bench = bench.filterNot { it.id == player.id },
        managers = managers.filterNot { it.id == player.id }
    ).withoutStatsOf(player)

private fun InsertMatchUiState.withoutStatsOf(player: GBPlayer?): InsertMatchUiState =
    if (player == null) {
        this
    } else {
        copy(stats = stats.mapValues { (_, players) -> players.filterNot { it.id == player.id } })
    }

/**
 * Only one of the times the player got it.
 */
private fun InsertMatchUiState.withoutStat(stat: GBStat, player: GBPlayer): InsertMatchUiState {
    val given = stat(stat)
    val index = given.indexOfFirst { it.id == player.id }
    if (index == -1) return this

    return copy(stats = stats + (stat to given.filterIndexed { position, _ -> position != index }))
}

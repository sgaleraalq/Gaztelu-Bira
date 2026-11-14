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

import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog.None
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.CupName
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.CupRound
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Date
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Description
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Formation
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Local
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Location
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Rival
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.RivalGoals
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.TeamGoals
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Time
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Type
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchPlayerTarget
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchPlayerTarget.Bench
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchPlayerTarget.LineUp
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchPlayerTarget.Manager
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchPlayerTarget.Stat
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection

private const val MAX_GOALS_DIGITS = 2

/**
 * How the form answers each action, as plain functions from one state to the next.
 *
 * They live next to the state rather than in a view model because nothing in them is specific to a
 * platform: the Android and the iOS view models both hold an [InsertMatchUiState] and call these,
 * and neither has to write the rules of the form a second time.
 */

/**
 * Any edit closes whichever dialog was open, as the pickers are the usual way to make one.
 */
fun InsertMatchUiState.update(field: InsertMatchField): InsertMatchUiState =
    when (field) {
        is Type -> copy(type = field.type)
        is CupName -> copy(cupName = field.name)
        is CupRound -> copy(cupRound = field.round)
        is Local -> copy(isLocal = field.isLocal)
        is Rival -> copy(rival = field.team)
        is TeamGoals -> copy(teamGoals = field.goals.asGoals())
        is RivalGoals -> copy(rivalGoals = field.goals.asGoals())
        is Date -> copy(date = field.date)
        is Time -> copy(hour = field.hour, minute = field.minute)
        is Location -> copy(location = field.location)
        is Description -> copy(description = field.description)
        is Formation -> copy(formation = field.formation)
    }.copy(dialog = None)

fun InsertMatchUiState.show(dialog: InsertMatchDialog): InsertMatchUiState =
    copy(dialog = dialog)

fun InsertMatchUiState.toggle(section: InsertMatchSection): InsertMatchUiState =
    copy(expanded = if (section in expanded) expanded - section else expanded + section)

/**
 * The players the picker offers for [target]: nobody is called up twice, and a stat can only go to
 * someone who was called up.
 */
fun InsertMatchUiState.candidates(target: InsertMatchPlayerTarget): List<GBPlayer> =
    when (target) {
        is LineUp, Bench -> {
            val taken = calledUp.map { it.id }.toSet()
            players.filterNot { it.id in taken }
        }
        Manager -> {
            val taken = managers.map { it.id }.toSet()
            staff.filterNot { it.id in taken }
        }
        is Stat -> calledUp
    }

/**
 * Puts [player] wherever the open picker was asking for, and closes it. With no picker open there
 * is nowhere to put them, and the state stays as it was.
 */
fun InsertMatchUiState.pick(player: GBPlayer): InsertMatchUiState {
    val target = (dialog as? InsertMatchDialog.Player)?.target ?: return this
    return when (target) {
        is LineUp -> copy(lineUp = lineUp + (target.index to player))
        Bench -> copy(bench = bench + player)
        Manager -> copy(managers = managers + player)
        is Stat -> copy(stats = stats + (target.stat to stats[target.stat].orEmpty() + player))
    }.copy(dialog = None)
}

/**
 * A player who leaves the call-up takes their stats with them: the match cannot keep a goal by
 * someone who did not play it.
 *
 * @param index The place in the target's list; the line-up ignores it, its target names the slot.
 */
fun InsertMatchUiState.remove(target: InsertMatchPlayerTarget, index: Int): InsertMatchUiState =
    when (target) {
        is LineUp -> copy(lineUp = lineUp - target.index).withoutStatsOf(lineUp[target.index])
        Bench -> copy(bench = bench.withoutIndex(index)).withoutStatsOf(bench.getOrNull(index))
        Manager -> copy(managers = managers.withoutIndex(index))
        is Stat -> copy(
            stats = stats + (target.stat to stats[target.stat].orEmpty().withoutIndex(index))
        )
    }

private fun InsertMatchUiState.withoutStatsOf(player: GBPlayer?): InsertMatchUiState {
    if (player == null) return this
    return copy(stats = stats.mapValues { (_, players) -> players.filterNot { it.id == player.id } })
}

private fun <T> List<T>.withoutIndex(index: Int): List<T> =
    filterIndexed { current, _ -> current != index }

private fun String.asGoals(): String =
    filter(Char::isDigit).take(MAX_GOALS_DIGITS)

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

package com.sgale.gaztelubira.multiplatform.ui.insert.match.state

import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.model.GBTeam

sealed interface InsertMatchField {
    data object ToggleType : InsertMatchField
    data object ToggleSide : InsertMatchField

    data class Rival(
        val team: GBTeam
    ) : InsertMatchField

    data class LocalGoals(
        val goals: Int?
    ) : InsertMatchField

    data class VisitorGoals(
        val goals: Int?
    ) : InsertMatchField

    data class CupName(
        val name: String
    ) : InsertMatchField

    data class CupRound(
        val round: String
    ) : InsertMatchField

    data class Location(
        val location: String
    ) : InsertMatchField

    data class Description(
        val description: String
    ) : InsertMatchField

    data class Formation(
        val formation: LineUpFormation
    ) : InsertMatchField

    data class AddPlayer(
        val slot: PlayerSlot,
        val player: GBPlayer
    ) : InsertMatchField

    data class RemovePlayer(
        val slot: PlayerSlot,
        val player: GBPlayer
    ) : InsertMatchField

    data class AddStat(
        val stat: GBStat,
        val player: GBPlayer
    ) : InsertMatchField

    data class RemoveStat(
        val stat: GBStat,
        val player: GBPlayer
    ) : InsertMatchField
}

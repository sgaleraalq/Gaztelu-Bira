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
import com.sgale.gaztelubira.multiplatform.model.GBMatchType
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.model.GBTeam

/**
 * The blocks of the form that fold away.
 */
enum class InsertMatchSection {
    INFORMATION,
    LINE_UP,
    STATS
}

/**
 * Everything the user can edit that is a plain value. Who plays where goes through
 * [InsertMatchPlayerTarget] instead, because it always starts from a picker.
 */
sealed interface InsertMatchField {

    data class Type(
        val type: GBMatchType
    ) : InsertMatchField

    data class CupName(
        val name: String
    ) : InsertMatchField

    data class CupRound(
        val round: String
    ) : InsertMatchField

    /**
     * Whether Gaztelu Bira plays at home.
     */
    data class Local(
        val isLocal: Boolean
    ) : InsertMatchField

    data class Rival(
        val team: GBTeam
    ) : InsertMatchField

    data class TeamGoals(
        val goals: String
    ) : InsertMatchField

    data class RivalGoals(
        val goals: String
    ) : InsertMatchField

    /**
     * @param date Midnight UTC of the chosen day, in milliseconds, as the date picker gives it.
     */
    data class Date(
        val date: Long
    ) : InsertMatchField

    data class Time(
        val hour: Int,
        val minute: Int
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
}

/**
 * Where a picked player goes, and where one is removed from.
 */
sealed interface InsertMatchPlayerTarget {

    /**
     * @param index Slot of the formation, in the order [LineUpFormation.positions] lists them.
     */
    data class LineUp(
        val index: Int
    ) : InsertMatchPlayerTarget

    data object Bench : InsertMatchPlayerTarget

    data object Manager : InsertMatchPlayerTarget

    data class Stat(
        val stat: GBStat
    ) : InsertMatchPlayerTarget
}

sealed interface InsertMatchDialog {

    data object None : InsertMatchDialog

    data object Rival : InsertMatchDialog

    data object Date : InsertMatchDialog

    data object Time : InsertMatchDialog

    data class Player(
        val target: InsertMatchPlayerTarget
    ) : InsertMatchDialog
}

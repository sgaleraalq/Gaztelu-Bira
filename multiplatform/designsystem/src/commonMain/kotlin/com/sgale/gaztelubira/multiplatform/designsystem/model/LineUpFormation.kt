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

package com.sgale.gaztelubira.multiplatform.designsystem.model

import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.BackCenter
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.BackCenterLeft
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.BackCenterRight
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.Goalkeeper
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.LeftStriker
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.MidAttackingLeft
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.MidAttackingRight
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.MidDefensiveCenter
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.MidDefensiveLeft
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.MidDefensiveRight
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.RightStriker
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.WingBackLeft
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.WingBackRight
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition.WingerLeft

private const val PLAYERS_ON_PITCH = 11
private const val FOUR_THREE_THREE = "4-3-3"
private const val FOUR_FOUR_TWO = "4-4-2"
private const val FIVE_THREE_TWO = "5-3-2"

enum class LineUpFormation(
    val code: String,
    val positions: List<PlayerPosition>
) {
    /**
     * 4-3-3 FORMATION
     */
    FourThreeThree(
        code = FOUR_THREE_THREE,
        positions = listOf(
            PlayerPosition(Goalkeeper, x = 0.5f, y = 0.9f),
            PlayerPosition(WingBackLeft, x = 0.15f, y = 0.75f),
            PlayerPosition(BackCenterLeft, x = 0.4f, y = 0.75f),
            PlayerPosition(BackCenterRight, x = 0.6f, y = 0.75f),
            PlayerPosition(WingBackRight, x = 0.85f, y = 0.75f),
            PlayerPosition(MidDefensiveLeft, x = 0.2f, y = 0.45f),
            PlayerPosition(MidDefensiveCenter, x = 0.5f, y = 0.55f),
            PlayerPosition(MidAttackingRight, x = 0.8f, y = 0.45f),
            PlayerPosition(WingerLeft, x = 0.2f, y = 0.2f),
            PlayerPosition(RightStriker, x = 0.5f, y = 0.15f),
            PlayerPosition(LeftStriker, x = 0.8f, y = 0.2f)
        )
    ),

    /**
     * 4-4-2 FORMATION
     */
    FourFourTwo(
        code = FOUR_FOUR_TWO,
        positions = listOf(
            PlayerPosition(Goalkeeper, x = 0.5f, y = 0.9f),
            PlayerPosition(WingBackLeft, x = 0.15f, y = 0.75f),
            PlayerPosition(BackCenterLeft, x = 0.4f, y = 0.75f),
            PlayerPosition(BackCenterRight, x = 0.6f, y = 0.75f),
            PlayerPosition(WingBackRight, x = 0.85f, y = 0.75f),
            PlayerPosition(MidAttackingLeft, x = 0.15f, y = 0.4f),
            PlayerPosition(MidDefensiveLeft, x = 0.35f, y = 0.5f),
            PlayerPosition(MidDefensiveRight, x = 0.65f, y = 0.5f),
            PlayerPosition(MidAttackingRight, x = 0.85f, y = 0.4f),
            PlayerPosition(LeftStriker, x = 0.3f, y = 0.15f),
            PlayerPosition(RightStriker, x = 0.7f, y = 0.15f)
        )
    ),

    /**
     * 5-3-2 FORMATION
     */
    FiveThreeTwo(
        code = FIVE_THREE_TWO,
        positions = listOf(
            PlayerPosition(Goalkeeper, x = 0.5f, y = 0.9f),
            PlayerPosition(WingBackLeft, x = 0.12f, y = 0.60f),
            PlayerPosition(BackCenterLeft, x = 0.25f, y = 0.75f),
            PlayerPosition(BackCenter, x = 0.5f, y = 0.75f),
            PlayerPosition(BackCenterRight, x = 0.75f, y = 0.75f),
            PlayerPosition(WingBackRight, x = 0.88f, y = 0.60f),
            PlayerPosition(MidDefensiveLeft, x = 0.25f, y = 0.40f),
            PlayerPosition(MidDefensiveCenter, x = 0.5f, y = 0.50f),
            PlayerPosition(MidDefensiveRight, x = 0.75f, y = 0.40f),
            PlayerPosition(LeftStriker, x = 0.35f, y = 0.20f),
            PlayerPosition(RightStriker, x = 0.65f, y = 0.20f)
        )
    );

    init {
        require(positions.size == PLAYERS_ON_PITCH) {
            "$code lines up ${positions.size} players, expected $PLAYERS_ON_PITCH"
        }
    }

    operator fun get(index: Int): PlayerPosition? = positions.getOrNull(index)

    fun indexOf(position: LineUpPosition): Int? =
        positions.indexOfFirst { it.position == position }.takeIf { it >= 0 }

    companion object {
        val DEFAULT = FourThreeThree

        fun fromCode(code: String): LineUpFormation? =
            entries.firstOrNull { it.code == code }
    }
}

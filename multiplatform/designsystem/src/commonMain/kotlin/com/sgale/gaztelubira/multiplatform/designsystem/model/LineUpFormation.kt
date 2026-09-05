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

import androidx.compose.runtime.Stable
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

private const val FOUR_THREE_THREE = "4-3-3"
private const val FOUR_FOUR_TWO = "4-4-2"
private const val FIVE_THREE_TWO = "5-3-2"

@Stable
sealed class LineUpFormation(
    val formation: String,
    val positions: List<PlayerPosition>
) {
    companion object {
        val ALL_FORMATIONS by lazy {
            listOf(
                FourThreeThree,
                FourFourTwo,
                FiveThreeTwo
            )
        }

        fun getLineUpFromString(formation: String): LineUpFormation =
            ALL_FORMATIONS.firstOrNull {
                it.formation == formation
            } ?: FourThreeThree
    }

    /**
     * 4-3-3 FORMATION
     */
    data object FourThreeThree : LineUpFormation(
        FOUR_THREE_THREE,
        listOf(
            PlayerPosition(1, Goalkeeper, 0.5f, 0.9f),
            PlayerPosition(2, WingBackLeft, 0.15f, 0.75f),
            PlayerPosition(3, BackCenterLeft, 0.4f, 0.75f),
            PlayerPosition(4, BackCenterRight, 0.6f, 0.75f),
            PlayerPosition(5, WingBackRight, 0.85f, 0.75f),
            PlayerPosition(8, MidDefensiveLeft, 0.2f, 0.45f),
            PlayerPosition(6, MidDefensiveCenter, 0.5f, 0.55f),
            PlayerPosition(7, MidAttackingRight, 0.8f, 0.45f),
            PlayerPosition(9, WingerLeft, 0.2f, 0.2f),
            PlayerPosition(10, RightStriker, 0.5f, 0.15f),
            PlayerPosition(11, LeftStriker, 0.8f, 0.2f)
        )
    )

    /**
     * 4-4-2 FORMATION
     */
    data object FourFourTwo : LineUpFormation(
        FOUR_FOUR_TWO,
        listOf(
            PlayerPosition(1, Goalkeeper, 0.5f, 0.9f),
            PlayerPosition(2, WingBackLeft, 0.15f, 0.75f),
            PlayerPosition(3, BackCenterLeft, 0.4f, 0.75f),
            PlayerPosition(4, BackCenterRight, 0.6f, 0.75f),
            PlayerPosition(5, WingBackRight, 0.85f, 0.75f),
            PlayerPosition(8, MidAttackingLeft, 0.15f, 0.4f),
            PlayerPosition(6, MidDefensiveLeft, 0.35f, 0.5f),
            PlayerPosition(7, MidDefensiveRight, 0.65f, 0.5f),
            PlayerPosition(9, MidAttackingRight, 0.85f, 0.4f),
            PlayerPosition(10, LeftStriker, 0.3f, 0.15f),
            PlayerPosition(11, RightStriker, 0.7f, 0.15f)
        )
    )

    /**
     * 5-3-2 FORMATION
     */
    data object FiveThreeTwo : LineUpFormation(
        FIVE_THREE_TWO,
        listOf(
            PlayerPosition(1, Goalkeeper, 0.5f, 0.9f),
            PlayerPosition(2, WingBackLeft, 0.12f, 0.60f),
            PlayerPosition(3, BackCenterLeft, 0.25f, 0.75f),
            PlayerPosition(4, BackCenter, 0.5f, 0.75f),
            PlayerPosition(5, BackCenterRight, 0.75f, 0.75f),
            PlayerPosition(6, WingBackRight, 0.88f, 0.60f),
            PlayerPosition(7, MidDefensiveLeft, 0.25f, 0.40f),
            PlayerPosition(8, MidDefensiveCenter, 0.5f, 0.50f),
            PlayerPosition(9, MidDefensiveRight, 0.75f, 0.40f),
            PlayerPosition(10, LeftStriker, 0.35f, 0.20f),
            PlayerPosition(11, RightStriker, 0.65f, 0.20f)
        )
    )
}

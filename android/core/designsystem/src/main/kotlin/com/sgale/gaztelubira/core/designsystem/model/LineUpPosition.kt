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

package com.sgale.gaztelubira.core.designsystem.model

import com.sgale.gaztelubira.core.designsystem.R
import com.sgale.gaztelubira.core.domain.model.player.Position
import com.sgale.gaztelubira.core.domain.model.player.Position.Defender
import com.sgale.gaztelubira.core.domain.model.player.Position.Forward
import com.sgale.gaztelubira.core.domain.model.player.Position.GoalKeeper
import com.sgale.gaztelubira.core.domain.model.player.Position.MidFielder

enum class LineUpPosition(
    val position: Position,
    val positionName: Int
) {
    Goalkeeper(
        position = GoalKeeper,
        positionName = R.string.goalkeeper
    ),
    BackCenter(
        position = Defender,
        positionName = R.string.back_center
    ),
    BackCenterLeft(
        position = Defender,
        positionName = R.string.back_center_left
    ),
    BackCenterRight(
        position = Defender,
        positionName = R.string.back_center_right
    ),
    WingBackLeft(
        position = Defender,
        positionName = R.string.wing_back_left
    ),
    WingBackRight(
        position = Defender,
        positionName = R.string.wing_back_right
    ),

    MidDefensiveLeft(
        position = MidFielder,
        positionName = R.string.mid_defensive_left
    ),
    MidDefensiveRight(
        position = MidFielder,
        positionName = R.string.mid_defensive_right
    ),
    MidDefensiveCenter(
        position = MidFielder,
        positionName = R.string.mid_defensive_center
    ),
    MidCenterLeft(
        position = MidFielder,
        positionName = R.string.mid_center_left
    ),
    MidCenterRight(
        position = MidFielder,
        positionName = R.string.mid_center_right
    ),
    MidCenter(
        position = MidFielder,
        positionName = R.string.mid_center
    ),
    MidAttackingLeft(
        position = MidFielder,
        positionName = R.string.mid_attacking_left
    ),
    MidAttackingRight(
        position = MidFielder,
        positionName = R.string.mid_attacking_right
    ),
    MidAttackingCenter(
        position = MidFielder,
        positionName = R.string.mid_attacking_center
    ),
    WingerLeft(
        position = Forward,
        positionName = R.string.winger_left
    ),
    WingerRight(
        position = Forward,
        positionName = R.string.winger_right
    ),
    Striker(
        position = Forward,
        positionName = R.string.striker
    ),
    SecondStriker(
        position = Forward,
        positionName = R.string.second_striker
    ),
    RightStriker(
        position = Forward,
        positionName = R.string.right_striker
    ),
    LeftStriker(
        position = Forward,
        positionName = R.string.left_striker
    ),
    Bench(
        position = Position.Bench,
        positionName = Position.Bench.positionName
    ),
    Manager(
        position = Position.Manager,
        positionName = Position.Manager.positionName
    )
}
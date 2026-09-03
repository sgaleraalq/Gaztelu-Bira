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

package com.sgale.gaztelubira.multiplatform.designsystem.model

import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.manager
import com.sgale.gaztelubira.multiplatform.ui.resources.bench
import com.sgale.gaztelubira.multiplatform.ui.resources.back_center
import com.sgale.gaztelubira.multiplatform.ui.resources.back_center_left
import com.sgale.gaztelubira.multiplatform.ui.resources.back_center_right
import com.sgale.gaztelubira.multiplatform.ui.resources.goalkeeper
import com.sgale.gaztelubira.multiplatform.ui.resources.left_striker
import com.sgale.gaztelubira.multiplatform.ui.resources.mid_attacking_center
import com.sgale.gaztelubira.multiplatform.ui.resources.mid_attacking_left
import com.sgale.gaztelubira.multiplatform.ui.resources.mid_attacking_right
import com.sgale.gaztelubira.multiplatform.ui.resources.mid_center
import com.sgale.gaztelubira.multiplatform.ui.resources.mid_center_left
import com.sgale.gaztelubira.multiplatform.ui.resources.mid_center_right
import com.sgale.gaztelubira.multiplatform.ui.resources.mid_defensive_center
import com.sgale.gaztelubira.multiplatform.ui.resources.mid_defensive_left
import com.sgale.gaztelubira.multiplatform.ui.resources.mid_defensive_right
import com.sgale.gaztelubira.multiplatform.ui.resources.right_striker
import com.sgale.gaztelubira.multiplatform.ui.resources.second_striker
import com.sgale.gaztelubira.multiplatform.ui.resources.striker
import com.sgale.gaztelubira.multiplatform.ui.resources.wing_back_left
import com.sgale.gaztelubira.multiplatform.ui.resources.wing_back_right
import com.sgale.gaztelubira.multiplatform.ui.resources.winger_left
import com.sgale.gaztelubira.multiplatform.ui.resources.winger_right
import org.jetbrains.compose.resources.StringResource

enum class LineUpPosition(
    val positionName: StringResource
) {
    Goalkeeper(
        positionName = Res.string.goalkeeper
    ),
    BackCenter(
        positionName = Res.string.back_center
    ),
    BackCenterLeft(
        positionName = Res.string.back_center_left
    ),
    BackCenterRight(
        positionName = Res.string.back_center_right
    ),
    WingBackLeft(
        positionName = Res.string.wing_back_left
    ),
    WingBackRight(
        positionName = Res.string.wing_back_right
    ),

    MidDefensiveLeft(
        positionName = Res.string.mid_defensive_left
    ),
    MidDefensiveRight(
        positionName = Res.string.mid_defensive_right
    ),
    MidDefensiveCenter(
        positionName = Res.string.mid_defensive_center
    ),
    MidCenterLeft(
        positionName = Res.string.mid_center_left
    ),
    MidCenterRight(
        positionName = Res.string.mid_center_right
    ),
    MidCenter(
        positionName = Res.string.mid_center
    ),
    MidAttackingLeft(
        positionName = Res.string.mid_attacking_left
    ),
    MidAttackingRight(
        positionName = Res.string.mid_attacking_right
    ),
    MidAttackingCenter(
        positionName = Res.string.mid_attacking_center
    ),
    WingerLeft(
        positionName = Res.string.winger_left
    ),
    WingerRight(
        positionName = Res.string.winger_right
    ),
    Striker(
        positionName = Res.string.striker
    ),
    SecondStriker(
        positionName = Res.string.second_striker
    ),
    RightStriker(
        positionName = Res.string.right_striker
    ),
    LeftStriker(
        positionName = Res.string.left_striker
    ),
    Bench(
        positionName = Res.string.bench
    ),
    Manager(
        positionName = Res.string.manager
    )
}

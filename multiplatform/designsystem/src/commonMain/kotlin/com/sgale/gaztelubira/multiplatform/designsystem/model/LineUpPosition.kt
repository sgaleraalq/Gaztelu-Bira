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

package com.sgale.gaztelubira.multiplatform.designsystem.model

import androidx.compose.runtime.Composable
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.common_back_center
import com.sgale.gaztelubira.multiplatform.ui.resources.common_back_center_left
import com.sgale.gaztelubira.multiplatform.ui.resources.common_back_center_right
import com.sgale.gaztelubira.multiplatform.ui.resources.common_bench
import com.sgale.gaztelubira.multiplatform.ui.resources.common_goalkeeper
import com.sgale.gaztelubira.multiplatform.ui.resources.common_left_striker
import com.sgale.gaztelubira.multiplatform.ui.resources.common_manager
import com.sgale.gaztelubira.multiplatform.ui.resources.common_mid_attacking_center
import com.sgale.gaztelubira.multiplatform.ui.resources.common_mid_attacking_left
import com.sgale.gaztelubira.multiplatform.ui.resources.common_mid_attacking_right
import com.sgale.gaztelubira.multiplatform.ui.resources.common_mid_center
import com.sgale.gaztelubira.multiplatform.ui.resources.common_mid_center_left
import com.sgale.gaztelubira.multiplatform.ui.resources.common_mid_center_right
import com.sgale.gaztelubira.multiplatform.ui.resources.common_mid_defensive_center
import com.sgale.gaztelubira.multiplatform.ui.resources.common_mid_defensive_left
import com.sgale.gaztelubira.multiplatform.ui.resources.common_mid_defensive_right
import com.sgale.gaztelubira.multiplatform.ui.resources.common_right_striker
import com.sgale.gaztelubira.multiplatform.ui.resources.common_second_striker
import com.sgale.gaztelubira.multiplatform.ui.resources.common_striker
import com.sgale.gaztelubira.multiplatform.ui.resources.common_wing_back_left
import com.sgale.gaztelubira.multiplatform.ui.resources.common_wing_back_right
import com.sgale.gaztelubira.multiplatform.ui.resources.common_winger_left
import com.sgale.gaztelubira.multiplatform.ui.resources.common_winger_right
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

enum class LineUpPosition(
    internal val positionName: StringResource
) {
    Goalkeeper(
        positionName = Res.string.common_goalkeeper
    ),
    BackCenter(
        positionName = Res.string.common_back_center
    ),
    BackCenterLeft(
        positionName = Res.string.common_back_center_left
    ),
    BackCenterRight(
        positionName = Res.string.common_back_center_right
    ),
    WingBackLeft(
        positionName = Res.string.common_wing_back_left
    ),
    WingBackRight(
        positionName = Res.string.common_wing_back_right
    ),

    MidDefensiveLeft(
        positionName = Res.string.common_mid_defensive_left
    ),
    MidDefensiveRight(
        positionName = Res.string.common_mid_defensive_right
    ),
    MidDefensiveCenter(
        positionName = Res.string.common_mid_defensive_center
    ),
    MidCenterLeft(
        positionName = Res.string.common_mid_center_left
    ),
    MidCenterRight(
        positionName = Res.string.common_mid_center_right
    ),
    MidCenter(
        positionName = Res.string.common_mid_center
    ),
    MidAttackingLeft(
        positionName = Res.string.common_mid_attacking_left
    ),
    MidAttackingRight(
        positionName = Res.string.common_mid_attacking_right
    ),
    MidAttackingCenter(
        positionName = Res.string.common_mid_attacking_center
    ),
    WingerLeft(
        positionName = Res.string.common_winger_left
    ),
    WingerRight(
        positionName = Res.string.common_winger_right
    ),
    Striker(
        positionName = Res.string.common_striker
    ),
    SecondStriker(
        positionName = Res.string.common_second_striker
    ),
    RightStriker(
        positionName = Res.string.common_right_striker
    ),
    LeftStriker(
        positionName = Res.string.common_left_striker
    ),
    Bench(
        positionName = Res.string.common_bench
    ),
    Manager(
        positionName = Res.string.common_manager
    )
}

/**
 * The label of a pitch slot, already resolved. Callers get a `String` so they never need the
 * resource system this module happens to use.
 */
val LineUpPosition.label: String
    @Composable get() = stringResource(positionName)

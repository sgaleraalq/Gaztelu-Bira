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

package com.sgale.gaztelubira.multiplatform.model;

import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.bench
import com.sgale.gaztelubira.multiplatform.ui.resources.defender
import com.sgale.gaztelubira.multiplatform.ui.resources.forward
import com.sgale.gaztelubira.multiplatform.ui.resources.goalkeeper
import com.sgale.gaztelubira.multiplatform.ui.resources.manager
import com.sgale.gaztelubira.multiplatform.ui.resources.midfielder
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.runtime.Composable

enum class Position(
    internal val positionName: StringResource
) {
    Manager(
        Res.string.manager
    ),
    Bench(
        Res.string.bench
    ),
    GoalKeeper(
        Res.string.goalkeeper
    ),
    Defender(
        Res.string.defender
    ),
    MidFielder(
        Res.string.midfielder
    ),
    Forward(
        Res.string.forward
    );

    companion object {
        fun mapPosition(position: String): Position =
            entries.firstOrNull { it.name.equals(position, ignoreCase = true) }
                ?: Manager
    }
}

val Position.label: String
    @Composable get() = stringResource(positionName)

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

package com.sgale.gaztelubira.core.domain.model.player

import androidx.annotation.StringRes
import gbmultiplatform.domain.generated.resources.Res
import gbmultiplatform.domain.generated.resources.bench
import gbmultiplatform.domain.generated.resources.defender
import gbmultiplatform.domain.generated.resources.forward
import gbmultiplatform.domain.generated.resources.goalkeeper
import gbmultiplatform.domain.generated.resources.manager
import gbmultiplatform.domain.generated.resources.midfielder

enum class Position(
    @StringRes val positionName: Int
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

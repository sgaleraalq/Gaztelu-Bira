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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state

import androidx.compose.runtime.Stable
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.punctuation_values
import com.sgale.gaztelubira.multiplatform.ui.resources.settings
import com.sgale.gaztelubira.multiplatform.ui.resources.stats_values
import org.jetbrains.compose.resources.StringResource

@Stable
sealed interface GBStatsSettings {
    val title: StringResource?

    data object Hidden : GBStatsSettings {
        override val title = null
    }

    data object Menu : GBStatsSettings {
        override val title = Res.string.settings
    }

    data object ChangeStat : GBStatsSettings {
        override val title = Res.string.stats_values
    }

    data class ChangePunctuation(
        val draft: GBPunctuation
    ) : GBStatsSettings {
        override val title = Res.string.punctuation_values
    }

    companion object {
        internal fun GBStatsSettings.isAtMenu(): Boolean =
            this == Menu
    }
}

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

package com.sgale.gaztelubira.core.screens.home.tabs.stats.dialogs

import androidx.compose.runtime.Stable
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation

@Stable
sealed interface SettingsDialogState {
    val title: Int

    data object Default: SettingsDialogState {
        override val title = R.string.settings
    }

    data class ChangeStat(
        val selectedStat: Stat?
    ): SettingsDialogState {
        override val title = R.string.stats_values
    }

    data class ChangePunctuation(
        var punctuation: Punctuation
    ) : SettingsDialogState {
        override val title = R.string.punctuation_values
    }
}

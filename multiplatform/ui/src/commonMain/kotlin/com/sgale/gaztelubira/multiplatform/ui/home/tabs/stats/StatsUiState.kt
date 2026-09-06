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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats

import com.sgale.gaztelubira.multiplatform.model.GBPlayerStat
import com.sgale.gaztelubira.multiplatform.model.GBPlayerStatsDetail
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation
import com.sgale.gaztelubira.multiplatform.model.GBStat

data class StatsUiState(
    val isLoading: Boolean = true,
    val selectedStat: GBStat = GBStat.PERCENTAGE,
    val players: List<GBPlayerStat> = emptyList(),
    val punctuation: GBPunctuation = GBPunctuation(),
    val selectedPlayer: GBPlayerStatsDetail? = null,
    val settings: GBStatsSettings = GBStatsSettings.Hidden
)

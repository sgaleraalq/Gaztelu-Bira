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

package com.sgale.gaztelubira.multiplatform.ui.home

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.sgale.gaztelubira.multiplatform.designsystem.icons.GBAboutBottomTab
import com.sgale.gaztelubira.multiplatform.designsystem.icons.GBIcons
import com.sgale.gaztelubira.multiplatform.designsystem.icons.GBLogo
import com.sgale.gaztelubira.multiplatform.designsystem.icons.GBMatchesBottomTab
import com.sgale.gaztelubira.multiplatform.designsystem.icons.GBStatsBottomTab
import com.sgale.gaztelubira.multiplatform.designsystem.icons.GBTeamBottomTab

internal const val TAB_ABOUT = "about"
internal const val TAB_GAZTELU_BIRA = "gaztelu_bira"
internal const val TAB_MATCHES = "matches"
internal const val TAB_STATS = "stats"
internal const val TAB_TEAM = "team"

enum class HomeTab(
    val id: String,
    val iconContent: @Composable () -> Unit
) {
    HOME(
        id = TAB_GAZTELU_BIRA,
        iconContent = { Icon(GBIcons.GBLogo, null) }
    ),
    TEAM(
        id = TAB_TEAM,
        iconContent = { Icon(GBIcons.GBTeamBottomTab, null) }
    ),
    STATS(
        id = TAB_STATS,
        iconContent = { Icon(GBIcons.GBStatsBottomTab, null) }
    ),
    MATCHES(
        id = TAB_MATCHES,
        iconContent = { Icon(GBIcons.GBMatchesBottomTab, null) }
    ),
    ABOUT(
        id = TAB_ABOUT,
        iconContent = { Icon(GBIcons.GBAboutBottomTab, null) }
    )
}

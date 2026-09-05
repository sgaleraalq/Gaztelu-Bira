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

package com.sgale.gaztelubira.core.screens.home

import androidx.compose.runtime.Composable
import com.sgale.gaztelubira.core.screens.home.tabs.about.AboutScreen
import com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira.GazteluBiraHomeScreen
import com.sgale.gaztelubira.core.screens.home.tabs.matches.MatchesScreen
import com.sgale.gaztelubira.core.screens.home.tabs.stats.StatsScreen
import com.sgale.gaztelubira.core.screens.home.tabs.team.TeamScreen
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab.ABOUT
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab.HOME
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab.MATCHES
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab.STATS
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab.TEAM

/**
 * What each shared tab shows on Android. The tab list and its icons are multiplatform; these
 * screens are not — they are Hilt-injected and take the Android [NavigationState] — so the mapping
 * lives on this side.
 */
@Composable
internal fun HomeTab.Content(state: NavigationState) {
    when (this) {
        HOME -> GazteluBiraHomeScreen(state)
        TEAM -> TeamScreen(state)
        STATS -> StatsScreen(state)
        MATCHES -> MatchesScreen(state)
        ABOUT -> AboutScreen()
    }
}

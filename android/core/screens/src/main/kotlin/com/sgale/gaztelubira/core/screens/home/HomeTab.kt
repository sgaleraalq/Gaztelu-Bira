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

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.sgale.gaztelubira.core.screens.home.tabs.about.AboutScreen
import com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira.GazteluBiraHomeScreen
import com.sgale.gaztelubira.core.screens.home.tabs.matches.MatchesScreen
import com.sgale.gaztelubira.core.screens.home.tabs.stats.StatsScreen
import com.sgale.gaztelubira.core.screens.home.tabs.team.TeamScreen
import com.sgale.gaztelubira.core.screens.navigation.NavigationState

internal const val GAZTELU_BIRA = "gaztelu_bira"
internal const val MATCHES = "matches"
internal const val STATS = "stats"
internal const val TEAM = "team"
internal const val ABOUT = "about"

enum class HomeTab(
    val id: String,
    val iconContent: @Composable () -> Unit,
    val content: @Composable (NavigationState) -> Unit
) {
    Home(
        id = GAZTELU_BIRA,
        iconContent = { Icon(painterResource(GBIcons.GBLogo), null) },
        content = { GazteluBiraHomeScreen(it) }
    ),
    Team(
        id = TEAM,
        iconContent = { Icon(GBIcons.GBTeamBottomTab, null) },
        content = { TeamScreen(it) }
    ),
    Stats(
        id = STATS,
        iconContent = { Icon(painterResource(GBIcons.GBStatsBottomTab), null) },
        content = { StatsScreen(it) }
    ),
    Matches(
        id = MATCHES,
        iconContent = { Icon(painterResource(GBIcons.GBMatchesBottomTab), null) },
        content = { MatchesScreen(it) }
    ),
    About(
        id = ABOUT,
        iconContent = { Icon(GBIcons.GBAboutBottomTab, null) },
        content = { AboutScreen() }
    )
}

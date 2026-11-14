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

package com.sgale.gaztelubira.multiplatform.ui.ios

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.window.ComposeUIViewController
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.ui.home.HomeActions
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab.ABOUT
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab.HOME
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab.MATCHES
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab.STATS
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab.TEAM
import com.sgale.gaztelubira.multiplatform.ui.home.HomeUiState
import com.sgale.gaztelubira.multiplatform.ui.home.HomeView
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.about.AboutView
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.matches.MatchesActions
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.matches.MatchesUiState
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.matches.MatchesView
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.season.SeasonActions
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.season.SeasonUiState
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.season.SeasonView
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.StatsActions
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.StatsUiState
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.StatsView
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsPlayerModal.DismissPlayer
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsPlayerModal.ShowPlayer
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsSettings
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsState
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.team.TeamActions
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.team.TeamUiState
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.team.TeamView
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.home_matches_not_enough_players
import org.jetbrains.compose.resources.stringResource
import platform.UIKit.UIViewController

/**
 * What the stats tab answers to: Android's `StatsActions`, with the player modal split in two so
 * Swift does not have to tell its sealed cases apart.
 */
class IosStatsActions(
    internal val onChangeSelectedSeason: (String) -> Unit,
    internal val onChangeState: (GBStatsState) -> Unit,
    internal val onShowPlayer: (String) -> Unit,
    internal val onDismissPlayer: () -> Unit,
    internal val onSettingsChanged: (GBStatsSettings) -> Unit,
    internal val onStatSelected: (GBStat) -> Unit,
    internal val onPunctuationDraftChanged: (GBPunctuation) -> Unit,
    internal val onPunctuationConfirmed: (GBPunctuation) -> Unit,
    internal val onImgLoadingError: (String) -> Unit
) {
    internal fun asStatsActions() = StatsActions(
        onChangeSelectedSeason = onChangeSelectedSeason,
        onChangeState = onChangeState,
        onPlayerAction = { action ->
            when (action) {
                DismissPlayer -> onDismissPlayer()
                is ShowPlayer -> onShowPlayer(action.id)
            }
        },
        onSettingsChanged = onSettingsChanged,
        onStatSelected = onStatSelected,
        onPunctuationDraftChanged = onPunctuationDraftChanged,
        onPunctuationConfirmed = onPunctuationConfirmed,
        onImgLoadingError = onImgLoadingError
    )
}

/**
 * Everything the tabs of the home screen need. On Android each tab asks Hilt for its own view
 * model; here the whole home is a single Compose controller, so Swift builds the four of them and
 * hands their states over together.
 *
 * @param navigateTo Shared by the tabs: none of them owns navigation, as on Android.
 */
class IosHomeTabs(
    internal val season: IosState<SeasonUiState>,
    internal val team: IosState<TeamUiState>,
    internal val stats: IosState<StatsUiState>,
    internal val matches: IosState<MatchesUiState>,
    statsActions: IosStatsActions,
    navigateTo: (IosRoute) -> Unit
) {
    internal val navigation = navigateTo.asUiNavigation()
    internal val seasonActions = SeasonActions(navigateTo = navigation)
    internal val teamActions = TeamActions(navigateTo = navigation)
    internal val statsActions = statsActions.asStatsActions()
}

/**
 * The iOS counterpart of Android's `HomeScreen`.
 *
 * The selected tab is the one in [state]: Swift answers [onTabSelected] by writing it back, which
 * is what Android's `TabNavigation` plus `onTabChanged` amount to.
 *
 * @param showToast iOS has no toast of its own, so the Swift side draws one.
 */
fun homeViewController(
    state: IosState<HomeUiState>,
    onTabSelected: (HomeTab) -> Unit,
    onLogout: () -> Unit,
    onDismissLogout: () -> Unit,
    onUpdate: () -> Unit,
    tabs: IosHomeTabs,
    showToast: (String) -> Unit
): UIViewController {
    val actions = HomeActions(
        onTabSelected = onTabSelected,
        onLogout = onLogout,
        onDismissLogout = onDismissLogout,
        onUpdate = onUpdate
    )
    return ComposeUIViewController {
        GBTheme {
            /**
             * A hidden tab keeps its saved state (scroll position and the like), as it does on
             * Android.
             */
            val tabStates = rememberSaveableStateHolder()
            HomeView(
                state = state.value,
                actions = actions,
                tabContent = {
                    val tab = state.value.selectedTab
                    tabStates.SaveableStateProvider(tab.id) {
                        tab.Content(tabs, showToast)
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeTab.Content(
    tabs: IosHomeTabs,
    showToast: (String) -> Unit
) {
    when (this) {
        HOME -> SeasonView(tabs.season.value, tabs.seasonActions)
        TEAM -> TeamView(tabs.team.value, tabs.teamActions)
        STATS -> StatsView(tabs.stats.value, tabs.statsActions)
        MATCHES -> MatchesTab(tabs, showToast)
        ABOUT -> AboutView()
    }
}

/**
 * Same gate as the Android `MatchesScreen`: nothing is opened from this tab until the squad is
 * big enough to fill a lineup.
 */
@Composable
private fun MatchesTab(
    tabs: IosHomeTabs,
    showToast: (String) -> Unit
) {
    val notEnoughPlayersMsg = stringResource(Res.string.home_matches_not_enough_players)
    val actions = remember(tabs, showToast, notEnoughPlayersMsg) {
        MatchesActions(
            navigateTo = { destination ->
                when (tabs.matches.value.hasEnoughPlayers) {
                    true -> tabs.navigation(destination)
                    false -> showToast(notEnoughPlayersMsg)
                }
            }
        )
    }
    MatchesView(tabs.matches.value, actions)
}

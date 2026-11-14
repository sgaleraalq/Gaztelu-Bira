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

import SwiftUI
import GazteluBiraUI

/// The home screen and its tabs. On Android each tab is a screen with a view model of its own;
/// here the whole home is one Compose controller, so the four view models live side by side and
/// are handed to it together (`IosHomeTabs`).
struct HomeScreen: View {

    /// How far from the left edge a swipe may start, and how far it has to travel, to count as
    /// "back".
    private static let edgeWidth: CGFloat = 16
    private static let swipeDistance: CGFloat = 60

    @EnvironmentObject private var navState: NavigationState
    @EnvironmentObject private var toast: ToastCenter

    @StateObject private var viewModel: HomeViewModel
    @StateObject private var seasonViewModel: SeasonViewModel
    @StateObject private var teamViewModel: TeamViewModel
    @StateObject private var statsViewModel: StatsViewModel
    @StateObject private var matchesViewModel: MatchesViewModel

    init(container: AppContainer) {
        _viewModel = StateObject(
            wrappedValue: HomeViewModel(
                logout: container.logout,
                initAppHandler: container.initAppHandler
            )
        )
        _seasonViewModel = StateObject(
            wrappedValue: SeasonViewModel(
                sessionManager: container.sessionManager,
                selectedSeason: container.selectedSeason,
                getTeams: container.getTeams,
                getMatches: container.getMatches,
                seasonHandler: SeasonHandler()
            )
        )
        _teamViewModel = StateObject(
            wrappedValue: TeamViewModel(
                sessionManager: container.sessionManager,
                selectedSeason: container.selectedSeason,
                getSquad: container.getSquad
            )
        )
        _statsViewModel = StateObject(
            wrappedValue: StatsViewModel(
                selectedSeason: container.selectedSeason,
                getSquad: container.getSquad,
                getSeasonPlayerStats: container.getSeasonPlayerStats,
                getMatches: container.getMatches,
                seasonLocal: container.seasonLocal,
                preferences: container.preferences
            )
        )
        _matchesViewModel = StateObject(
            wrappedValue: MatchesViewModel(
                sessionManager: container.sessionManager,
                selectedSeason: container.selectedSeason,
                getMatches: container.getMatches,
                getSquad: container.getSquad,
                getTeam: container.getTeam
            )
        )
    }

    var body: some View {
        ComposeScreen { [viewModel, navState, toast] in
            HomeViewControllerKt.homeViewController(
                state: viewModel.state,
                onTabSelected: { viewModel.onTabChanged($0) },
                onLogout: { viewModel.logout(navState) },
                onDismissLogout: { viewModel.updateLogoutDialog(false) },
                onUpdate: { openStoreListing() },
                tabs: makeTabs(),
                showToast: { toast.show($0) }
            )
        }
        .overlay(alignment: .leading) { logoutEdge }
    }

    private func makeTabs() -> IosHomeTabs {
        let stats = statsViewModel
        let navigation = self.navState
        return IosHomeTabs(
            season: seasonViewModel.state,
            team: teamViewModel.state,
            stats: stats.state,
            matches: matchesViewModel.state,
            statsActions: IosStatsActions(
                onChangeSelectedSeason: { stats.onChangeSeason($0) },
                onChangeState: { stats.onChangeState($0) },
                onShowPlayer: { stats.onShowPlayer($0) },
                onDismissPlayer: { stats.onDismissPlayer() },
                onSettingsChanged: { stats.onSettingsChanged($0) },
                onStatSelected: { stats.onStatSelected($0) },
                onPunctuationDraftChanged: { stats.onPunctuationDraftChanged($0) },
                onPunctuationConfirmed: { stats.onPunctuationConfirmed($0) },
                onImgLoadingError: { Log.e("Error loading image: \($0)") }
            ),
            navigateTo: { navigation.navigateTo($0) }
        )
    }

    /// On Android, pressing back on the home asks whether to log out, and it is the only way to
    /// reach that dialog. iOS has no back button, so a swipe from the left edge — the gesture that
    /// means "back" everywhere else — stands in for it.
    private var logoutEdge: some View {
        Color.clear
            .frame(width: HomeScreen.edgeWidth)
            .frame(maxHeight: .infinity)
            .contentShape(Rectangle())
            .gesture(
                DragGesture(minimumDistance: 20).onEnded { drag in
                    if drag.translation.width > HomeScreen.swipeDistance {
                        viewModel.updateLogoutDialog(true)
                    }
                }
            )
    }
}

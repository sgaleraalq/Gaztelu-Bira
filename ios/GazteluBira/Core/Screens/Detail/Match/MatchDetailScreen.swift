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

struct MatchDetailScreen: View {

    @EnvironmentObject private var navState: NavigationState
    @StateObject private var viewModel: MatchDetailViewModel

    private let matchId: MatchId

    init(container: AppContainer, matchId: MatchId) {
        self.matchId = matchId
        _viewModel = StateObject(
            wrappedValue: MatchDetailViewModel(
                getPlayers: container.getPlayers,
                getMatchDetail: container.getMatchDetail,
                getMatchPlayerStats: container.getMatchPlayerStats,
                getTeam: container.getTeam
            )
        )
    }

    var body: some View {
        ComposeScreen { [viewModel, navState, matchId] in
            viewModel.loadMatch(matchId, onMatchNotFound: { navState.navigateBack() })
            return DetailViewControllersKt.matchDetailViewController(
                state: viewModel.state,
                changeUiState: { viewModel.changeUiState($0) },
                navigateTo: { navState.navigateTo($0) }
            )
        }
    }
}

@MainActor
final class MatchDetailViewModel: ObservableObject {

    let state = IosState<MatchDetailUiState>(
        initial: IosUiStatesKt.matchDetailUiState(team: GazteluBiraUtils.gazteluBira.toGBTeam())
    )

    private let getPlayers: GetPlayers
    private let getMatchDetail: GetMatchDetail
    private let getMatchPlayerStats: GetMatchPlayerStats
    private let getTeam: GetTeam

    init(
        getPlayers: GetPlayers,
        getMatchDetail: GetMatchDetail,
        getMatchPlayerStats: GetMatchPlayerStats,
        getTeam: GetTeam
    ) {
        self.getPlayers = getPlayers
        self.getMatchDetail = getMatchDetail
        self.getMatchPlayerStats = getMatchPlayerStats
        self.getTeam = getTeam
    }

    func changeUiState(_ newState: MatchDetailState) {
        let current = state.value
        state.value = current.doCopy(
            uiState: newState,
            team: current.team,
            localTeam: current.localTeam,
            localGoals: current.localGoals,
            visitorTeam: current.visitorTeam,
            visitorGoals: current.visitorGoals,
            information: current.information,
            lineUp: current.lineUp,
            stats: current.stats
        )
    }

    func loadMatch(_ matchId: MatchId, onMatchNotFound: @escaping () -> Void) {
        Task {
            do {
                state.value = try await loadState(matchId)
            } catch {
                Log.e("Error loading match", error)
                onMatchNotFound()
            }
        }
    }

    private func loadState(_ matchId: MatchId) async throws -> MatchDetailUiState {
        let players = Dictionary(
            try await getPlayers().map { ($0.id, $0) },
            uniquingKeysWith: { first, _ in first }
        )
        let matchDetail = try await getMatchDetail(matchId)
        let match = matchDetail.match.match

        let localTeam = try await getTeam(match.localTeam).toGBTeam()
        let visitorTeam = try await getTeam(match.visitorTeam).toGBTeam()

        let information = matchDetail.toMatchDetailInformation(localTeam: localTeam, visitorTeam: visitorTeam)
        let lineUp = matchDetail.toMatchDetailLineUp(players)
        let stats = try await getMatchPlayerStats(matchId).toMatchDetailStats(players)

        return MatchDetailUiState(
            uiState: IosSealedStatesKt.matchDetailLineup(lineUp: lineUp),
            team: GazteluBiraUtils.gazteluBira.toGBTeam(),
            localTeam: localTeam,
            localGoals: match.score.local.kotlin,
            visitorTeam: visitorTeam,
            visitorGoals: match.score.visitor.kotlin,
            information: information,
            lineUp: lineUp,
            stats: stats
        )
    }
}

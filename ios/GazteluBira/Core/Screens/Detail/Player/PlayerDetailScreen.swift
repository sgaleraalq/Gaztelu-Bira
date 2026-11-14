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

struct PlayerDetailScreen: View {

    @EnvironmentObject private var navState: NavigationState
    @EnvironmentObject private var toast: ToastCenter
    @StateObject private var viewModel: PlayerDetailViewModel

    private let playerId: String
    private let isManager: Bool

    init(container: AppContainer, playerId: String, isManager: Bool) {
        self.playerId = playerId
        self.isManager = isManager
        _viewModel = StateObject(
            wrappedValue: PlayerDetailViewModel(
                getPlayerDetailInformation: GetPlayerDetailInformation(
                    selectedSeason: container.selectedSeason,
                    getPlayer: container.getPlayer,
                    getSeasonPlayer: container.getSeasonPlayer
                ),
                getPlayerStats: container.getPlayerStats,
                getMatches: container.getMatches
            )
        )
    }

    var body: some View {
        ComposeScreen { [viewModel, navState, toast, playerId, isManager] in
            viewModel.updateState(PlayerId(playerId), isManager: isManager)
            return DetailViewControllersKt.playerDetailViewController(
                state: viewModel.state,
                navigateTo: { navState.navigateTo($0) },
                showToast: { toast.show($0) }
            )
        }
    }
}

struct PlayerDetailState {
    let player: Player
    let seasonPlayer: SeasonPlayer

    func toGBPlayer() -> GBPlayer {
        GBPlayer(
            id: "",
            name: player.name,
            image: player.bodyImage,
            dorsal: seasonPlayer.dorsal?.kotlinBoxed
        )
    }
}

final class GetPlayerDetailInformation {

    private let selectedSeason: SelectedSeason
    private let getPlayer: GetPlayer
    private let getSeasonPlayer: GetSeasonPlayer

    init(selectedSeason: SelectedSeason, getPlayer: GetPlayer, getSeasonPlayer: GetSeasonPlayer) {
        self.selectedSeason = selectedSeason
        self.getPlayer = getPlayer
        self.getSeasonPlayer = getSeasonPlayer
    }

    func callAsFunction(_ playerId: PlayerId) async throws -> PlayerDetailState? {
        guard let seasonId = await selectedSeason.first() else { return nil }
        return PlayerDetailState(
            player: try await getPlayer(playerId),
            seasonPlayer: try await getSeasonPlayer(playerId, seasonId: seasonId)
        )
    }
}

@MainActor
final class PlayerDetailViewModel: ObservableObject {

    let state = IosState<PlayerDetailUiState>(initial: IosUiStatesKt.playerDetailUiState())

    private let getPlayerDetailInformation: GetPlayerDetailInformation
    private let getPlayerStats: GetPlayerStats
    private let getMatches: GetMatches

    init(
        getPlayerDetailInformation: GetPlayerDetailInformation,
        getPlayerStats: GetPlayerStats,
        getMatches: GetMatches
    ) {
        self.getPlayerDetailInformation = getPlayerDetailInformation
        self.getPlayerStats = getPlayerStats
        self.getMatches = getMatches
    }

    func updateState(_ playerId: PlayerId, isManager: Bool) {
        Task {
            do {
                guard let playerInfo = try await getPlayerDetailInformation(playerId) else { return }
                let differences = try await goalDifferences(playerId)
                state.value = state.value.doCopy(
                    isManager: isManager,
                    player: playerInfo.toGBPlayer(),
                    winRate: IosSealedStatesKt.playerWinRate(
                        wins: differences.filter { $0 > 0 }.count.kotlin,
                        draws: differences.filter { $0 == 0 }.count.kotlin,
                        loses: differences.filter { $0 < 0 }.count.kotlin
                    ),
                    stats: state.value.stats
                )
            } catch {
                Log.e("Error loading player", error)
            }
        }
    }

    /// Results of the season matches the player has stats for, i.e. the ones they played.
    private func goalDifferences(_ playerId: PlayerId) async throws -> [Int] {
        let stats = try await getPlayerStats(playerId)
        let playedMatchIds = Set(stats.map { $0.matchId })
        let matches = try await getMatches()
        return matches
            .filter { playedMatchIds.contains($0.id) }
            .map { goalDifference($0.match) }
    }

    /// Seen from Gaztelu Bira's side: positive when it won, whether it played home or away.
    private func goalDifference(_ match: MatchScore) -> Int {
        match.isLocal
            ? match.score.local - match.score.visitor
            : match.score.visitor - match.score.local
    }
}

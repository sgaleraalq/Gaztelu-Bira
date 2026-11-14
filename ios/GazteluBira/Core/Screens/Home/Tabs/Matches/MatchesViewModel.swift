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

import Foundation
import GazteluBiraUI

private let minimumSquad = 11

@MainActor
final class MatchesViewModel: ObservableObject {

    let state = IosState<MatchesUiState>(initial: IosUiStatesKt.matchesUiState())

    private let getMatches: GetMatches
    private let getSquad: GetSquad
    private let getTeam: GetTeam

    private var matches: [GBMatch] = []
    private var hasEnoughPlayers = false
    private var isAdmin = false
    private var observations: [Task<Void, Never>] = []

    init(
        sessionManager: SessionManager,
        selectedSeason: SelectedSeason,
        getMatches: GetMatches,
        getSquad: GetSquad,
        getTeam: GetTeam
    ) {
        self.getMatches = getMatches
        self.getSquad = getSquad
        self.getTeam = getTeam

        let seasons = selectedSeason()
        let admins = sessionManager.isAdmin()
        observations = [
            Task { [weak self] in
                for await season in seasons {
                    await self?.load(season)
                }
            },
            Task { [weak self] in
                for await admin in admins {
                    self?.isAdmin = admin
                    self?.render()
                }
            }
        ]
    }

    deinit {
        observations.forEach { $0.cancel() }
    }

    private func load(_ season: SeasonId?) async {
        var stored: [Match] = []
        var squad: [SquadPlayer] = []
        if let season {
            stored = (try? await getMatches(season)) ?? []
            squad = (try? await getSquad(season)) ?? []
        }

        var loaded: [GBMatch] = []
        for match in stored.sorted(by: { $0.information.date > $1.information.date }) {
            let local = (try? await getTeam(match.match.localTeam)) ?? Team.errorTeam
            let visitor = (try? await getTeam(match.match.visitorTeam)) ?? Team.errorTeam
            loaded.append(match.toGBMatch(local: local, visitor: visitor))
        }

        matches = loaded
        hasEnoughPlayers = squad.filter { $0.position != .manager }.count >= minimumSquad
        render()
    }

    private func render() {
        state.value = MatchesUiState(
            matches: matches,
            isAdmin: isAdmin,
            hasEnoughPlayers: hasEnoughPlayers
        )
    }
}

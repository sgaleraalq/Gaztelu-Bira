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

@MainActor
final class TeamViewModel: ObservableObject {

    let state = IosState<TeamUiState>(initial: IosUiStatesKt.teamUiState())

    private let getSquad: GetSquad

    private var squad: [SquadPlayer] = []
    private var isAdmin = false
    private var observations: [Task<Void, Never>] = []

    init(
        sessionManager: SessionManager,
        selectedSeason: SelectedSeason,
        getSquad: GetSquad
    ) {
        self.getSquad = getSquad

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
        if let season {
            squad = (try? await getSquad(season)) ?? []
        } else {
            squad = []
        }
        render()
    }

    private func render() {
        state.value = TeamUiState(
            players: squad.filter { $0.position != .manager }.map { $0.toGBPlayer() },
            managers: squad.filter { $0.position == .manager }.map { $0.toGBPlayer() },
            isAdmin: isAdmin
        )
    }
}

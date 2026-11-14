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

struct TeamSeason {
    let points: Int
    let games: Int
    let wins: Int
    let draws: Int
    let loses: Int
    let goalsFor: Int
    let goalsAgainst: Int
    let streak: TeamStreak
}

struct TeamStreak {
    let currentStreak: Int
    let lastGames: [MatchResult]
}

final class SeasonHandler {

    func getGBInformation(_ matches: [Match]) -> TeamSeason {
        var wins = 0
        var draws = 0
        var loses = 0
        var goalsFor = 0
        var goalsAgainst = 0
        var games: [MatchResult] = []

        for match in matches.sorted(by: { $0.information.date < $1.information.date }) {
            let score = match.match
            let gbGoals = score.isLocal ? score.score.local : score.score.visitor
            let rivalGoals = score.isLocal ? score.score.visitor : score.score.local

            goalsFor += gbGoals
            goalsAgainst += rivalGoals

            if gbGoals > rivalGoals {
                wins += 1
                games.append(.victory)
            } else if gbGoals < rivalGoals {
                loses += 1
                games.append(.defeat)
            } else {
                draws += 1
                games.append(.draw)
            }
        }

        return TeamSeason(
            points: (wins * 3) + draws,
            games: matches.count,
            wins: wins,
            draws: draws,
            loses: loses,
            goalsFor: goalsFor,
            goalsAgainst: goalsAgainst,
            streak: TeamStreak(
                currentStreak: games.reversed().prefix { $0 == .victory }.count,
                lastGames: games
            )
        )
    }
}

@MainActor
final class SeasonViewModel: ObservableObject {

    let state = IosState<SeasonUiState>(initial: IosUiStatesKt.seasonUiState())

    private let getTeams: GetTeams
    private let getMatches: GetMatches
    private let seasonHandler: SeasonHandler

    private var matches: [Match] = []
    private var teams: [Team] = []
    private var isAdmin = false
    private var observations: [Task<Void, Never>] = []

    init(
        sessionManager: SessionManager,
        selectedSeason: SelectedSeason,
        getTeams: GetTeams,
        getMatches: GetMatches,
        seasonHandler: SeasonHandler
    ) {
        self.getTeams = getTeams
        self.getMatches = getMatches
        self.seasonHandler = seasonHandler

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
            matches = (try? await getMatches(season)) ?? []
            teams = (try? await getTeams(season)) ?? []
        } else {
            matches = []
            teams = []
        }
        render()
    }

    /// The season summary is built by a Kotlin helper and handed straight over: two classes of the
    /// shared UI are called `GBSeason`, and this way Swift does not have to say which one it is.
    private func render() {
        let season = seasonHandler.getGBInformation(matches)
        state.value = SeasonUiState(
            season: IosUiStatesKt.seasonSummary(
                points: season.points.kotlin,
                games: season.games.kotlin,
                wins: season.wins.kotlin,
                draws: season.draws.kotlin,
                loses: season.loses.kotlin,
                goalsFor: season.goalsFor.kotlin,
                goalsAgainst: season.goalsAgainst.kotlin,
                currentStreak: season.streak.currentStreak.kotlin,
                lastGames: season.streak.lastGames.map { $0.toGBMatchResult() }
            ),
            teams: teams
                .map { $0.toGBTeam() }
                .filter { $0.id != GazteluBiraUtils.gazteluBiraId },
            isAdmin: isAdmin
        )
    }
}

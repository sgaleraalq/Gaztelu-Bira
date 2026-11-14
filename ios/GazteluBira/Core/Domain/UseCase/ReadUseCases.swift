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

private let minDorsal = 1
private let maxDorsal = 99

/// Dorsals nobody wears yet in the selected season's squad.
final class GetAvailableDorsals {

    private let selectedSeason: SelectedSeason
    private let seasonLocal: SeasonLocal

    init(selectedSeason: SelectedSeason, seasonLocal: SeasonLocal) {
        self.selectedSeason = selectedSeason
        self.seasonLocal = seasonLocal
    }

    func callAsFunction() async throws -> [Int] {
        guard let season = await selectedSeason.first() else { return [] }
        let taken = Set(try await seasonLocal.getSeasonPlayers(season).compactMap { $0.dorsal })
        return (minDorsal...maxDorsal).filter { !taken.contains($0) }
    }
}

final class GetMatchDetail {

    private let matchLocal: MatchLocal

    init(matchLocal: MatchLocal) {
        self.matchLocal = matchLocal
    }

    func callAsFunction(_ matchId: MatchId) async throws -> MatchDetail {
        let match = try await matchLocal.getMatch(matchId)
        let events = try await matchLocal.getMatchEvents(matchId)
        let players = try await matchLocal.getMatchLineupPlayer(matchId)
        return MatchDetail(match: match, events: events, players: players)
    }
}

final class GetMatchPlayerStats {

    private let playerLocal: PlayerLocal

    init(playerLocal: PlayerLocal) {
        self.playerLocal = playerLocal
    }

    func callAsFunction(_ matchId: MatchId) async throws -> [PlayerStats] {
        try await playerLocal.getMatchPlayerStats(matchId)
    }
}

final class GetMatches {

    private let selectedSeason: SelectedSeason
    private let matchLocal: MatchLocal

    init(selectedSeason: SelectedSeason, matchLocal: MatchLocal) {
        self.selectedSeason = selectedSeason
        self.matchLocal = matchLocal
    }

    func callAsFunction() async throws -> [Match] {
        guard let seasonId = await selectedSeason.first() else { return [] }
        return try await matchLocal.getMatches(seasonId)
    }

    func callAsFunction(_ seasonId: SeasonId) async throws -> [Match] {
        try await matchLocal.getMatches(seasonId)
    }
}

final class GetPlayer {

    private let playerLocal: PlayerLocal

    init(playerLocal: PlayerLocal) {
        self.playerLocal = playerLocal
    }

    func callAsFunction(_ playerId: PlayerId) async throws -> Player {
        try await playerLocal.getPlayer(playerId)
    }
}

final class GetPlayerStats {

    private let selectedSeason: SelectedSeason
    private let playerLocal: PlayerLocal

    init(selectedSeason: SelectedSeason, playerLocal: PlayerLocal) {
        self.selectedSeason = selectedSeason
        self.playerLocal = playerLocal
    }

    /// One `PlayerStats` per match the player played in the selected season.
    func callAsFunction(_ playerId: PlayerId) async throws -> [PlayerStats] {
        guard let seasonId = await selectedSeason.first() else { return [] }
        return try await playerLocal.getSeasonPlayerStats(playerId, seasonId: seasonId)
    }
}

final class GetPlayers {

    private let playerLocal: PlayerLocal

    init(playerLocal: PlayerLocal) {
        self.playerLocal = playerLocal
    }

    func callAsFunction() async throws -> [Player] {
        try await playerLocal.getPlayers()
    }
}

final class GetSeasonPlayer {

    private let seasonLocal: SeasonLocal

    init(seasonLocal: SeasonLocal) {
        self.seasonLocal = seasonLocal
    }

    func callAsFunction(_ playerId: PlayerId, seasonId: SeasonId) async throws -> SeasonPlayer {
        try await seasonLocal.getSeasonPlayer(playerId, seasonId: seasonId)
    }
}

/// One `PlayerStats` per player and match of the season, so a player's games played is how many
/// of them they have.
final class GetSeasonPlayerStats {

    private let playerLocal: PlayerLocal

    init(playerLocal: PlayerLocal) {
        self.playerLocal = playerLocal
    }

    func callAsFunction(_ seasonId: SeasonId) async throws -> [PlayerStats] {
        try await playerLocal.getSeasonPlayerStats(seasonId)
    }
}

final class GetSquad {

    private let seasons: SeasonLocal

    init(seasons: SeasonLocal) {
        self.seasons = seasons
    }

    func callAsFunction(_ seasonId: SeasonId) async throws -> [SquadPlayer] {
        try await seasons.getSquad(seasonId)
    }
}

final class GetTeam {

    private let teams: TeamLocal

    init(teams: TeamLocal) {
        self.teams = teams
    }

    func callAsFunction(_ teamId: TeamId) async throws -> Team {
        try await teams.getTeam(teamId)
    }
}

final class GetTeams {

    private let teams: TeamLocal

    init(teams: TeamLocal) {
        self.teams = teams
    }

    func callAsFunction(_ seasonId: SeasonId) async throws -> [Team] {
        try await teams.getTeams(seasonId)
    }
}

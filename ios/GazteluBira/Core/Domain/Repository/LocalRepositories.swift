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

/// The local cache, as the domain sees it. Every call can fail the way a Room query can: a single
/// row that is not there throws.

protocol MatchLocal {
    func getMatch(_ matchId: MatchId) async throws -> Match
    func getMatches(_ seasonId: SeasonId) async throws -> [Match]
    func getMatchEvents(_ matchId: MatchId) async throws -> [MatchEvent]
    func getMatchLineupPlayer(_ matchId: MatchId) async throws -> [MatchLineupPlayer]
    func insertMatches(_ matches: [Match]) async throws
    func insertMatchEvents(_ matchId: MatchId, events: [MatchEvent]) async throws
    func insertMatchPlayers(_ matchId: MatchId, players: [MatchLineupPlayer]) async throws
}

protocol PlayerLocal {
    func getPlayer(_ id: PlayerId) async throws -> Player
    func getPlayers() async throws -> [Player]
    func getMatchPlayerStats(_ matchId: MatchId) async throws -> [PlayerStats]
    func getSeasonPlayerStats(_ seasonId: SeasonId) async throws -> [PlayerStats]
    func getSeasonPlayerStats(_ playerId: PlayerId, seasonId: SeasonId) async throws -> [PlayerStats]
    func insertPlayer(_ player: Player) async throws
    func insertPlayers(_ players: [Player]) async throws
    func insertMatchStats(_ matchId: MatchId, stats: [PlayerStats]) async throws
}

protocol SeasonLocal {
    func getSeasons() async throws -> [Season]
    func getSeasonPlayer(_ playerId: PlayerId, seasonId: SeasonId) async throws -> SeasonPlayer
    func getSeasonPlayers(_ season: SeasonId) async throws -> [SeasonPlayer]
    func getSquad(_ season: SeasonId) async throws -> [SquadPlayer]
    func insertSeasons(_ seasons: [Season]) async throws
    func insertSeasonPlayers(_ players: [SeasonPlayer]) async throws
}

protocol TeamLocal {
    func getTeam(_ teamId: TeamId) async throws -> Team
    func getTeams(_ seasonId: SeasonId) async throws -> [Team]
    func insertTeam(_ team: Team) async throws
    func insertTeams(_ teams: [Team]) async throws
}

protocol Preferences {
    /// The season the user picked, now and every time it changes.
    func selectedSeason() -> AsyncStream<SeasonId?>
    /// When the cache was last brought up to date (milliseconds), now and every time it changes.
    func lastSync() -> AsyncStream<Int?>
    func resolveSelectedSeason() async -> SeasonId?
    func selectSeason(_ season: SeasonId?) async
    func setLastSync(_ time: Int) async
}

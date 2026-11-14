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

protocol AppVersionRemote {
    /// Whether this build is at least the minimum version Remote Config asks for. When that cannot
    /// be found out (offline, throttled) the app is let through.
    func isSupported() async -> Bool
}

protocol MatchRemote {
    func fetchMatches(_ seasonId: SeasonId) async throws -> [Match]
    func fetchMatchEvents(_ seasonId: SeasonId, matchId: MatchId) async throws -> [MatchEvent]
    func fetchMatchLineup(_ seasonId: SeasonId, matchId: MatchId) async throws -> [MatchPlayer]
}

protocol PlayerRemote {
    func fetchPlayer(_ id: PlayerId) async throws -> Player?
    func fetchPlayers() async throws -> [Player]
    func playerExists(_ id: PlayerId) async throws -> Bool
    /// Writes the player and their squad entry for `seasonPlayer.seasonId` atomically, so a player
    /// never ends up created but missing from the season.
    func insertPlayer(_ player: Player, seasonPlayer: SeasonPlayer) async throws
}

protocol SeasonRemote {
    func fetchSeasons() async throws -> [Season]
    func fetchSquad(_ season: SeasonId) async throws -> [SeasonPlayer]
}

protocol StorageRemote {
    /// - Parameters:
    ///   - id: TeamId used as the logo's file name.
    ///   - url: File url of the image to be uploaded.
    func insertTeamLogo(_ id: TeamId, url: URL) async -> FirebaseInsertResult
    /// - Parameters:
    ///   - id: PlayerId used as the folder where the player's pictures live.
    ///   - type: Which of the player's pictures is being uploaded.
    ///   - url: File url of the image to be uploaded.
    func insertPlayerImage(_ id: PlayerId, type: PictureType, url: URL) async -> FirebaseInsertResult
}

protocol TeamRemote {
    func fetchTeams(_ seasonId: SeasonId) async throws -> [Team]
    func teamExists(_ seasonId: SeasonId, teamId: TeamId) async throws -> Bool
    func insertTeam(_ seasonId: SeasonId, team: Team) async throws
}

protocol UsersRemote {
    func getUser(_ uid: UserId) async -> UserSession?
    func insertUser(_ user: User) async -> Bool
    func isUserInserted(_ uid: UserId) async -> User?
}

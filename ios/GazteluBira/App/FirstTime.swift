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

/// Keeps the local cache in step with Firestore.
///
/// The first sync brings every season. After that, each launch only brings back what can still
/// change: the players, the season list, the current season and the one on screen. A season that
/// is over is not edited anymore, and syncing all of them on every launch costs about two thousand
/// reads per user.
final class FirstTime: InitAppHandler {

    private let appVersion: AppVersionRemote
    private let matchLocal: MatchLocal
    private let matchRemote: MatchRemote
    private let preferences: Preferences
    private let playerLocal: PlayerLocal
    private let playerRemote: PlayerRemote
    private let seasonLocal: SeasonLocal
    private let seasonRemote: SeasonRemote
    private let teamLocal: TeamLocal
    private let teamRemote: TeamRemote

    init(
        appVersion: AppVersionRemote,
        matchLocal: MatchLocal,
        matchRemote: MatchRemote,
        preferences: Preferences,
        playerLocal: PlayerLocal,
        playerRemote: PlayerRemote,
        seasonLocal: SeasonLocal,
        seasonRemote: SeasonRemote,
        teamLocal: TeamLocal,
        teamRemote: TeamRemote
    ) {
        self.appVersion = appVersion
        self.matchLocal = matchLocal
        self.matchRemote = matchRemote
        self.preferences = preferences
        self.playerLocal = playerLocal
        self.playerRemote = playerRemote
        self.seasonLocal = seasonLocal
        self.seasonRemote = seasonRemote
        self.teamLocal = teamLocal
        self.teamRemote = teamRemote
    }

    func updateAvailable() async -> Bool {
        await !appVersion.isSupported()
    }

    /// Everything else hangs from a season, so without any stored there is nothing to show.
    func hasLocalData() async -> Bool {
        let seasons = (try? await seasonLocal.getSeasons()) ?? []
        return !seasons.isEmpty
    }

    func firstTimeInit() async -> Result<Bool, Error> {
        do {
            try await sync { _ in true }
            return .success(true)
        } catch {
            return .failure(error)
        }
    }

    func initApp() async {
        do {
            let shown = await preferences.resolveSelectedSeason()
            try await sync { season in season.isCurrent || season.id == shown }
        } catch {
            Log.w("Refresh failed, the cached data stays", error)
        }
    }

    /// The players and the season list come every time: they are small, and every season points
    /// at them.
    ///
    /// - Parameter refresh: Which seasons get their squad, teams and matches downloaded again.
    private func sync(refresh: (Season) -> Bool) async throws {
        try await playerLocal.insertPlayers(playerRemote.fetchPlayers())

        let seasons = try await seasonRemote.fetchSeasons()
        try await seasonLocal.insertSeasons(seasons)

        for season in seasons where refresh(season) {
            try await syncSeason(season.id)
        }

        await preferences.setLastSync(getActualTimeAsLong())
    }

    /// The matches go in parallel: each one is two reads of its own, and one after another they
    /// made the wait as long as the season.
    private func syncSeason(_ seasonId: SeasonId) async throws {
        try await seasonLocal.insertSeasonPlayers(seasonRemote.fetchSquad(seasonId))
        try await teamLocal.insertTeams(teamRemote.fetchTeams(seasonId))

        let matches = try await matchRemote.fetchMatches(seasonId)
        try await matchLocal.insertMatches(matches)

        try await withThrowingTaskGroup(of: Void.self) { group in
            for match in matches {
                group.addTask { try await self.syncMatch(seasonId, matchId: match.id) }
            }
            try await group.waitForAll()
        }
    }

    private func syncMatch(_ seasonId: SeasonId, matchId: MatchId) async throws {
        try await matchLocal.insertMatchEvents(
            matchId,
            events: matchRemote.fetchMatchEvents(seasonId, matchId: matchId)
        )

        let lineup = try await matchRemote.fetchMatchLineup(seasonId, matchId: matchId)
        try await matchLocal.insertMatchPlayers(matchId, players: lineup.map { $0.lineup })
        try await playerLocal.insertMatchStats(matchId, stats: lineup.map { $0.stats })
    }
}

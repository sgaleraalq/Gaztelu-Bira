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

/// The iOS counterpart of Android's `TeamDao` and `RoomTeam`.
final class SQLiteTeam: TeamLocal {

    private let database: SQLiteDatabase

    init(database: SQLiteDatabase) {
        self.database = database
    }

    /// A team that is not stored comes back as `Team.errorTeam` instead of failing, as on Android.
    func getTeam(_ teamId: TeamId) async throws -> Team {
        let team = try await database.run { connection in
            try connection.query(
                "SELECT * FROM TeamEntity WHERE id = ?",
                [.text(teamId.value)]
            ) { try Team(row: $0) }.first
        }
        return team ?? Team.errorTeam
    }

    func getTeams(_ seasonId: SeasonId) async throws -> [Team] {
        try await database.run { connection in
            try connection.query(
                "SELECT * FROM TeamEntity WHERE seasonId = ?",
                [.text(seasonId.value)]
            ) { try Team(row: $0) }
        }
    }

    func insertTeam(_ team: Team) async throws {
        try await insertTeams([team])
    }

    func insertTeams(_ teams: [Team]) async throws {
        try await database.run { connection in
            try connection.transaction {
                for team in teams {
                    try connection.execute(
                        "INSERT OR REPLACE INTO TeamEntity (id, seasonId, name, logo) VALUES (?, ?, ?, ?)",
                        team.values
                    )
                }
            }
        }
    }
}

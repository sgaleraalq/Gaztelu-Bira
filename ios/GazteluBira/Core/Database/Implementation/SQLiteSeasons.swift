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

/// The iOS counterpart of Android's `SeasonDao` and `RoomSeasons`.
final class SQLiteSeasons: SeasonLocal {

    private let database: SQLiteDatabase

    init(database: SQLiteDatabase) {
        self.database = database
    }

    func getSeasons() async throws -> [Season] {
        try await database.run { connection in
            try connection.query("SELECT * FROM SeasonEntity") { try Season(row: $0) }
        }
    }

    func getSeasonPlayer(_ playerId: PlayerId, seasonId: SeasonId) async throws -> SeasonPlayer {
        let player = try await database.run { connection in
            try connection.query(
                "SELECT * FROM SeasonPlayerEntity WHERE seasonId = ? AND playerId = ?",
                [.text(seasonId.value), .text(playerId.value)]
            ) { try SeasonPlayer(row: $0) }.first
        }
        guard let player else {
            throw DatabaseError(message: "Player \(playerId) is not in the squad of \(seasonId)")
        }
        return player
    }

    func getSeasonPlayers(_ season: SeasonId) async throws -> [SeasonPlayer] {
        try await database.run { connection in
            try connection.query(
                "SELECT * FROM SeasonPlayerEntity WHERE seasonId = ? ORDER BY dorsal IS NULL, dorsal",
                [.text(season.value)]
            ) { try SeasonPlayer(row: $0) }
        }
    }

    func getSquad(_ season: SeasonId) async throws -> [SquadPlayer] {
        try await database.run { connection in
            try connection.query(
                """
                SELECT p.*, s.dorsal, s.position
                FROM SeasonPlayerEntity s
                INNER JOIN PlayerEntity p ON p.id = s.playerId
                WHERE s.seasonId = ?
                ORDER BY s.dorsal IS NULL, s.dorsal
                """,
                [.text(season.value)]
            ) { try SquadPlayer(row: $0) }
        }
    }

    func insertSeasons(_ seasons: [Season]) async throws {
        try await database.run { connection in
            try connection.transaction {
                for season in seasons {
                    try connection.execute(
                        "INSERT OR REPLACE INTO SeasonEntity (id, name, isCurrent, startsAt, endsAt) VALUES (?, ?, ?, ?, ?)",
                        season.values
                    )
                }
            }
        }
    }

    func insertSeasonPlayers(_ players: [SeasonPlayer]) async throws {
        try await database.run { connection in
            try connection.transaction {
                for player in players {
                    try connection.execute(
                        "INSERT OR REPLACE INTO SeasonPlayerEntity (seasonId, playerId, dorsal, position) VALUES (?, ?, ?, ?)",
                        player.values
                    )
                }
            }
        }
    }
}

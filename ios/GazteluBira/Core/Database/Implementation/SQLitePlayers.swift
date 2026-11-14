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

/// The iOS counterpart of Android's `PlayerDao` and `RoomPlayers`.
final class SQLitePlayers: PlayerLocal {

    private static let insertPlayerSql =
        "INSERT OR REPLACE INTO PlayerEntity (id, name, nickname, faceImage, bodyImage) VALUES (?, ?, ?, ?, ?)"

    private let database: SQLiteDatabase

    init(database: SQLiteDatabase) {
        self.database = database
    }

    func getPlayer(_ id: PlayerId) async throws -> Player {
        let player = try await database.run { connection in
            try connection.query(
                "SELECT * FROM PlayerEntity WHERE id = ?",
                [.text(id.value)]
            ) { try Player(row: $0) }.first
        }
        guard let player else { throw DatabaseError(message: "Player \(id) is not stored") }
        return player
    }

    func getPlayers() async throws -> [Player] {
        try await database.run { connection in
            try connection.query("SELECT * FROM PlayerEntity") { try Player(row: $0) }
        }
    }

    func getMatchPlayerStats(_ matchId: MatchId) async throws -> [PlayerStats] {
        try await database.run { connection in
            try connection.query(
                "SELECT * FROM PlayerStatsEntity WHERE matchId = ?",
                [.text(matchId.value)]
            ) { try PlayerStats(row: $0) }
        }
    }

    func getSeasonPlayerStats(_ seasonId: SeasonId) async throws -> [PlayerStats] {
        try await database.run { connection in
            try connection.query(
                """
                SELECT PlayerStatsEntity.* FROM PlayerStatsEntity
                INNER JOIN MatchEntity ON MatchEntity.id = PlayerStatsEntity.matchId
                WHERE MatchEntity.seasonId = ?
                """,
                [.text(seasonId.value)]
            ) { try PlayerStats(row: $0) }
        }
    }

    func getSeasonPlayerStats(_ playerId: PlayerId, seasonId: SeasonId) async throws -> [PlayerStats] {
        try await database.run { connection in
            try connection.query(
                """
                SELECT PlayerStatsEntity.* FROM PlayerStatsEntity
                INNER JOIN MatchEntity ON MatchEntity.id = PlayerStatsEntity.matchId
                WHERE MatchEntity.seasonId = ? AND PlayerStatsEntity.playerId = ?
                """,
                [.text(seasonId.value), .text(playerId.value)]
            ) { try PlayerStats(row: $0) }
        }
    }

    func insertPlayer(_ player: Player) async throws {
        try await insertPlayers([player])
    }

    func insertPlayers(_ players: [Player]) async throws {
        try await database.run { connection in
            try connection.transaction {
                for player in players {
                    try connection.execute(SQLitePlayers.insertPlayerSql, player.values)
                }
            }
        }
    }

    /// The match's stats are swapped as a whole, as its events and lineup are.
    func insertMatchStats(_ matchId: MatchId, stats: [PlayerStats]) async throws {
        try await database.run { connection in
            try connection.transaction {
                try connection.execute(
                    "DELETE FROM PlayerStatsEntity WHERE matchId = ?",
                    [.text(matchId.value)]
                )
                for stat in stats {
                    try connection.execute(
                        """
                        INSERT INTO PlayerStatsEntity (
                            matchId, playerId, assists, cleanSheets, fails, goals, goalsProvoked,
                            penaltiesProvoked, saves, blueCards, redCards, yellowCards
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """,
                        stat.values(matchId: matchId)
                    )
                }
            }
        }
    }
}

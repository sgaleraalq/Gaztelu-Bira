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

/// The iOS counterpart of Android's `MatchDao` and `RoomMatches`: the same queries, written out.
final class SQLiteMatches: MatchLocal {

    private let database: SQLiteDatabase

    init(database: SQLiteDatabase) {
        self.database = database
    }

    func getMatch(_ matchId: MatchId) async throws -> Match {
        let match = try await database.run { connection in
            try connection.query(
                "SELECT * FROM MatchEntity WHERE id = ?",
                [.text(matchId.value)]
            ) { try MatchEntity(row: $0).asModel() }.first
        }
        guard let match else { throw DatabaseError(message: "Match \(matchId) is not stored") }
        return match
    }

    func getMatches(_ seasonId: SeasonId) async throws -> [Match] {
        try await database.run { connection in
            try connection.query(
                "SELECT * FROM MatchEntity WHERE seasonId = ?",
                [.text(seasonId.value)]
            ) { try MatchEntity(row: $0).asModel() }
        }
    }

    func getMatchEvents(_ matchId: MatchId) async throws -> [MatchEvent] {
        try await database.run { connection in
            try connection.query(
                "SELECT * FROM MatchEventEntity WHERE matchId = ?",
                [.text(matchId.value)]
            ) { try MatchEventEntity(row: $0).asModel() }
        }
    }

    func getMatchLineupPlayer(_ matchId: MatchId) async throws -> [MatchLineupPlayer] {
        try await database.run { connection in
            try connection.query(
                "SELECT * FROM MatchLineupPlayerEntity WHERE matchId = ?",
                [.text(matchId.value)]
            ) { try MatchLineupPlayerEntity(row: $0).asModel() }
        }
    }

    /// An upsert and not a replace, as on Android: replacing the row would delete it first, and
    /// the cascade would take the match's events, lineup and stats with it.
    func insertMatches(_ matches: [Match]) async throws {
        let entities = matches.map { $0.asEntity() }
        try await database.run { connection in
            try connection.transaction {
                for entity in entities {
                    try connection.execute(
                        """
                        INSERT INTO MatchEntity (
                            id, seasonId, date, description, location, journey, name, round, type,
                            formation, localGoals, localTeam, visitorGoals, visitorTeam
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        ON CONFLICT(id) DO UPDATE SET
                            seasonId = excluded.seasonId,
                            date = excluded.date,
                            description = excluded.description,
                            location = excluded.location,
                            journey = excluded.journey,
                            name = excluded.name,
                            round = excluded.round,
                            type = excluded.type,
                            formation = excluded.formation,
                            localGoals = excluded.localGoals,
                            localTeam = excluded.localTeam,
                            visitorGoals = excluded.visitorGoals,
                            visitorTeam = excluded.visitorTeam
                        """,
                        [
                            .text(entity.id.value),
                            .text(entity.seasonId.value),
                            .integer(entity.date),
                            .text(entity.description),
                            .text(entity.location),
                            .integer(entity.journey),
                            .text(entity.name),
                            .text(entity.round),
                            .text(entity.type),
                            .text(entity.formation),
                            .integer(entity.localGoals),
                            .text(entity.localTeam.value),
                            .integer(entity.visitorGoals),
                            .text(entity.visitorTeam.value)
                        ]
                    )
                }
            }
        }
    }

    /// The match's events are swapped as a whole, so an event removed remotely does not linger
    /// here.
    func insertMatchEvents(_ matchId: MatchId, events: [MatchEvent]) async throws {
        let entities = events.map { $0.asEntity(matchId: matchId) }
        try await database.run { connection in
            try connection.transaction {
                try connection.execute(
                    "DELETE FROM MatchEventEntity WHERE matchId = ?",
                    [.text(matchId.value)]
                )
                for entity in entities {
                    try connection.execute(
                        """
                        INSERT INTO MatchEventEntity (
                            matchId, "order", seasonId, type, scorer, assist, ownGoal, scoredFor,
                            provokedBy, scored, taker
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """,
                        [
                            .text(entity.matchId.value),
                            .integer(entity.order),
                            .text(entity.seasonId.value),
                            .optional(entity.type),
                            .optional(entity.scorer),
                            .optional(entity.assist),
                            .bool(entity.ownGoal),
                            .optional(entity.scoredFor),
                            .optional(entity.provokedBy),
                            .optional(entity.scored),
                            .optional(entity.taker)
                        ]
                    )
                }
            }
        }
    }

    /// The match's players are swapped as a whole, so a player removed remotely does not linger
    /// here.
    func insertMatchPlayers(_ matchId: MatchId, players: [MatchLineupPlayer]) async throws {
        let entities = players.map { $0.asEntity(matchId: matchId) }
        try await database.run { connection in
            try connection.transaction {
                try connection.execute(
                    "DELETE FROM MatchLineupPlayerEntity WHERE matchId = ?",
                    [.text(matchId.value)]
                )
                for entity in entities {
                    try connection.execute(
                        """
                        INSERT INTO MatchLineupPlayerEntity (
                            matchId, playerId, seasonId, state, formationPosition
                        ) VALUES (?, ?, ?, ?, ?)
                        """,
                        [
                            .text(entity.matchId.value),
                            .text(entity.playerId.value),
                            .text(entity.seasonId.value),
                            .text(entity.state),
                            .optional(entity.formationPosition)
                        ]
                    )
                }
            }
        }
    }
}

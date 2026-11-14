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

private let gazteluDb = "gaztelu_bira_database"

/// The iOS counterpart of Android's Room `GazteluDatabase`: the same tables, with the same names
/// and columns, on the SQLite that ships with the system.
enum GazteluDatabase {

    /// Bump it whenever a table below changes. As on Android (`fallbackToDestructiveMigration`),
    /// a version change drops everything: the database is only a cache of Firestore, and the next
    /// launch downloads it again.
    private static let version = 12

    private static let tables = [
        "MatchEventEntity",
        "MatchLineupPlayerEntity",
        "PlayerStatsEntity",
        "MatchEntity",
        "PlayerEntity",
        "SeasonEntity",
        "SeasonPlayerEntity",
        "TeamEntity"
    ]

    private static let schema = [
        """
        CREATE TABLE IF NOT EXISTS MatchEntity (
            id TEXT NOT NULL PRIMARY KEY,
            seasonId TEXT NOT NULL,
            date INTEGER NOT NULL,
            description TEXT NOT NULL,
            location TEXT NOT NULL,
            journey INTEGER NOT NULL,
            name TEXT NOT NULL,
            round TEXT NOT NULL,
            type TEXT NOT NULL,
            formation TEXT NOT NULL,
            localGoals INTEGER NOT NULL,
            localTeam TEXT NOT NULL,
            visitorGoals INTEGER NOT NULL,
            visitorTeam TEXT NOT NULL
        )
        """,
        """
        CREATE TABLE IF NOT EXISTS MatchEventEntity (
            matchId TEXT NOT NULL,
            "order" INTEGER NOT NULL,
            seasonId TEXT NOT NULL,
            type TEXT,
            scorer TEXT,
            assist TEXT,
            ownGoal INTEGER NOT NULL,
            scoredFor TEXT,
            provokedBy TEXT,
            scored INTEGER,
            taker TEXT,
            PRIMARY KEY (matchId, "order"),
            FOREIGN KEY (matchId) REFERENCES MatchEntity(id) ON DELETE CASCADE
        )
        """,
        """
        CREATE TABLE IF NOT EXISTS MatchLineupPlayerEntity (
            matchId TEXT NOT NULL,
            playerId TEXT NOT NULL,
            seasonId TEXT NOT NULL,
            state TEXT NOT NULL,
            formationPosition INTEGER,
            PRIMARY KEY (matchId, playerId),
            FOREIGN KEY (matchId) REFERENCES MatchEntity(id) ON DELETE CASCADE
        )
        """,
        """
        CREATE TABLE IF NOT EXISTS PlayerEntity (
            id TEXT NOT NULL PRIMARY KEY,
            name TEXT NOT NULL,
            nickname TEXT,
            faceImage TEXT,
            bodyImage TEXT
        )
        """,
        """
        CREATE TABLE IF NOT EXISTS PlayerStatsEntity (
            matchId TEXT NOT NULL,
            playerId TEXT NOT NULL,
            assists INTEGER NOT NULL,
            cleanSheets REAL NOT NULL,
            fails INTEGER NOT NULL,
            goals INTEGER NOT NULL,
            goalsProvoked INTEGER NOT NULL,
            penaltiesProvoked INTEGER NOT NULL,
            saves INTEGER NOT NULL,
            blueCards INTEGER NOT NULL,
            redCards INTEGER NOT NULL,
            yellowCards INTEGER NOT NULL,
            PRIMARY KEY (matchId, playerId),
            FOREIGN KEY (matchId) REFERENCES MatchEntity(id) ON DELETE CASCADE
        )
        """,
        """
        CREATE TABLE IF NOT EXISTS SeasonEntity (
            id TEXT NOT NULL PRIMARY KEY,
            name TEXT NOT NULL,
            isCurrent INTEGER NOT NULL,
            startsAt INTEGER NOT NULL,
            endsAt INTEGER NOT NULL
        )
        """,
        """
        CREATE TABLE IF NOT EXISTS SeasonPlayerEntity (
            seasonId TEXT NOT NULL,
            playerId TEXT NOT NULL,
            dorsal INTEGER,
            position TEXT NOT NULL,
            PRIMARY KEY (seasonId, playerId)
        )
        """,
        """
        CREATE TABLE IF NOT EXISTS TeamEntity (
            id TEXT NOT NULL,
            seasonId TEXT NOT NULL,
            name TEXT NOT NULL,
            logo TEXT NOT NULL,
            PRIMARY KEY (id, seasonId)
        )
        """
    ]

    static func open() throws -> SQLiteDatabase {
        let folder = try FileManager.default.url(
            for: .applicationSupportDirectory,
            in: .userDomainMask,
            appropriateFor: nil,
            create: true
        )
        let path = folder.appendingPathComponent("\(gazteluDb).sqlite").path
        let connection = try SQLiteConnection(path: path)
        try migrate(connection)
        return SQLiteDatabase(connection: connection)
    }

    private static func migrate(_ connection: SQLiteConnection) throws {
        /// Off by default in SQLite; without it the cascades of the match tables do nothing.
        try connection.execute("PRAGMA foreign_keys = ON")

        let stored = try connection.query("PRAGMA user_version") { try $0.int("user_version") }.first ?? 0
        if stored != version {
            try connection.transaction {
                try tables.forEach { try connection.execute("DROP TABLE IF EXISTS \($0)") }
            }
        }
        try connection.transaction {
            try schema.forEach { try connection.execute($0) }
        }
        try connection.execute("PRAGMA user_version = \(version)")
    }
}

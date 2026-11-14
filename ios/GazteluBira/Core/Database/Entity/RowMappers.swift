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

/// The tables below hold exactly what their domain model holds, so the row is read straight into
/// it: the iOS counterpart of Android's one-to-one entities and their mappers (`PlayerEntity`,
/// `PlayerStatsEntity`, `SeasonEntity`, `SeasonPlayerEntity`, `TeamEntity`).

extension Player {
    init(row: SQLiteRow) throws {
        self.init(
            id: PlayerId(try row.string("id")),
            name: try row.string("name"),
            nickname: try row.optionalString("nickname"),
            faceImage: try row.optionalString("faceImage"),
            bodyImage: try row.optionalString("bodyImage")
        )
    }

    var values: [SQLiteValue] {
        [
            .text(id.value),
            .text(name),
            .optional(nickname),
            .optional(faceImage),
            .optional(bodyImage)
        ]
    }
}

extension PlayerStats {
    init(row: SQLiteRow) throws {
        self.init(
            playerId: PlayerId(try row.string("playerId")),
            matchId: MatchId(try row.string("matchId")),
            assists: try row.int("assists"),
            cleanSheets: try row.double("cleanSheets"),
            fails: try row.int("fails"),
            goals: try row.int("goals"),
            goalsProvoked: try row.int("goalsProvoked"),
            penaltiesProvoked: try row.int("penaltiesProvoked"),
            saves: try row.int("saves"),
            blueCards: try row.int("blueCards"),
            redCards: try row.int("redCards"),
            yellowCards: try row.int("yellowCards")
        )
    }

    /// - Parameter matchId: The match the row is stored under, whatever the stats themselves say.
    func values(matchId: MatchId) -> [SQLiteValue] {
        [
            .text(matchId.value),
            .text(playerId.value),
            .integer(assists),
            .real(cleanSheets),
            .integer(fails),
            .integer(goals),
            .integer(goalsProvoked),
            .integer(penaltiesProvoked),
            .integer(saves),
            .integer(blueCards),
            .integer(redCards),
            .integer(yellowCards)
        ]
    }
}

extension Season {
    init(row: SQLiteRow) throws {
        self.init(
            id: SeasonId(try row.string("id")),
            name: try row.string("name"),
            isCurrent: try row.bool("isCurrent"),
            startsAt: try row.int("startsAt"),
            endsAt: try row.int("endsAt")
        )
    }

    var values: [SQLiteValue] {
        [.text(id.value), .text(name), .bool(isCurrent), .integer(startsAt), .integer(endsAt)]
    }
}

extension SeasonPlayer {
    init(row: SQLiteRow) throws {
        self.init(
            id: PlayerId(try row.string("playerId")),
            seasonId: SeasonId(try row.string("seasonId")),
            dorsal: try row.optionalInt("dorsal"),
            position: Position(rawValue: try row.string("position")) ?? .undefined
        )
    }

    var values: [SQLiteValue] {
        [.text(seasonId.value), .text(id.value), .optional(dorsal), .text(position.rawValue)]
    }
}

/// The iOS counterpart of Android's `SquadPlayerView`: a player row joined with their squad entry.
extension SquadPlayer {
    init(row: SQLiteRow) throws {
        self.init(
            player: try Player(row: row),
            dorsal: try row.optionalInt("dorsal"),
            position: Position(rawValue: try row.string("position")) ?? .undefined
        )
    }
}

extension Team {
    init(row: SQLiteRow) throws {
        self.init(
            id: TeamId(try row.string("id")),
            seasonId: SeasonId(try row.string("seasonId")),
            name: try row.string("name"),
            logo: try row.string("logo")
        )
    }

    var values: [SQLiteValue] {
        [.text(id.value), .text(seasonId.value), .text(name), .text(logo)]
    }
}

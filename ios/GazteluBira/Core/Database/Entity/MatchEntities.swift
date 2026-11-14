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

/// What the match tables hold and how a row becomes a domain model and back: the iOS counterpart
/// of Android's `entity/` and `mapper/` packages for matches.

struct MatchEntity {
    let id: MatchId
    let seasonId: SeasonId
    /// Information
    let date: Int
    let description: String
    let location: String
    /// Competition
    let journey: Int
    let name: String
    let round: String
    let type: String
    /// Score
    let formation: String
    let localGoals: Int
    let localTeam: TeamId
    let visitorGoals: Int
    let visitorTeam: TeamId
}

extension MatchEntity {

    init(row: SQLiteRow) throws {
        id = MatchId(try row.string("id"))
        seasonId = SeasonId(try row.string("seasonId"))
        date = try row.int("date")
        description = try row.string("description")
        location = try row.string("location")
        journey = try row.int("journey")
        name = try row.string("name")
        round = try row.string("round")
        type = try row.string("type")
        formation = try row.string("formation")
        localGoals = try row.int("localGoals")
        localTeam = TeamId(try row.string("localTeam"))
        visitorGoals = try row.int("visitorGoals")
        visitorTeam = TeamId(try row.string("visitorTeam"))
    }

    func asModel() -> Match {
        Match(
            id: id,
            seasonId: seasonId,
            competition: toCompetition(),
            information: MatchInformation(date: date, description: description, location: location),
            match: MatchScore(
                formation: TeamFormation(rawValue: formation) ?? .fourFourTwo,
                isLocal: localTeam.isGBLocal(),
                localTeam: localTeam,
                visitorTeam: visitorTeam,
                score: Score(local: localGoals, visitor: visitorGoals)
            )
        )
    }

    private func toCompetition() -> MatchCompetition {
        let matchType = MatchType(rawValue: type)
            ?? (name.trimmingCharacters(in: .whitespaces).isEmpty ? .league : .cup)
        switch matchType {
        case .cup: return .cup(name: name, round: round)
        case .league: return .league(journey: journey)
        }
    }
}

extension Match {
    func asEntity() -> MatchEntity {
        MatchEntity(
            id: id,
            seasonId: seasonId,
            date: information.date,
            description: information.description,
            location: information.location,
            journey: competition.resolveJourney(),
            name: competition.resolveName(),
            round: competition.resolveRound(),
            type: competition.asMatchType().rawValue,
            formation: match.formation.rawValue,
            localGoals: match.score.local,
            localTeam: match.localTeam,
            visitorGoals: match.score.visitor,
            visitorTeam: match.visitorTeam
        )
    }
}

/// One row per event, so they live and die with their match: deleting a `MatchEntity` cascades to
/// its events.
struct MatchEventEntity {
    let matchId: MatchId
    let order: Int
    let seasonId: SeasonId
    let type: String?
    let scorer: String?
    let assist: String?
    let ownGoal: Bool
    let scoredFor: String?
    /// Penalty
    let provokedBy: String?
    let scored: Bool?
    let taker: String?
}

extension MatchEventEntity {

    init(row: SQLiteRow) throws {
        matchId = MatchId(try row.string("matchId"))
        order = try row.int("order")
        seasonId = SeasonId(try row.string("seasonId"))
        type = try row.optionalString("type")
        scorer = try row.optionalString("scorer")
        assist = try row.optionalString("assist")
        ownGoal = try row.bool("ownGoal")
        scoredFor = try row.optionalString("scoredFor")
        provokedBy = try row.optionalString("provokedBy")
        scored = try row.optionalBool("scored")
        taker = try row.optionalString("taker")
    }

    func asModel() -> MatchEvent {
        MatchEvent(
            matchId: matchId,
            seasonId: seasonId,
            type: type.flatMap { MatchEventType(rawValue: $0) },
            order: order,
            ownGoal: ownGoal,
            scoredFor: scoredFor,
            assist: assist,
            scorer: scorer,
            provokedBy: provokedBy,
            scored: scored,
            taker: taker
        )
    }
}

extension MatchEvent {
    /// - Parameter matchId: The match the row is stored under, whatever the event itself says.
    func asEntity(matchId: MatchId) -> MatchEventEntity {
        MatchEventEntity(
            matchId: matchId,
            order: order,
            seasonId: seasonId,
            type: type?.rawValue,
            scorer: scorer,
            assist: assist,
            ownGoal: ownGoal,
            scoredFor: scoredFor,
            provokedBy: provokedBy,
            scored: scored,
            taker: taker
        )
    }
}

/// One row per player called up for a match, so they live and die with their match: deleting a
/// `MatchEntity` cascades to its players.
struct MatchLineupPlayerEntity {
    let matchId: MatchId
    let playerId: PlayerId
    let seasonId: SeasonId
    let state: String
    let formationPosition: Int?
}

extension MatchLineupPlayerEntity {

    init(row: SQLiteRow) throws {
        matchId = MatchId(try row.string("matchId"))
        playerId = PlayerId(try row.string("playerId"))
        seasonId = SeasonId(try row.string("seasonId"))
        state = try row.string("state")
        formationPosition = try row.optionalInt("formationPosition")
    }

    func asModel() -> MatchLineupPlayer {
        MatchLineupPlayer(
            playerId: playerId,
            matchId: matchId,
            seasonId: seasonId,
            state: MatchPlayerState(rawValue: state) ?? .undefined,
            formationPosition: formationPosition
        )
    }
}

extension MatchLineupPlayer {
    func asEntity(matchId: MatchId) -> MatchLineupPlayerEntity {
        MatchLineupPlayerEntity(
            matchId: matchId,
            playerId: playerId,
            seasonId: seasonId,
            state: state.rawValue,
            formationPosition: formationPosition
        )
    }
}

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
import FirebaseFirestore

/// The iOS counterpart of Android's `MatchResponse*` classes and `MatchMapper`: how a document of
/// `seasons/{seasonId}/matches` reads as a `Match`.
enum MatchMapper {

    static func asModel(_ data: DocumentData, id: MatchId, seasonId: SeasonId) -> Match {
        Match(
            id: id,
            seasonId: seasonId,
            competition: competition(data.dictionary("competition")),
            information: information(data.dictionary("information")),
            match: score(data.dictionary("match"))
        )
    }

    static func asResponse(_ match: Match) -> DocumentData {
        [
            "competition": competition(match.competition),
            "information": [
                "date": Timestamp(seconds: Int64(match.information.date), nanoseconds: 0),
                "description": match.information.description,
                "location": match.information.location
            ],
            "match": [
                "formation": match.match.formation.shorten,
                "localTeam": match.match.localTeam.value,
                "visitorTeam": match.match.visitorTeam.value,
                "score": [
                    "local": match.match.score.local,
                    "visitor": match.match.score.visitor
                ]
            ],
            "deletedAt": NSNull(),
            "updatedAt": FieldValue.serverTimestamp()
        ]
    }

    private static func competition(_ competition: MatchCompetition) -> DocumentData {
        switch competition {
        case .cup(let name, let round):
            return [
                "journey": NSNull(),
                "name": name,
                "round": orNull(round),
                "type": MatchType.cup.rawValue
            ]
        case .league(let journey):
            return [
                "journey": journey,
                "name": NSNull(),
                "round": NSNull(),
                "type": MatchType.league.rawValue
            ]
        }
    }

    private static func competition(_ data: DocumentData) -> MatchCompetition {
        let name = data.string("name")
        let matchType = MatchType(rawValue: data.string("type") ?? "")
            ?? (name != nil ? .cup : .league)
        switch matchType {
        case .cup:
            return .cup(name: name ?? "", round: data.string("round") ?? "")
        case .league:
            return .league(journey: data.int("journey") ?? 0)
        }
    }

    private static func information(_ data: DocumentData) -> MatchInformation {
        MatchInformation(
            date: data.seconds("date") ?? 0,
            description: data.string("description") ?? "",
            location: data.string("location") ?? ""
        )
    }

    private static func score(_ data: DocumentData) -> MatchScore {
        let localTeam = TeamId(data.string("localTeam") ?? "")
        let score = data.dictionary("score")
        return MatchScore(
            formation: TeamFormation.findFormation(data.string("formation") ?? ""),
            isLocal: localTeam.isGBLocal(),
            localTeam: localTeam,
            visitorTeam: TeamId(data.string("visitorTeam") ?? ""),
            score: Score(local: score.int("local") ?? 0, visitor: score.int("visitor") ?? 0)
        )
    }
}

/// The iOS counterpart of Android's `MatchEventResponse` and `MatchEventMapper`.
enum MatchEventMapper {

    static func asModel(_ data: DocumentData, id: MatchId, seasonId: SeasonId) -> MatchEvent {
        MatchEvent(
            matchId: id,
            seasonId: seasonId,
            type: MatchEventType(rawValue: data.string("type") ?? ""),
            order: data.int("order") ?? 0,
            ownGoal: data.bool("ownGoal") ?? false,
            scoredFor: data.string("scoredFor"),
            assist: data.string("assist"),
            scorer: data.string("scorer"),
            provokedBy: data.string("provokedBy"),
            scored: data.bool("scored"),
            taker: data.string("taker")
        )
    }

    static func asResponse(_ event: MatchEvent) -> DocumentData {
        [
            "matchId": event.matchId.value,
            "seasonId": event.seasonId.value,
            "order": event.order,
            "type": event.type?.rawValue ?? "",
            "ownGoal": event.ownGoal,
            "scoredFor": orNull(event.scoredFor),
            "assist": orNull(event.assist),
            "scorer": orNull(event.scorer),
            "provokedBy": orNull(event.provokedBy),
            "scored": orNull(event.scored),
            "taker": orNull(event.taker),
            "updatedAt": FieldValue.serverTimestamp()
        ]
    }
}

/// The iOS counterpart of Android's `MatchPlayerResponse` and `MatchPlayerMapper`: a lineup
/// document carries both who played and what they did.
enum MatchPlayerMapper {

    static func asModel(_ data: DocumentData, id: MatchId, seasonId: SeasonId) -> MatchPlayer {
        let playerId = PlayerId(data.string("playerId") ?? "")
        return MatchPlayer(
            lineup: MatchLineupPlayer(
                playerId: playerId,
                matchId: id,
                seasonId: seasonId,
                state: MatchPlayerState(rawValue: data.string("role") ?? "") ?? .undefined,
                formationPosition: data.int("formationPosition")
            ),
            stats: PlayerStats(
                playerId: playerId,
                matchId: id,
                assists: data.int("assists") ?? 0,
                cleanSheets: data.double("cleanSheets") ?? 0,
                fails: data.int("fails") ?? 0,
                goals: data.int("goals") ?? 0,
                goalsProvoked: data.int("goalsProvoked") ?? 0,
                penaltiesProvoked: data.int("penaltiesProvoked") ?? 0,
                saves: data.int("saves") ?? 0,
                blueCards: data.int("blueCards") ?? 0,
                redCards: data.int("redCards") ?? 0,
                yellowCards: data.int("yellowCards") ?? 0
            )
        )
    }

    static func asResponse(_ player: MatchPlayer) -> DocumentData {
        [
            "playerId": player.lineup.playerId.value,
            "matchId": player.lineup.matchId.value,
            "seasonId": player.lineup.seasonId.value,
            "role": player.lineup.state.rawValue,
            "formationPosition": orNull(player.lineup.formationPosition),
            "assists": player.stats.assists,
            "cleanSheets": player.stats.cleanSheets,
            "fails": player.stats.fails,
            "goals": player.stats.goals,
            "goalsProvoked": player.stats.goalsProvoked,
            "penaltiesProvoked": player.stats.penaltiesProvoked,
            "saves": player.stats.saves,
            "blueCards": player.stats.blueCards,
            "redCards": player.stats.redCards,
            "yellowCards": player.stats.yellowCards,
            "updatedAt": FieldValue.serverTimestamp()
        ]
    }
}

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

struct MatchDetail {
    let match: Match
    let events: [MatchEvent]
    let players: [MatchLineupPlayer]
}

struct MatchEvent: Equatable {
    let matchId: MatchId
    let seasonId: SeasonId
    let type: MatchEventType?
    let order: Int
    let ownGoal: Bool
    let scoredFor: String?
    let assist: String?
    let scorer: String?
    /// Penalty
    let provokedBy: String?
    let scored: Bool?
    let taker: String?
}

enum MatchEventType: String, CaseIterable {
    case goal = "GOAL"
    case penalty = "PENALTY"
}

struct MatchLineupPlayer: Equatable {
    let playerId: PlayerId
    let matchId: MatchId
    let seasonId: SeasonId
    let state: MatchPlayerState
    let formationPosition: Int?
}

struct MatchPlayer {
    let lineup: MatchLineupPlayer
    let stats: PlayerStats
}

enum MatchPlayerState: String, CaseIterable {
    case starter = "STARTER"
    case bench = "BENCH"
    case manager = "MANAGER"
    case undefined = "UNDEFINED"
}

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

struct MatchId: FirebaseId {
    let value: String

    init(_ value: String) {
        self.value = value
    }
}

struct Match: Equatable {
    let id: MatchId
    let seasonId: SeasonId
    let competition: MatchCompetition
    let information: MatchInformation
    let match: MatchScore

    func result() -> MatchResult {
        let goalsFor = match.isLocal ? match.score.local : match.score.visitor
        let goalsAgainst = match.isLocal ? match.score.visitor : match.score.local
        if goalsFor > goalsAgainst { return .victory }
        if goalsFor < goalsAgainst { return .defeat }
        return .draw
    }
}

enum MatchCompetition: Equatable {
    case league(journey: Int)
    case cup(name: String, round: String?)

    func asMatchType() -> MatchType {
        switch self {
        case .cup: return .cup
        case .league: return .league
        }
    }

    func resolveJourney() -> Int {
        switch self {
        case .cup: return 0
        case .league(let journey): return journey
        }
    }

    func resolveName() -> String {
        switch self {
        case .cup(let name, _): return name
        case .league: return ""
        }
    }

    func resolveRound() -> String {
        switch self {
        case .cup(_, let round): return round ?? ""
        case .league: return ""
        }
    }
}

struct MatchInformation: Equatable {
    /// Seconds since 1970, as Firestore gives them.
    let date: Int
    let description: String
    let location: String
}

enum MatchResult {
    case defeat
    case draw
    case victory
}

struct MatchScore: Equatable {
    let formation: TeamFormation
    let isLocal: Bool
    let localTeam: TeamId
    let visitorTeam: TeamId
    let score: Score
}

extension TeamId {
    func isGBLocal() -> Bool {
        value == GazteluBiraUtils.gazteluBiraId
    }
}

enum MatchSide {
    case local
    case visitor
}

/// The raw values are the names Android stores, in Firestore and in the local database alike.
enum MatchType: String, CaseIterable {
    case cup = "CUP"
    case league = "LEAGUE"
}

struct Score: Equatable {
    let local: Int
    let visitor: Int
}

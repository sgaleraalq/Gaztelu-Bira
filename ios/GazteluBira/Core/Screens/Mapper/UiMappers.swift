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
import GazteluBiraUI

/// From the domain to the models the shared UI draws: the iOS counterpart of Android's `mapper/`
/// package (`TeamUiMapper`, `PlayerUiMapper`, `MatchUiMapper`, `SeasonUiMapper`).
///
/// Kotlin's `Int` is 32 bits wide and its nullable form is boxed, hence the conversions.

extension Int {
    var kotlin: Int32 { Int32(truncatingIfNeeded: self) }
    var kotlinBoxed: KotlinInt { KotlinInt(int: kotlin) }
}

/// TeamUiMapper
extension Team {
    func toGBTeam() -> GBTeam {
        GBTeam(id: id.value, name: name, logo: logo)
    }
}

/// PlayerUiMapper
extension Player {

    func toGBPlayer(dorsal: Int? = nil) -> GBPlayer {
        GBPlayer(
            id: id.value,
            name: displayName(),
            image: faceImage,
            dorsal: dorsal?.kotlinBoxed
        )
    }

    func displayName() -> String {
        nickname ?? name
    }
}

extension SquadPlayer {
    func toGBPlayer() -> GBPlayer {
        player.toGBPlayer(dorsal: dorsal)
    }
}

extension PlayerId {
    /// For ids that point to a player that may not be loaded, it still shows the id
    func toGBPlayer(_ players: [PlayerId: Player]) -> GBPlayer {
        players[self]?.toGBPlayer() ?? GBPlayer(id: value, name: "", image: nil, dorsal: nil)
    }
}

extension Position {
    func toGBPosition() -> GBPosition {
        switch self {
        case .undefined: return .undefined
        case .manager: return .manager
        case .goalkeeper: return .goalkeeper
        case .defender: return .defender
        case .midfielder: return .midfielder
        case .forward: return .forward
        }
    }
}

/// MatchUiMapper
extension Match {

    func toGBMatch(local: Team, visitor: Team) -> GBMatch {
        let type: GBMatchType
        switch competition {
        case .cup: type = .cup
        case .league: type = .league
        }
        return GBMatch(
            id: id.value,
            name: displayName(),
            type: type,
            // The domain keeps dates in seconds, toDate() expects millis
            date: (information.date * 1_000).toDate(),
            localTeam: local.toGBMatchTeam(goals: match.score.local),
            visitorTeam: visitor.toGBMatchTeam(goals: match.score.visitor),
            result: result().toGBMatchResult()
        )
    }

    // TODO
    private func displayName() -> String {
        switch competition {
        case .cup: return "Cup"
        case .league(let journey): return "Jornada \(journey)"
        }
    }
}

extension MatchResult {
    func toGBMatchResult() -> GBMatchResult {
        switch self {
        case .defeat: return .defeat
        case .draw: return .draw
        case .victory: return .victory
        }
    }
}

extension TeamFormation {
    func toLineUpFormation() -> LineUpFormation {
        switch self {
        case .fourFourTwo: return .fourFourTwo
        case .fiveThreeTwo: return .fiveThreeTwo
        }
    }
}

private extension Team {
    func toGBMatchTeam(goals: Int) -> GBMatchTeam {
        GBMatchTeam(
            isGaztelu: id.value == GazteluBiraUtils.gazteluBiraId,
            name: name,
            logo: logo,
            goals: goals.kotlin
        )
    }
}

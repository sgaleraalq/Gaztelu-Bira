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

struct TeamId: FirebaseId {
    let value: String

    init(_ value: String) {
        self.value = value
    }
}

struct Team: Equatable {
    let id: TeamId
    let seasonId: SeasonId
    let name: String
    let logo: String

    func isGaztelu() -> Bool {
        id.value == GazteluBiraUtils.gazteluBiraId
    }

    static let errorTeam = Team(
        id: TeamId(""),
        seasonId: SeasonId(""),
        name: "",
        logo: ""
    )
}

/// The raw values are the names Android stores in the local database; `shorten` is what Firestore
/// holds.
enum TeamFormation: String, CaseIterable {
    case fourFourTwo = "FOUR_FOUR_TWO"
    case fiveThreeTwo = "FIVE_THREE_TWO"

    var shorten: String {
        switch self {
        case .fourFourTwo: return "4-4-2"
        case .fiveThreeTwo: return "5-3-2"
        }
    }

    static func findFormation(_ shorten: String) -> TeamFormation {
        allCases.first { $0.shorten == shorten } ?? .fourFourTwo
    }
}

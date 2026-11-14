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

struct PlayerId: FirebaseId {
    let value: String

    init(_ value: String) {
        self.value = value
    }
}

struct Player: Equatable {
    let id: PlayerId
    let name: String
    let nickname: String?
    let faceImage: String?
    let bodyImage: String?
}

struct PlayerStats: Equatable {
    let playerId: PlayerId
    let matchId: MatchId
    let assists: Int
    let cleanSheets: Double
    let fails: Int
    let goals: Int
    let goalsProvoked: Int
    let penaltiesProvoked: Int
    let saves: Int
    let blueCards: Int
    let redCards: Int
    let yellowCards: Int
}

/// The raw values are the names Android stores, in Firestore and in the local database alike.
enum Position: String, CaseIterable {
    case undefined = "UNDEFINED"
    case manager = "MANAGER"
    case goalkeeper = "GOALKEEPER"
    case defender = "DEFENDER"
    case midfielder = "MIDFIELDER"
    case forward = "FORWARD"

    static func mapPosition(_ position: String) -> Position {
        allCases.first { $0.rawValue.caseInsensitiveCompare(position) == .orderedSame } ?? .manager
    }
}

enum Stat {
    case assist
    case cleanSheet
    case fails
    case goals
    case goalsProvoked
    case penaltiesProvoked
    case saves
    case blueCards
    case redCards
    case yellowCards
}

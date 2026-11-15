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

/// What the Cloud Function and the apps agree on. The same names live in
/// `client/functions/match-notification.js` and in Android's `MatchNotificationContract.kt`: a
/// change here has to be made in the three places.
enum MatchNotificationContract {
    static let topic = "matches"

    static let keyType = "type"
    static let keySeasonId = "seasonId"
    static let keyMatchId = "matchId"

    static let typeMatchInserted = "match_inserted"
}

/// The match a push talks about. The ids are plain strings so this folder does not need the
/// domain.
struct MatchNotificationPayload: Equatable, Sendable {

    let seasonId: String
    let matchId: String

    init(seasonId: String, matchId: String) {
        self.seasonId = seasonId
        self.matchId = matchId
    }

    /// Fails when `userInfo` is not the push of an inserted match.
    init?(userInfo: [AnyHashable: Any]) {
        guard
            userInfo[MatchNotificationContract.keyType] as? String
                == MatchNotificationContract.typeMatchInserted,
            let seasonId = userInfo[MatchNotificationContract.keySeasonId] as? String,
            let matchId = userInfo[MatchNotificationContract.keyMatchId] as? String,
            !seasonId.isEmpty,
            !matchId.isEmpty
        else {
            return nil
        }

        self.init(seasonId: seasonId, matchId: matchId)
    }
}

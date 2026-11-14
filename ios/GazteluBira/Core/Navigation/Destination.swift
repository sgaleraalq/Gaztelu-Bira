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

/// Every screen of the app, as a key of the back stack.
///
/// Keys only carry what their screen needs to be rebuilt, never the screen itself: the content of
/// each one is registered where the screens live (`DestinationEntries`).
enum Destination: Hashable {

    /// HOME
    case home
    case splash

    /// Auth screens
    case welcome
    case signUp
    case login

    /// Details screens
    case matchDetail(matchId: String)
    case playerDetail(playerId: String, isManager: Bool)

    /// Insert screens
    case insertMatch
    case insertPlayer
    case insertTeam
    case reviewPhoto
}

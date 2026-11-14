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

import SwiftUI

/// The screen each `Destination` shows. Destinations are plain keys in `Core/Navigation`, so their
/// content is wired here, next to the screens.
struct DestinationEntry: View {

    let destination: Destination
    let container: AppContainer

    var body: some View {
        switch destination {
        /// HOME
        case .home:
            HomeScreen(container: container)
        case .splash:
            SplashScreen(container: container)

        /// Auth screens
        case .welcome:
            WelcomeScreen()
        case .signUp:
            SignUpScreen(container: container)
        case .login:
            LoginScreen(container: container)

        /// Details screens
        case .matchDetail(let matchId):
            MatchDetailScreen(container: container, matchId: MatchId(matchId))
        case .playerDetail(let playerId, let isManager):
            PlayerDetailScreen(container: container, playerId: playerId, isManager: isManager)

        /// Insert screens
        case .insertMatch:
            InsertMatchScreen()
        case .insertPlayer:
            InsertPlayerScreen(container: container)
        case .insertTeam:
            InsertTeamScreen(container: container)
        case .reviewPhoto:
            /// Not wired on Android either: its entry is commented out there.
            EmptyView()
        }
    }
}

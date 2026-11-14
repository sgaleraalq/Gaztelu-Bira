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
import GazteluBiraUI

/// The welcome screen has no state and no view model, on Android either: it only needs to be told
/// what "go to login" means.
struct WelcomeScreen: View {

    @EnvironmentObject private var navState: NavigationState

    var body: some View {
        ComposeScreen { [navState] in
            WelcomeViewControllerKt.welcomeViewController(
                navigateToLogin: { navState.navigateTo(.login) }
            )
        }
    }
}

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
import FirebaseCore
import GoogleSignIn

@main
struct GazteluBiraApp: App {

    private let container: AppContainer

    init() {
        /// Before anything else: every Firebase service the container builds asks for this app.
        /// It reads `GoogleService-Info.plist`, which is not in the repository (see ios/README.md).
        FirebaseApp.configure()
        container = AppContainer()
    }

    var body: some Scene {
        WindowGroup {
            MainScreen(container: container)
                /// Google hands the user back to the app through a URL once they have signed in.
                .onOpenURL { url in
                    _ = GIDSignIn.sharedInstance.handle(url)
                }
        }
    }
}

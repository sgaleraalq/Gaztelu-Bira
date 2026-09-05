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

struct RootView: View {

    /// A reference type rather than `@State`: the callback handed to Compose is captured once,
    /// when the view controller is created, so it has to outlive the SwiftUI value re-renders.
    @StateObject private var router = AuthRouter()

    var body: some View {
        NavigationStack(path: $router.path) {
            WelcomeScreen(
                navigateToLogin: { router.navigate(to: .login) }
            )
            .toolbar(.hidden, for: .navigationBar)
            .navigationDestination(for: AuthRoute.self) { route in
                switch route {
                case .login:
                    LoginScreen()
                }
            }
        }
    }
}

#Preview {
    RootView()
}

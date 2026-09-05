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

/// Hosts the shared Compose `WelcomeView`. A `@Composable` cannot cross into Objective-C, so the
/// Kotlin side hands it over wrapped in a `UIViewController`.
struct WelcomeScreen: View {

    let navigateToLogin: () -> Void

    var body: some View {
        WelcomeViewControllerRepresentable(navigateToLogin: navigateToLogin)
            // Compose draws its own background edge to edge and applies window insets itself,
            // and it brings its own keyboard handling.
            .ignoresSafeArea(.all)
            .ignoresSafeArea(.keyboard)
    }
}

private struct WelcomeViewControllerRepresentable: UIViewControllerRepresentable {

    let navigateToLogin: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        WelcomeViewControllerKt.welcomeViewController(navigateToLogin: navigateToLogin)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

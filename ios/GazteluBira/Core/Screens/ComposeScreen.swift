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
import UIKit

/// Hosts one of the shared Compose screens. A `@Composable` cannot cross into Objective-C, so the
/// Kotlin side hands each screen over wrapped in a `UIViewController`, and this is what puts that
/// controller in a SwiftUI hierarchy.
///
/// The controller is made once and never updated: what changes afterwards travels through the
/// `IosState` the screen was built with.
struct ComposeScreen: View {

    private let make: () -> UIViewController

    init(_ make: @escaping () -> UIViewController) {
        self.make = make
    }

    var body: some View {
        ComposeViewControllerRepresentable(make: make)
            /// Compose draws its own background edge to edge and applies window insets itself,
            /// and it brings its own keyboard handling.
            .ignoresSafeArea(.all)
            .ignoresSafeArea(.keyboard)
            /// The shared screens bring their own top bar.
            .toolbar(.hidden, for: .navigationBar)
    }
}

private struct ComposeViewControllerRepresentable: UIViewControllerRepresentable {

    let make: () -> UIViewController

    func makeUIViewController(context: Context) -> UIViewController {
        make()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

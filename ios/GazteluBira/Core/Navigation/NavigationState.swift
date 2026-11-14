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
import SwiftUI

/// The iOS counterpart of Android's `NavigationState`: one back stack of `Destination`s, drawn by
/// a SwiftUI `NavigationStack` instead of Navigation 3's `NavDisplay`.
///
/// A `NavigationStack` has a fixed root and a path on top of it, so the stack is split that way
/// when SwiftUI asks: the first destination is the root, the rest is the path.
@MainActor
final class NavigationState: ObservableObject {

    @Published private(set) var backStack: [Destination]

    /// Changes every time the stack is cleared, even when the new root is the same destination as
    /// the old one. The root view is keyed by it, so a cleared stack always starts from a fresh
    /// screen with fresh view models.
    @Published private(set) var rootId = UUID()

    /// Whether a back gesture may pop the screen on top. The insert screens turn it off while they
    /// are saving, which is what their `BackHandler` does on Android.
    var isBackEnabled = true

    init(startDestination: Destination = .splash) {
        backStack = [startDestination]
    }

    var root: Destination {
        backStack[0]
    }

    var currentDestination: Destination {
        backStack[backStack.count - 1]
    }

    /// What `NavigationStack(path:)` binds to. SwiftUI writes it when the user swipes back.
    var path: [Destination] {
        get { Array(backStack.dropFirst()) }
        set { backStack = [root] + newValue }
    }

    func navigateTo(_ destination: Destination, clearStack: Bool = false) {
        if clearStack {
            backStack = [destination]
            rootId = UUID()
            isBackEnabled = true
        } else if currentDestination != destination {
            /// Navigating again to the screen on top (a double tap, for instance) is ignored.
            backStack.append(destination)
        }
    }

    func popUpTo(_ destination: Destination) {
        guard let index = backStack.lastIndex(of: destination) else { return }
        backStack = Array(backStack.prefix(index + 1))
    }

    func navigateBack() {
        if backStack.count > 1 {
            backStack.removeLast()
        }
    }
}

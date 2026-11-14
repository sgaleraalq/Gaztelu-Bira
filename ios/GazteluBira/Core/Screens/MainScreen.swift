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

/// The iOS counterpart of Android's `MainScreen`: it owns the navigation state and shows whatever
/// is on top of the back stack.
struct MainScreen: View {

    let container: AppContainer

    @StateObject private var navState = NavigationState()
    @StateObject private var toast = ToastCenter()

    var body: some View {
        NavigationStack(path: $navState.path) {
            DestinationEntry(destination: navState.root, container: container)
                /// A cleared stack gets a brand-new root, view models included.
                .id(navState.rootId)
                .navigationDestination(for: Destination.self) { destination in
                    DestinationEntry(destination: destination, container: container)
                }
        }
        .environmentObject(navState)
        .environmentObject(toast)
        .overlay(alignment: .bottom) {
            if let message = toast.message {
                ToastView(message: message)
                    .transition(.opacity)
            }
        }
        .animation(.easeInOut(duration: 0.2), value: toast.message)
        .preferredColorScheme(.dark)
        .onAppear {
            UINavigationController.isSwipeBackEnabled = { [navState] in navState.isBackEnabled }
        }
    }
}

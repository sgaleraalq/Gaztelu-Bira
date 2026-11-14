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

/// Placeholder. On Android this screen is still drawn with Android-only Compose code inside
/// `:core:screens`; it has no shared `InsertMatchView` in `:multiplatform:ui` yet, so there is
/// nothing for iOS to host. Once that view exists it gets a view controller in `iosMain` and a
/// view model here, like every other screen.
struct InsertMatchScreen: View {

    @EnvironmentObject private var navState: NavigationState

    var body: some View {
        ZStack {
            Color.black.ignoresSafeArea()
            VStack(spacing: 24) {
                Text("Insert match")
                    .font(.title2)
                    .foregroundColor(.white)
                Text("Not yet available")
                    .foregroundColor(.white.opacity(0.7))
                Button("Back") { navState.navigateBack() }
            }
        }
        .toolbar(.hidden, for: .navigationBar)
    }
}

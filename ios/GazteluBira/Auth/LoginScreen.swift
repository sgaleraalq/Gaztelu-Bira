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

/// Placeholder destination: `LoginView` already exists in `commonMain`, but it has no iOS entry
/// point yet — it still depends on the auth repository, which is Android-only for now.
struct LoginScreen: View {
    var body: some View {
        Text("Login")
            .font(.title2)
            .navigationTitle("Login")
            .navigationBarTitleDisplayMode(.inline)
    }
}

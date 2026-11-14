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

struct LoginScreen: View {

    @EnvironmentObject private var navState: NavigationState
    @StateObject private var viewModel: LoginViewModel

    private let mainViewModel: MainViewModel

    init(container: AppContainer) {
        mainViewModel = container.mainViewModel
        _viewModel = StateObject(
            wrappedValue: LoginViewModel(
                loginWithEmail: container.loginEmail,
                loginWithGoogle: container.loginGoogle
            )
        )
    }

    var body: some View {
        ComposeScreen { [viewModel, navState, mainViewModel] in
            /// The iOS counterpart of collecting `LoginEvent.LoggedIn`.
            viewModel.onLoggedIn = {
                mainViewModel.start()
                navState.navigateTo(.splash)
            }
            return AuthViewControllersKt.loginViewController(
                state: viewModel.state,
                onEmailChanged: { viewModel.onEmailChanged($0) },
                onPasswordChanged: { viewModel.onPasswordChanged($0) },
                onTogglePasswordVisibility: { viewModel.onTogglePasswordVisibility() },
                onLoginWithEmail: { viewModel.onLogin(.email) },
                onLoginWithGoogle: { viewModel.onLogin(.google) },
                navigateTo: { navState.navigateTo($0) }
            )
        }
    }
}

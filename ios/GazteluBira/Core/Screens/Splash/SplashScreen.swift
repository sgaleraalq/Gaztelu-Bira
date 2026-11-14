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

struct SplashScreen: View {

    @EnvironmentObject private var navState: NavigationState
    @StateObject private var viewModel: SplashViewModel

    init(container: AppContainer) {
        _viewModel = StateObject(wrappedValue: SplashViewModel(controller: container.splashController))
    }

    var body: some View {
        ComposeScreen { [viewModel, navState] in
            AuthViewControllersKt.splashViewController(
                state: viewModel.state,
                navigate: { viewModel.navigate(navState) }
            )
        }
    }
}

/// Unlike the other screens, the splash owns none of its state: `MainViewModel` drives the
/// app-scoped `SplashController` while the app boots, and this view model only projects that onto
/// a `SplashUiState`. The destination stays out of the state because only navigation reads it,
/// and it is read from the status itself so it cannot be picked up before boot has decided on one.
@MainActor
final class SplashViewModel: ObservableObject {

    let state: IosState<SplashUiState>

    private let controller: SplashController
    private var observation: Task<Void, Never>?

    init(controller: SplashController) {
        self.controller = controller
        state = IosState(initial: SplashUiState(phase: controller.status.toPhase()))

        let statuses = controller.statusStream()
        observation = Task { [weak self] in
            for await status in statuses {
                self?.state.value = SplashUiState(phase: status.toPhase())
            }
        }
    }

    deinit {
        observation?.cancel()
    }

    func navigate(_ navState: NavigationState) {
        guard let destination = controller.status.destination else { return }
        navState.navigateTo(destination, clearStack: true)
    }
}

private extension SplashStatus {
    func toPhase() -> SplashPhase {
        switch self {
        case .loading: return .loading
        case .completed: return .completing
        case .skipped: return .skipped
        }
    }
}

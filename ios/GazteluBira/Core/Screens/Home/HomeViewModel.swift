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
import GazteluBiraUI

@MainActor
final class HomeViewModel: ObservableObject {

    static let defaultTab: HomeTab = .stats

    let state = IosState<HomeUiState>(
        initial: IosUiStatesKt.homeUiState(selectedTab: HomeViewModel.defaultTab)
    )

    private let logoutUseCase: Logout

    init(logout: Logout, initAppHandler: InitAppHandler) {
        logoutUseCase = logout

        Task { [weak self] in
            let updateAvailable = await initAppHandler.updateAvailable()
            self?.update(updateAvailable: updateAvailable)
        }
    }

    func onTabChanged(_ tab: HomeTab) {
        update(selectedTab: tab)
    }

    func updateLogoutDialog(_ show: Bool) {
        update(showLogoutDialog: show)
    }

    func logout(_ navState: NavigationState) {
        navState.navigateTo(.login, clearStack: true)
        logoutUseCase()
    }

    /// Kotlin's `copy` reaches Swift with every parameter required; this gives the defaults back.
    private func update(
        selectedTab: HomeTab? = nil,
        showLogoutDialog: Bool? = nil,
        updateAvailable: Bool? = nil
    ) {
        let current = state.value
        state.value = current.doCopy(
            selectedTab: selectedTab ?? current.selectedTab,
            showLogoutDialog: showLogoutDialog ?? current.showLogoutDialog,
            updateAvailable: updateAvailable ?? current.updateAvailable
        )
    }
}

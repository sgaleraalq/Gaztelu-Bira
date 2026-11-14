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

/// Which of the two ways in the user chose: the iOS counterpart of `AuthState.Loading.Login`.
enum LoginProvider {
    case email
    case google
}

@MainActor
final class LoginViewModel: ObservableObject {

    let state = IosState<LoginUiState>(initial: IosUiStatesKt.loginUiState())

    /// Called once the user is in. The iOS counterpart of Android's `LoginEvent.LoggedIn`.
    var onLoggedIn: () -> Void = {}

    private let loginWithEmail: LoginEmail
    private let loginWithGoogle: LoginGoogle

    init(loginWithEmail: LoginEmail, loginWithGoogle: LoginGoogle) {
        self.loginWithEmail = loginWithEmail
        self.loginWithGoogle = loginWithGoogle
    }

    func onEmailChanged(_ value: String) {
        let user = state.value.user
        updateUser(
            user.doCopy(email: value, password: user.password, isPasswordVisible: user.isPasswordVisible)
        )
    }

    func onPasswordChanged(_ value: String) {
        let user = state.value.user
        updateUser(
            user.doCopy(email: user.email, password: value, isPasswordVisible: user.isPasswordVisible)
        )
    }

    func onTogglePasswordVisibility() {
        let current = state.value
        let user = current.user
        state.value = current.doCopy(
            user: user.doCopy(
                email: user.email,
                password: user.password,
                isPasswordVisible: !user.isPasswordVisible
            ),
            error: current.error,
            auth: current.auth
        )
    }

    func onLogin(_ provider: LoginProvider) {
        let current = state.value
        let user = current.user

        /// Only the email form has anything to validate: Google vouches for its own addresses.
        if provider == .email, let validationError = user.validate() {
            state.value = current.doCopy(
                user: user,
                error: validationError,
                auth: IosSealedStatesKt.authDefault()
            )
            return
        }

        Task {
            setAuth(provider == .email
                ? IosSealedStatesKt.authLoadingEmail()
                : IosSealedStatesKt.authLoadingGoogle())

            let result: AuthResult
            switch provider {
            case .email:
                result = await loginWithEmail(email: user.email, password: user.password)
            case .google:
                result = await loginWithGoogle()
            }

            if result.isSuccess {
                setAuth(IosSealedStatesKt.authDefault())
                onLoggedIn()
            } else {
                setAuth(IosSealedStatesKt.authError())
            }
        }
    }

    /// Editing clears the last failure — but never interrupts a login already in flight.
    private func updateUser(_ user: LoginUiState.LoginUser) {
        let current = state.value
        state.value = current.doCopy(
            user: user,
            error: nil,
            auth: IosSealedStatesKt.isAuthError(state: current.auth)
                ? IosSealedStatesKt.authDefault()
                : current.auth
        )
    }

    private func setAuth(_ auth: AuthState) {
        let current = state.value
        state.value = current.doCopy(user: current.user, error: nil, auth: auth)
    }
}

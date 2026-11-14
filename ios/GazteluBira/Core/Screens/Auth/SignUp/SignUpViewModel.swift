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
final class SignUpViewModel: ObservableObject {

    let state = IosState<SignUpUiState>(initial: IosUiStatesKt.signUpUiState())

    /// Called once the account exists. The iOS counterpart of Android's `SignUpEvent.SignedUp`.
    var onSignedUp: () -> Void = {}

    private let signUpWithEmail: SignUpEmail

    init(signUpWithEmail: SignUpEmail) {
        self.signUpWithEmail = signUpWithEmail
    }

    func onNameChange(_ value: String) {
        updateUser(copy(state.value.user, name: value))
    }

    func onEmailChange(_ value: String) {
        updateUser(copy(state.value.user, email: value))
    }

    func onPasswordChange(_ value: String) {
        updateUser(copy(state.value.user, password: value))
    }

    func onRepeatPasswordChange(_ value: String) {
        updateUser(copy(state.value.user, repeatPassword: value))
    }

    /// Revealing a password is not an edit, so it leaves any showing error alone.
    func onTogglePasswordVisibility() {
        let current = state.value
        state.value = current.doCopy(
            user: copy(current.user, passwordVisible: !current.user.passwordVisible),
            error: current.error,
            auth: current.auth
        )
    }

    func onToggleRepeatPasswordVisibility() {
        let current = state.value
        state.value = current.doCopy(
            user: copy(current.user, repeatPasswordVisible: !current.user.repeatPasswordVisible),
            error: current.error,
            auth: current.auth
        )
    }

    func signUp() {
        let current = state.value
        let user = current.user

        if let validationError = user.validate() {
            state.value = current.doCopy(
                user: user,
                error: validationError,
                auth: IosSealedStatesKt.authDefault()
            )
            return
        }

        Task {
            setAuth(IosSealedStatesKt.authLoadingSignUp())
            let result = await signUpWithEmail(name: user.name, email: user.email, password: user.password)
            if result.isSuccess {
                setAuth(IosSealedStatesKt.authDefault())
                onSignedUp()
            } else {
                setAuth(IosSealedStatesKt.authError())
            }
        }
    }

    /// Editing clears the last failure — but never interrupts a sign-up already in flight.
    private func updateUser(_ user: SignUpUiState.SignUpUser) {
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

    /// Kotlin's `copy` reaches Swift with every parameter required; this gives the defaults back.
    private func copy(
        _ user: SignUpUiState.SignUpUser,
        name: String? = nil,
        email: String? = nil,
        password: String? = nil,
        repeatPassword: String? = nil,
        passwordVisible: Bool? = nil,
        repeatPasswordVisible: Bool? = nil
    ) -> SignUpUiState.SignUpUser {
        user.doCopy(
            name: name ?? user.name,
            email: email ?? user.email,
            password: password ?? user.password,
            repeatPassword: repeatPassword ?? user.repeatPassword,
            passwordVisible: passwordVisible ?? user.passwordVisible,
            repeatPasswordVisible: repeatPasswordVisible ?? user.repeatPasswordVisible
        )
    }
}

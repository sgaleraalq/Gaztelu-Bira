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

enum AuthErrors {
    static let signUpError = "There was an error signing up"
    static let loginError = "There was an error logging in"
}

protocol AuthRepository {
    func logout()
    func getUserSession() -> String?
    func getUserRole() async -> UserRole
    func signUpWithEmail(name: String, email: String, password: String) async -> AuthResult
    func loginWithEmail(email: String, password: String) async -> AuthResult
    func loginWithGoogle() async -> AuthResult
}

/// Opens the platform's Google sign-in flow. Each platform brings its own way of asking the user
/// for an account; what comes back is always a `User`.
protocol GoogleAuth {
    /// - Returns: the signed-in user, or nil when the user backs out or the platform cannot
    ///   complete the flow.
    func signIn() async -> User?
}

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
import FirebaseAuth

private let adminClaim = "admin"

final class AuthRepositoryImpl: AuthRepository {

    private let googleSignIn: GoogleAuth
    private let auth = Auth.auth()

    init(googleSignIn: GoogleAuth) {
        self.googleSignIn = googleSignIn
    }

    func logout() {
        try? auth.signOut()
    }

    func getUserSession() -> String? {
        auth.currentUser?.uid
    }

    /// The token is refreshed first, so a claim granted since the last sign-in counts from the
    /// next launch. Offline, the cached token is the best there is; with no token at all, nobody
    /// is an admin.
    func getUserRole() async -> UserRole {
        guard let user = auth.currentUser else { return .guest }
        var claims = await self.claims(of: user, forceRefresh: true)
        if claims == nil {
            claims = await self.claims(of: user, forceRefresh: false)
        }
        return (claims?[adminClaim] as? Bool) == true ? .admin : .guest
    }

    func signUpWithEmail(name: String, email: String, password: String) async -> AuthResult {
        do {
            let user = try await auth.createUser(withEmail: email, password: password).user
            try await updateUserName(name, user: user)
            return .success(
                user: User(
                    uid: UserId(user.uid),
                    name: user.displayName,
                    email: user.email
                )
            )
        } catch {
            return .error(message: error.localizedDescription, cause: error)
        }
    }

    func loginWithEmail(email: String, password: String) async -> AuthResult {
        do {
            let user = try await auth.signIn(withEmail: email, password: password).user
            return .success(
                user: User(
                    uid: UserId(user.uid),
                    name: user.displayName,
                    email: user.email
                )
            )
        } catch {
            return .error(message: error.localizedDescription, cause: error)
        }
    }

    func loginWithGoogle() async -> AuthResult {
        guard let user = await googleSignIn.signIn() else {
            return .error(message: AuthErrors.loginError)
        }
        return .success(user: user)
    }

    private func updateUserName(_ name: String, user: FirebaseAuth.User) async throws {
        let request = user.createProfileChangeRequest()
        request.displayName = name
        try await request.commitChanges()
    }

    private func claims(of user: FirebaseAuth.User, forceRefresh: Bool) async -> [String: Any]? {
        try? await user.getIDTokenResult(forcingRefresh: forceRefresh).claims
    }
}

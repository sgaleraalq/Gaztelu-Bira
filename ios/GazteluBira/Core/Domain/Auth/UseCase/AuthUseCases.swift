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

final class LoginEmail {

    private let repository: AuthRepository

    init(repository: AuthRepository) {
        self.repository = repository
    }

    func callAsFunction(email: String, password: String) async -> AuthResult {
        await repository.loginWithEmail(email: email, password: password)
    }
}

/// Google does not tell a login from a sign up, so a first-time account is stored on the way in.
final class LoginGoogle {

    private let repository: AuthRepository
    private let isUserInserted: IsUserInserted
    private let insertUser: InsertUser

    init(repository: AuthRepository, isUserInserted: IsUserInserted, insertUser: InsertUser) {
        self.repository = repository
        self.isUserInserted = isUserInserted
        self.insertUser = insertUser
    }

    func callAsFunction() async -> AuthResult {
        let authResult = await repository.loginWithGoogle()
        guard case .success(let user) = authResult else { return authResult }
        if let stored = await isUserInserted(user.uid) { return .success(user: stored) }
        return await insertUser(user) ? authResult : .error()
    }
}

final class Logout {

    private let sessionManager: SessionManager

    init(sessionManager: SessionManager) {
        self.sessionManager = sessionManager
    }

    func callAsFunction() {
        sessionManager.logout()
    }
}

/// Entry point of sign up with Firebase
final class SignUpEmail {

    private let repository: AuthRepository
    private let insertUser: InsertUser

    init(repository: AuthRepository, insertUser: InsertUser) {
        self.repository = repository
        self.insertUser = insertUser
    }

    func callAsFunction(name: String, email: String, password: String) async -> AuthResult {
        let authResult = await repository.signUpWithEmail(name: name, email: email, password: password)
        if case .success(let user) = authResult {
            _ = await insertUser(user)
        }
        return authResult
    }
}

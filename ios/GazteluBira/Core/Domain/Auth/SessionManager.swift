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

/// Single source of truth for who is using the app. Any view model that cares about the user (or
/// only about whether they are an admin) takes this instead of having a screen hand it down.
final class SessionManager {

    private let auth: AuthRepository
    private let users: UsersRemote

    private let _session = MutableStateFlow<UserSession?>(nil)

    init(auth: AuthRepository, users: UsersRemote) {
        self.auth = auth
        self.users = users
    }

    var session: UserSession? { _session.value }

    func sessionStream() -> AsyncStream<UserSession?> {
        _session.stream()
    }

    func isAdmin() -> AsyncStream<Bool> {
        _session.stream()
            .mapAsync { $0?.isAdmin() == true }
            .distinctUntilChanged()
    }

    func isAuthenticated() -> Bool {
        auth.getUserSession() != nil
    }

    /// Fetches the profile of whoever Firebase has signed in. Without one the session is cleared.
    ///
    /// The role does not come from the profile but from the token, which is what the Firestore
    /// rules check: what the app offers and what the rules allow can never disagree.
    func load() async {
        guard let uid = auth.getUserSession(), let profile = await users.getUser(UserId(uid)) else {
            _session.value = nil
            return
        }
        var user = profile.session
        user.role = await auth.getUserRole()
        _session.value = UserSession(session: user, team: profile.team, player: profile.player)
    }

    func logout() {
        auth.logout()
        _session.value = nil
    }
}

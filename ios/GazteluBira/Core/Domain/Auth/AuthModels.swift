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

enum AuthResult {
    case success(user: User)
    case error(message: String = "There was an error", cause: Error? = nil)

    var isSuccess: Bool {
        if case .success = self { return true }
        return false
    }
}

struct UserSession: Equatable {
    let session: User
    let team: Team
    var player: PlayerId? = nil

    func isAdmin() -> Bool {
        session.role == .admin
    }
}

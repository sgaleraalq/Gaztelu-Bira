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

struct UserId: FirebaseId {
    let value: String

    init(_ value: String) {
        self.value = value
    }
}

struct User: Equatable {
    let uid: UserId
    let name: String?
    let email: String?
    var img: String? = nil
    var role: UserRole = .guest
}

enum UserRole: String, CaseIterable {
    case guest = "GUEST"
    case admin = "ADMIN"

    static func userRoleOf(_ value: String) -> UserRole {
        allCases.first { $0.rawValue.caseInsensitiveCompare(value) == .orderedSame } ?? .guest
    }
}

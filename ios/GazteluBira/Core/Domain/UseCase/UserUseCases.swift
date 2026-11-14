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

final class GetUser {

    private let repository: UsersRemote

    init(repository: UsersRemote) {
        self.repository = repository
    }

    func callAsFunction(_ uid: UserId) async -> UserSession? {
        await repository.getUser(uid)
    }
}

final class InsertUser {

    private let repository: UsersRemote

    init(repository: UsersRemote) {
        self.repository = repository
    }

    func callAsFunction(_ user: User) async -> Bool {
        await repository.insertUser(user)
    }
}

final class IsUserInserted {

    private let repository: UsersRemote

    init(repository: UsersRemote) {
        self.repository = repository
    }

    func callAsFunction(_ uid: UserId) async -> User? {
        await repository.isUserInserted(uid)
    }
}

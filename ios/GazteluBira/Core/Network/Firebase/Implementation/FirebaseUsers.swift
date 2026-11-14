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
import FirebaseFirestore

final class FirebaseUsers: UsersRemote {

    private let firestore: Firestore

    init(firestore: Firestore) {
        self.firestore = firestore
    }

    func getUser(_ uid: UserId) async -> UserSession? {
        let team = getUserTeam()
        let playerId = getPlayerId(uid)
        guard let user = await getUserInformation(uid) else { return nil }
        return UserSession(session: user, team: team, player: playerId)
    }

    /// Merged rather than replaced: only the profile is written and whatever else the document
    /// holds stays, so a sign-in that could not tell the user was already stored wipes nothing.
    func insertUser(_ user: User) async -> Bool {
        do {
            try await firestore
                .collection(FirebaseConstants.users)
                .document(user.uid.value)
                .setData(UserMapper.asResponse(user), merge: true)
            return true
        } catch {
            return false
        }
    }

    func isUserInserted(_ uid: UserId) async -> User? {
        await getUserInformation(uid)
    }

    private func getUserInformation(_ uid: UserId) async -> User? {
        let snapshot = try? await firestore
            .collection(FirebaseConstants.users)
            .document(uid.value)
            .getDocument()
        return snapshot?.data().map { UserMapper.asModel($0, id: uid) }
    }

    private func getPlayerId(_ uid: UserId) -> PlayerId {
        PlayerId("TODO")
    }

    private func getUserTeam() -> Team {
        GazteluBiraUtils.gazteluBira
    }
}

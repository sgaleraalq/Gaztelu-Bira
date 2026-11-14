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

final class FirestoreTeam: TeamRemote {

    private let firestore: Firestore

    init(firestore: Firestore) {
        self.firestore = firestore
    }

    func fetchTeams(_ seasonId: SeasonId) async throws -> [Team] {
        try await teams(seasonId)
            .getDocuments()
            .mapDocuments { team in
                TeamMapper.asModel(team.data(), id: TeamId(team.documentID), seasonId: seasonId)
            }
    }

    func teamExists(_ seasonId: SeasonId, teamId: TeamId) async throws -> Bool {
        try await teams(seasonId).document(teamId.value).getDocument().exists
    }

    func insertTeam(_ seasonId: SeasonId, team: Team) async throws {
        try await teams(seasonId)
            .document(team.id.value)
            .setData(TeamMapper.asResponse(team))
    }

    private func teams(_ season: SeasonId) -> CollectionReference {
        firestore
            .collection(FirebaseConstants.seasons)
            .document(season.value)
            .collection(FirebaseConstants.teams)
    }
}

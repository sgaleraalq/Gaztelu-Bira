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

final class FirestoreSeason: SeasonRemote {

    private let firestore: Firestore

    init(firestore: Firestore) {
        self.firestore = firestore
    }

    func fetchSeasons() async throws -> [Season] {
        try await seasons()
            .getDocuments()
            .mapDocuments { season in
                SeasonMapper.asModel(season.data(), id: SeasonId(season.documentID))
            }
    }

    func fetchSquad(_ season: SeasonId) async throws -> [SeasonPlayer] {
        try await seasons()
            .document(season.value)
            .collection(FirebaseConstants.squad)
            .getDocuments()
            .mapDocuments { player in
                SeasonPlayerMapper.asModel(
                    player.data(),
                    seasonId: season,
                    playerId: PlayerId(player.documentID)
                )
            }
    }

    private func seasons() -> CollectionReference {
        firestore.collection(FirebaseConstants.seasons)
    }
}

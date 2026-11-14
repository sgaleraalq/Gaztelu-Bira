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

final class FirestorePlayers: PlayerRemote {

    private let firestore: Firestore

    init(firestore: Firestore) {
        self.firestore = firestore
    }

    func fetchPlayer(_ id: PlayerId) async throws -> Player? {
        let document = try await players().document(id.value).getDocument()
        return document.data().map { PlayerMapper.asModel($0, id: id) }
    }

    func fetchPlayers() async throws -> [Player] {
        try await players()
            .getDocuments()
            .mapDocuments { document in
                PlayerMapper.asModel(document.data(), id: PlayerId(document.documentID))
            }
    }

    func playerExists(_ id: PlayerId) async throws -> Bool {
        try await players().document(id.value).getDocument().exists
    }

    func insertPlayer(_ player: Player, seasonPlayer: SeasonPlayer) async throws {
        let squadEntry = firestore
            .collection(FirebaseConstants.seasons)
            .document(seasonPlayer.seasonId.value)
            .collection(FirebaseConstants.squad)
            .document(seasonPlayer.id.value)

        let batch = firestore.batch()
        batch.setData(PlayerMapper.asResponse(player), forDocument: players().document(player.id.value))
        batch.setData(SeasonPlayerMapper.asResponse(seasonPlayer), forDocument: squadEntry)
        try await batch.commit()
    }

    private func players() -> CollectionReference {
        firestore.collection(FirebaseConstants.players)
    }
}

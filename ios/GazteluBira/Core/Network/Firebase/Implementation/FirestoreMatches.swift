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

final class FirestoreMatches: MatchRemote {

    private let firestore: Firestore

    init(firestore: Firestore) {
        self.firestore = firestore
    }

    func fetchMatches(_ seasonId: SeasonId) async throws -> [Match] {
        try await matches(seasonId)
            .getDocuments()
            .mapDocuments { match in
                MatchMapper.asModel(match.data(), id: MatchId(match.documentID), seasonId: seasonId)
            }
    }

    func fetchMatchEvents(_ seasonId: SeasonId, matchId: MatchId) async throws -> [MatchEvent] {
        try await matches(seasonId)
            .document(matchId.value)
            .collection(FirebaseConstants.events)
            .getDocuments()
            .mapDocuments { event in
                MatchEventMapper.asModel(event.data(), id: matchId, seasonId: seasonId)
            }
    }

    func fetchMatchLineup(_ seasonId: SeasonId, matchId: MatchId) async throws -> [MatchPlayer] {
        try await matches(seasonId)
            .document(matchId.value)
            .collection(FirebaseConstants.lineup)
            .getDocuments()
            .mapDocuments { player in
                MatchPlayerMapper.asModel(player.data(), id: matchId, seasonId: seasonId)
            }
    }

    private func matches(_ season: SeasonId) -> CollectionReference {
        firestore
            .collection(FirebaseConstants.seasons)
            .document(season.value)
            .collection(FirebaseConstants.matches)
    }
}

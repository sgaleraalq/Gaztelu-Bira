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

/// Creates a brand-new player and adds them to the selected season's squad.
final class InsertPlayer {

    private let storage: StorageRemote
    private let selectedSeason: SelectedSeason
    private let playerLocal: PlayerLocal
    private let playerRemote: PlayerRemote
    private let seasonLocal: SeasonLocal

    init(
        storage: StorageRemote,
        selectedSeason: SelectedSeason,
        playerLocal: PlayerLocal,
        playerRemote: PlayerRemote,
        seasonLocal: SeasonLocal
    ) {
        self.storage = storage
        self.selectedSeason = selectedSeason
        self.playerLocal = playerLocal
        self.playerRemote = playerRemote
        self.seasonLocal = seasonLocal
    }

    /// - Parameters:
    ///   - faceImage: Optional, nil when the player has no face picture yet.
    ///   - bodyImage: Optional, nil when the player has no body picture yet.
    func callAsFunction(
        name: String,
        nickname: String?,
        dorsal: Int,
        position: Position,
        faceImage: URL?,
        bodyImage: URL?
    ) async -> FirebaseInsertResult {
        guard let season = await selectedSeason.first() else { return .noSeasonSelected }
        let cleanName = name.trimmingCharacters(in: .whitespacesAndNewlines)
        let id = PlayerId(cleanName.toFirebaseId())

        do {
            if try await playerRemote.playerExists(id) { return .alreadyExists }

            var faceUrl: String?
            if let faceImage {
                let upload = await storage.insertPlayerImage(id, type: .face, url: faceImage)
                guard case .image(let url) = upload else { return upload }
                faceUrl = url.absoluteString
            }

            var bodyUrl: String?
            if let bodyImage {
                let upload = await storage.insertPlayerImage(id, type: .body, url: bodyImage)
                guard case .image(let url) = upload else { return upload }
                bodyUrl = url.absoluteString
            }

            let cleanNickname = nickname?.trimmingCharacters(in: .whitespacesAndNewlines)
            let player = Player(
                id: id,
                name: cleanName,
                nickname: cleanNickname?.isEmpty == false ? cleanNickname : nil,
                faceImage: faceUrl,
                bodyImage: bodyUrl
            )
            let seasonPlayer = SeasonPlayer(
                id: id,
                seasonId: season,
                dorsal: dorsal,
                position: position
            )

            try await playerRemote.insertPlayer(player, seasonPlayer: seasonPlayer)
            try await playerLocal.insertPlayer(player)
            try await seasonLocal.insertSeasonPlayers([seasonPlayer])
            return .document
        } catch {
            return .unknown(message: error.localizedDescription)
        }
    }
}

final class InsertTeam {

    private let storage: StorageRemote
    private let selectedSeason: SelectedSeason
    private let teamLocal: TeamLocal
    private let teamRemote: TeamRemote

    init(
        storage: StorageRemote,
        selectedSeason: SelectedSeason,
        teamLocal: TeamLocal,
        teamRemote: TeamRemote
    ) {
        self.storage = storage
        self.selectedSeason = selectedSeason
        self.teamLocal = teamLocal
        self.teamRemote = teamRemote
    }

    func callAsFunction(name: String, image: URL) async -> FirebaseInsertResult {
        guard let season = await selectedSeason.first() else { return .noSeasonSelected }
        let cleanName = name.trimmingCharacters(in: .whitespacesAndNewlines)
        let id = TeamId(cleanName.toFirebaseId())

        do {
            if try await teamRemote.teamExists(season, teamId: id) { return .alreadyExists }

            let upload = await storage.insertTeamLogo(id, url: image)
            guard case .image(let url) = upload else { return upload }

            let team = Team(
                id: id,
                seasonId: season,
                name: cleanName,
                logo: url.absoluteString
            )

            try await teamRemote.insertTeam(season, team: team)
            try await teamLocal.insertTeam(team)
            return .document
        } catch {
            return .unknown(message: error.localizedDescription)
        }
    }
}

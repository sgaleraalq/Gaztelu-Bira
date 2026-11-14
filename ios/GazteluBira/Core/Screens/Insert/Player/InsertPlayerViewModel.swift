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
import GazteluBiraUI

@MainActor
final class InsertPlayerViewModel: ObservableObject {

    let state: IosState<InsertPlayerUiState>

    /// Whether the photo picker is on screen. The iOS counterpart of launching Android's `Gallery`.
    @Published var showGallery = false

    private let insertNewPlayer: InsertPlayer
    private let getAvailableDorsals: GetAvailableDorsals

    private var selectedPicture: PictureType = .face

    init(insertNewPlayer: InsertPlayer, getAvailableDorsals: GetAvailableDorsals) {
        self.insertNewPlayer = insertNewPlayer
        self.getAvailableDorsals = getAvailableDorsals
        state = IosState(
            initial: IosUiStatesKt.insertPlayerUiState(
                playerId: String(getActualTimeAsLong()),
                positions: Position.allCases.map { $0.rawValue }
            )
        )

        Task { [weak self] in
            let dorsals = (try? await getAvailableDorsals()) ?? []
            self?.update { $0.availableDorsals = dorsals.map { $0.kotlinBoxed } }
        }
    }

    /// InsertPlayerField, one case per function. Every edit closes whichever dialog was open.
    func onNameChanged(_ name: String) {
        update {
            $0.dialog = IosSealedStatesKt.insertPlayerDialogNone()
            $0.playerName = name
        }
    }

    func onNicknameChanged(_ nickname: String) {
        update {
            $0.dialog = IosSealedStatesKt.insertPlayerDialogNone()
            $0.playerNickname = nickname
        }
    }

    func onDorsalChanged(_ dorsal: Int) {
        update {
            $0.dialog = IosSealedStatesKt.insertPlayerDialogNone()
            $0.playerDorsal = dorsal.kotlin
        }
    }

    func onPositionChanged(_ position: String) {
        update {
            $0.dialog = IosSealedStatesKt.insertPlayerDialogNone()
            $0.playerPosition = position
        }
    }

    /// - Parameter newImage: Nil when the picture is removed.
    func onImageChanged(_ newImage: String?) {
        update {
            $0.dialog = IosSealedStatesKt.insertPlayerDialogNone()
            switch selectedPicture {
            case .face: $0.faceImage = newImage ?? ""
            case .body: $0.bodyImage = newImage ?? ""
            }
        }
    }

    func updateDialogState(_ newState: InsertPlayerDialog) {
        update { $0.dialog = newState }
    }

    func updateSelectedImage(_ picture: PictureType) {
        selectedPicture = picture
        showGallery = true
    }

    /// - Parameter onFailure: Receives the message to show. The form keeps its fields so the user
    ///   can retry.
    func insertPlayer(_ navState: NavigationState, onFailure: @escaping (String) -> Void) {
        let player = state.value
        guard IosSealedStatesKt.isInsertValid(state: player) else { return }

        Task {
            update { $0.state = IosSealedStatesKt.insertingLoading() }
            /// The iOS counterpart of Android's `BackHandler(state.isNotLoading())`.
            navState.isBackEnabled = false

            let result = await insertNewPlayer(
                name: player.playerName,
                nickname: player.playerNickname,
                dorsal: Int(player.playerDorsal),
                position: Position.mapPosition(player.playerPosition),
                faceImage: player.faceImage.isEmpty ? nil : URL(string: player.faceImage),
                bodyImage: player.bodyImage.isEmpty ? nil : URL(string: player.bodyImage)
            )

            navState.isBackEnabled = true
            if case .document = result {
                navState.navigateTo(.home, clearStack: true)
            } else {
                update { $0.state = IosSealedStatesKt.insertingDefault() }
                errorMessage(result, onResolved: onFailure)
            }
        }
    }

    private func errorMessage(_ result: FirebaseInsertResult, onResolved: @escaping (String) -> Void) {
        switch result {
        case .alreadyExists:
            IosMessagesKt.resolveMessage(message: .insertPlayerAlreadyExists, argument: nil, onResolved: onResolved)
        case .noSeasonSelected:
            IosMessagesKt.resolveMessage(message: .insertNoSeasonSelected, argument: nil, onResolved: onResolved)
        case .unknown(let message) where !message.trimmingCharacters(in: .whitespaces).isEmpty:
            IosMessagesKt.resolveMessage(message: .insertPlayerErrorReason, argument: message, onResolved: onResolved)
        default:
            IosMessagesKt.resolveMessage(message: .insertPlayerUploadError, argument: nil, onResolved: onResolved)
        }
    }

    /// Kotlin's `copy` reaches Swift with every parameter required. The fields are copied out into
    /// a Swift value, edited there and written back in one go.
    private func update(_ edit: (inout Fields) -> Void) {
        let current = state.value
        var fields = Fields(
            playerName: current.playerName,
            playerNickname: current.playerNickname,
            playerDorsal: current.playerDorsal,
            playerPosition: current.playerPosition,
            faceImage: current.faceImage,
            bodyImage: current.bodyImage,
            availableDorsals: current.availableDorsals,
            dialog: current.dialog,
            state: current.state
        )
        edit(&fields)
        state.value = current.doCopy(
            playerId: current.playerId,
            playerName: fields.playerName,
            playerNickname: fields.playerNickname,
            playerDorsal: fields.playerDorsal,
            playerPosition: fields.playerPosition,
            faceImage: fields.faceImage,
            bodyImage: fields.bodyImage,
            availableDorsals: fields.availableDorsals,
            positions: current.positions,
            dialog: fields.dialog,
            state: fields.state
        )
    }

    private struct Fields {
        var playerName: String
        var playerNickname: String
        var playerDorsal: Int32
        var playerPosition: String
        var faceImage: String
        var bodyImage: String
        var availableDorsals: [KotlinInt]
        var dialog: InsertPlayerDialog
        var state: InsertingDataState
    }
}

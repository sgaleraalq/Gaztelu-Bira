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

import SwiftUI
import GazteluBiraUI

struct InsertTeamScreen: View {

    @EnvironmentObject private var navState: NavigationState
    @EnvironmentObject private var toast: ToastCenter
    @StateObject private var viewModel: InsertTeamViewModel

    init(container: AppContainer) {
        _viewModel = StateObject(wrappedValue: InsertTeamViewModel(insertTeam: container.insertTeam))
    }

    var body: some View {
        ComposeScreen { [viewModel, navState, toast] in
            InsertViewControllersKt.insertTeamViewController(
                state: viewModel.state,
                onNameChanged: { viewModel.onNameChanged($0) },
                onImageChanged: { viewModel.onImageChanged($0) },
                openGallery: { viewModel.showGallery = true },
                insertTeam: {
                    viewModel.insertTeam(
                        navState,
                        onSuccess: { navState.navigateTo(.home, clearStack: true) },
                        onFailure: { message in toast.show(message) }
                    )
                },
                onMissingField: { toast.show($0) }
            )
        }
        .galleryPicker(isPresented: $viewModel.showGallery) { url in
            viewModel.onImageChanged(url.absoluteString)
        }
    }
}

@MainActor
final class InsertTeamViewModel: ObservableObject {

    let state = IosState<InsertTeamUiState>(
        initial: IosUiStatesKt.insertTeamUiState(teamId: String(getActualTimeAsLong()))
    )

    /// Whether the photo picker is on screen. The iOS counterpart of launching Android's `Gallery`.
    @Published var showGallery = false

    private let insertTeamUseCase: InsertTeam

    init(insertTeam: InsertTeam) {
        insertTeamUseCase = insertTeam
    }

    /// InsertTeamField, one case per function.
    func onNameChanged(_ newName: String) {
        update(teamName: newName)
    }

    /// - Parameter newImage: Nil when the logo is removed.
    func onImageChanged(_ newImage: String?) {
        update(teamImage: newImage ?? "")
    }

    func insertTeam(
        _ navState: NavigationState,
        onSuccess: @escaping () -> Void,
        onFailure: @escaping (String) -> Void
    ) {
        let team = state.value
        guard IosSealedStatesKt.isInsertValid(state: team), let image = URL(string: team.teamImage) else {
            return
        }

        Task {
            update(state: IosSealedStatesKt.insertingLoading())
            /// The iOS counterpart of Android's `BackHandler(state.isNotLoading())`.
            navState.isBackEnabled = false

            let result = await insertTeamUseCase(name: team.teamName, image: image)

            navState.isBackEnabled = true
            if case .document = result {
                onSuccess()
            } else {
                /// Keeps the filled fields so the user can fix them and retry.
                update(state: IosSealedStatesKt.insertingDefault())
                errorMessage(result, onResolved: onFailure)
            }
        }
    }

    private func errorMessage(_ result: FirebaseInsertResult, onResolved: @escaping (String) -> Void) {
        switch result {
        case .alreadyExists:
            IosMessagesKt.resolveMessage(message: .insertTeamAlreadyExists, argument: nil, onResolved: onResolved)
        case .noSeasonSelected:
            IosMessagesKt.resolveMessage(message: .insertNoSeasonSelected, argument: nil, onResolved: onResolved)
        case .unknown(let message) where !message.trimmingCharacters(in: .whitespaces).isEmpty:
            IosMessagesKt.resolveMessage(message: .insertTeamErrorReason, argument: message, onResolved: onResolved)
        default:
            IosMessagesKt.resolveMessage(message: .insertTeamError, argument: nil, onResolved: onResolved)
        }
    }

    /// Kotlin's `copy` reaches Swift with every parameter required; this gives the defaults back.
    private func update(
        teamName: String? = nil,
        teamImage: String? = nil,
        state newState: InsertingDataState? = nil
    ) {
        let current = state.value
        state.value = current.doCopy(
            teamId: current.teamId,
            teamName: teamName ?? current.teamName,
            teamImage: teamImage ?? current.teamImage,
            state: newState ?? current.state
        )
    }
}

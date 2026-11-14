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

struct InsertPlayerScreen: View {

    @EnvironmentObject private var navState: NavigationState
    @EnvironmentObject private var toast: ToastCenter
    @StateObject private var viewModel: InsertPlayerViewModel

    init(container: AppContainer) {
        _viewModel = StateObject(
            wrappedValue: InsertPlayerViewModel(
                insertNewPlayer: container.insertPlayer,
                getAvailableDorsals: container.getAvailableDorsals
            )
        )
    }

    var body: some View {
        ComposeScreen { [viewModel, navState, toast] in
            InsertViewControllersKt.insertPlayerViewController(
                state: viewModel.state,
                onNameChanged: { viewModel.onNameChanged($0) },
                onNicknameChanged: { viewModel.onNicknameChanged($0) },
                onDorsalChanged: { viewModel.onDorsalChanged($0.intValue) },
                onPositionChanged: { viewModel.onPositionChanged($0) },
                onImageChanged: { viewModel.onImageChanged($0) },
                updateDialogState: { viewModel.updateDialogState($0) },
                openFaceGallery: { viewModel.updateSelectedImage(.face) },
                openBodyGallery: { viewModel.updateSelectedImage(.body) },
                insertPlayer: {
                    viewModel.insertPlayer(navState) { message in toast.show(message) }
                },
                onMissingField: { toast.show($0) }
            )
        }
        .galleryPicker(isPresented: $viewModel.showGallery) { url in
            viewModel.onImageChanged(url.absoluteString)
        }
    }
}

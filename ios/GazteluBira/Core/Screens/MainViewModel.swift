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

/// App-scoped boot sequence: decides where the splash leads and warms up the session and the
/// local data. It owns no UI state; screens that need the user read it from `SessionManager`.
@MainActor
final class MainViewModel: ObservableObject {

    private let sessionManager: SessionManager
    private let splashController: SplashController
    private let initAppHandler: InitAppHandler

    private var boot: Task<Void, Never>?

    init(
        sessionManager: SessionManager,
        splashController: SplashController,
        initAppHandler: InitAppHandler
    ) {
        self.sessionManager = sessionManager
        self.splashController = splashController
        self.initAppHandler = initAppHandler
        start()
    }

    /// Runs on launch and again after every login or sign-up, always with the splash on screen.
    /// A boot still running is cancelled first, so two syncs never overlap.
    func start() {
        boot?.cancel()
        splashController.reset()

        guard sessionManager.isAuthenticated() else {
            splashController.skip(.welcome)
            return
        }

        boot = Task { [sessionManager, initAppHandler] in
            /// The session loads alongside the data, not before it.
            async let session: Void = sessionManager.load()
            if await initAppHandler.hasLocalData() {
                await regularInit()
            } else {
                await firstTimeInit()
            }
            await session
        }
    }

    /// Nothing stored to open on, so the splash waits for the whole download.
    private func firstTimeInit() async {
        let result = await initAppHandler.firstTimeInit()
        /// A cancelled boot has been replaced by another one: the splash is no longer its to move.
        guard !Task.isCancelled else { return }
        switch result {
        case .success:
            splashController.complete(.home)
        case .failure(let error):
            Log.e("First time init failed", error)
            splashController.complete(.welcome)
        }
    }

    /// The app opens on what is stored, and the refresh lands behind it.
    private func regularInit() async {
        guard !Task.isCancelled else { return }
        splashController.skip(.home)
        await initAppHandler.initApp()
    }
}

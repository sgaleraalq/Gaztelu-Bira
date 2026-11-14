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

/// How far the app boot has got, as the splash sees it.
///
/// One value instead of a pair of booleans plus a loose destination: "completed and skipped at the
/// same time" stops being representable, and the destination cannot be read before the decision
/// that produced it has been made.
enum SplashStatus: Equatable {
    /// Boot work is still running.
    case loading
    /// The splash was worth showing: let the progress bar finish before leaving.
    case completed(destination: Destination)
    /// There was nothing to wait for: leave the splash straight away.
    case skipped(destination: Destination)

    /// Where the app goes next, once boot work is over.
    var destination: Destination? {
        switch self {
        case .loading: return nil
        case .completed(let destination), .skipped(let destination): return destination
        }
    }
}

/// App-scoped hand-off between the boot sequence, which drives it, and the splash screen, which
/// only observes it. Writing is done through the three calls below so no other holder of this
/// singleton can push the splash into a state the boot sequence did not ask for.
protocol SplashController {
    var status: SplashStatus { get }
    func statusStream() -> AsyncStream<SplashStatus>

    /// Boot finished with work worth showing: the bar completes, then the app moves on.
    func complete(_ destination: Destination)

    /// Boot had nothing to wait for: the splash steps aside immediately.
    func skip(_ destination: Destination)

    /// Back to square one, for when the user logs out and the app boots again.
    func reset()
}

final class SplashState: SplashController {

    private let _status = MutableStateFlow<SplashStatus>(.loading)

    var status: SplashStatus { _status.value }

    func statusStream() -> AsyncStream<SplashStatus> {
        _status.stream()
    }

    func complete(_ destination: Destination) {
        _status.value = .completed(destination: destination)
    }

    func skip(_ destination: Destination) {
        _status.value = .skipped(destination: destination)
    }

    func reset() {
        _status.value = .loading
    }
}

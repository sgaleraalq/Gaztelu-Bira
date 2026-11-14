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
import UIKit

/// iOS has no toast, so the app draws its own: the iOS counterpart of Android's `showToast`.
/// `MainScreen` owns the only instance and shows whatever message is set here on top of every
/// screen.
@MainActor
final class ToastCenter: ObservableObject {

    private static let duration: UInt64 = 2_000_000_000

    @Published private(set) var message: String?

    private var dismissal: Task<Void, Never>?

    func show(_ message: String) {
        dismissal?.cancel()
        self.message = message
        dismissal = Task { [weak self] in
            try? await Task.sleep(nanoseconds: ToastCenter.duration)
            guard !Task.isCancelled else { return }
            self?.message = nil
        }
    }
}

struct ToastView: View {

    let message: String

    var body: some View {
        Text(message)
            .font(.subheadline)
            .foregroundColor(.white)
            .multilineTextAlignment(.center)
            .padding(.horizontal, 20)
            .padding(.vertical, 12)
            .background(Color.black.opacity(0.85), in: Capsule())
            .padding(.horizontal, 24)
            .padding(.bottom, 96)
            .allowsHitTesting(false)
    }
}

/// The App Store id of the app, once it has one. Until then the update dialog has nowhere to send
/// the user.
private let appStoreId = ""

/// The app's page in the App Store: the iOS counterpart of Android's `openStoreListing`.
@MainActor
func openStoreListing() {
    guard !appStoreId.isEmpty,
          let url = URL(string: "itms-apps://apps.apple.com/app/id\(appStoreId)") else {
        Log.w("The app has no App Store id yet, so there is no listing to open")
        return
    }
    UIApplication.shared.open(url)
}

/// A `NavigationStack` stops listening to the swipe-back gesture once its navigation bar is
/// hidden, and every screen of this app hides it. Taking over as the gesture's delegate brings
/// the gesture back, which is the closest iOS has to Android's system back.
extension UINavigationController: UIGestureRecognizerDelegate {

    /// Asked on every swipe. `MainScreen` points it at the app's `NavigationState`.
    static var isSwipeBackEnabled: @MainActor () -> Bool = { true }

    override open func viewDidLoad() {
        super.viewDidLoad()
        interactivePopGestureRecognizer?.delegate = self
    }

    public func gestureRecognizerShouldBegin(_ gestureRecognizer: UIGestureRecognizer) -> Bool {
        viewControllers.count > 1 && UINavigationController.isSwipeBackEnabled()
    }
}

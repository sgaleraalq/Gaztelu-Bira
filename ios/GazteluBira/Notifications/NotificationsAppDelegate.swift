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

import UIKit
import UserNotifications

/// The part of the pushes that iOS only hands to an app delegate: the APNs token and the
/// notifications themselves. SwiftUI apps have no delegate, so the app attaches this one:
///
/// ```swift
/// @UIApplicationDelegateAdaptor(NotificationsAppDelegate.self) private var notifications
/// ```
///
/// That line is the whole wiring on this side; everything else is asked to `MatchNotifications`.
final class NotificationsAppDelegate: NSObject, UIApplicationDelegate {

    /// The notification center keeps its delegate weakly, so somebody has to hold it.
    private let tray = MatchNotificationTray()

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        UNUserNotificationCenter.current().delegate = tray
        MatchNotifications.shared.start()
        return true
    }

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        MatchNotifications.shared.didRegister(deviceToken: deviceToken)
    }

    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Error
    ) {
        MatchNotifications.shared.didFailToRegister(error: error)
    }
}

/// What happens to a push once it is on the device.
private final class MatchNotificationTray: NSObject, UNUserNotificationCenterDelegate {

    /// With the app on screen iOS shows nothing unless it is told to. In the background this is
    /// never asked: the system shows the push by itself.
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        completionHandler([.banner, .list, .sound])
    }

    /// The user tapped a notification, with the app open or closed.
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        let userInfo = response.notification.request.content.userInfo

        if let payload = MatchNotificationPayload(userInfo: userInfo) {
            Task { @MainActor in
                MatchNotifications.shared.openedMatch = payload
            }
        }

        completionHandler()
    }
}

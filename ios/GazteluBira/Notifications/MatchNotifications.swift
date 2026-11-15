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
import os
import UIKit
import UserNotifications

/// Firebase Messaging is only linked once the target has it (see client/functions/README.md).
/// Until then this folder still compiles, and the only thing missing is the topic.
#if canImport(FirebaseMessaging)
import FirebaseCore
import FirebaseMessaging
#endif

/// The pushes sent when a match is inserted: the iOS counterpart of Android's
/// `MatchNotifications`.
///
/// Nobody in the app sends them: a Cloud Function watches `seasons/{season}/matches/{match}` and
/// publishes to a topic (see client/functions). A device only has to say whether it wants to
/// listen.
///
/// This folder knows nothing about sessions or screens. Whoever wants pushes attaches
/// `NotificationsAppDelegate` to the app and calls `subscribe()` and `unsubscribe()`.
@MainActor
final class MatchNotifications: ObservableObject {

    static let shared = MatchNotifications()

    /// The match behind the notification the user tapped. Whoever navigates reads it and sets it
    /// back to `nil`, so the same tap is not followed twice.
    @Published var openedMatch: MatchNotificationPayload?

    private let defaults = UserDefaults.standard
    private let log = Logger(subsystem: "com.sgale.gaztelubira", category: "notifications")

    private init() {}

    /// Remembered between launches: the topic can only be joined once APNs has given the device
    /// its token, and that happens after `subscribe()` has returned, or on a later launch.
    private var wantsPushes: Bool {
        get { defaults.bool(forKey: Self.wantsPushesKey) }
        set { defaults.set(newValue, forKey: Self.wantsPushesKey) }
    }

    /// Starts receiving the pushes on this device. Safe to call on every launch: the system only
    /// shows its permission dialog the first time, and subscribing twice is the same as once.
    func subscribe() async {
        wantsPushes = true

        do {
            _ = try await UNUserNotificationCenter.current()
                .requestAuthorization(options: [.alert, .badge, .sound])
        } catch {
            log.warning("Could not ask for the notification permission: \(error.localizedDescription)")
        }

        /// Even after a "no": the token is what the topic needs, and the user can still allow the
        /// notifications later from the settings.
        UIApplication.shared.registerForRemoteNotifications()
        await syncTopic()
    }

    /// Stops receiving them, for instance when the user logs out.
    func unsubscribe() async {
        wantsPushes = false
        await syncTopic()
    }

    /// Called by `NotificationsAppDelegate` on every launch. APNs may change the token of a
    /// device, so one that wants pushes asks for it again each time.
    func start() {
        if wantsPushes {
            UIApplication.shared.registerForRemoteNotifications()
        }
    }

    /// Called by `NotificationsAppDelegate` when APNs answers.
    func didRegister(deviceToken: Data) {
        #if canImport(FirebaseMessaging)
        guard FirebaseApp.app() != nil else { return }

        Messaging.messaging().apnsToken = deviceToken
        Task { await syncTopic() }
        #endif
    }

    func didFailToRegister(error: Error) {
        log.warning("APNs did not give a token, there will be no pushes: \(error.localizedDescription)")
    }

    /// Leaves the topic matching `wantsPushes`. Without network it fails and stays as it was: the
    /// next launch tries again.
    private func syncTopic() async {
        #if canImport(FirebaseMessaging)
        guard FirebaseApp.app() != nil else { return }

        let messaging = Messaging.messaging()
        let topic = MatchNotificationContract.topic

        do {
            if wantsPushes {
                /// Firebase refuses to subscribe without the APNs token. `didRegister` comes back
                /// here as soon as it arrives.
                guard messaging.apnsToken != nil else { return }
                try await messaging.subscribe(toTopic: topic)
            } else {
                try await messaging.unsubscribe(fromTopic: topic)
            }
        } catch {
            log.warning("Could not update the subscription to the match pushes: \(error.localizedDescription)")
        }
        #endif
    }

    private static let wantsPushesKey = "notifications.matches.wantsPushes"
}

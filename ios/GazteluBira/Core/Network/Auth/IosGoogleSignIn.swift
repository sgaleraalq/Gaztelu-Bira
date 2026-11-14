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
import FirebaseAuth
import FirebaseCore
import GoogleSignIn
import UIKit

/// iOS's answer to `GoogleAuth`: the Google Sign-In SDK asks the user for an account and Firebase
/// turns the resulting tokens into a session.
///
/// The client id is not handed in as it is on Android: it comes with `GoogleService-Info.plist`.
final class IosGoogleSignIn: GoogleAuth {

    @MainActor
    func signIn() async -> User? {
        guard let clientId = FirebaseApp.app()?.options.clientID else {
            Log.e("Google sign in failed: GoogleService-Info.plist has no CLIENT_ID")
            return nil
        }
        /// The SDK raises an Objective-C exception, which Swift cannot catch, when the app does
        /// not declare the scheme Google comes back through.
        guard IosGoogleSignIn.declaresUrlScheme(for: clientId) else {
            Log.e("Google sign in failed: the reversed client id is missing from the URL schemes")
            return nil
        }
        guard let presenter = IosGoogleSignIn.topViewController() else {
            Log.e("Google sign in failed: there is no screen to present it from")
            return nil
        }

        do {
            GIDSignIn.sharedInstance.configuration = GIDConfiguration(clientID: clientId)
            let google = try await GIDSignIn.sharedInstance.signIn(withPresenting: presenter).user
            guard let idToken = google.idToken?.tokenString else {
                Log.e("Google sign in failed: no id token came back")
                return nil
            }
            let credential = GoogleAuthProvider.credential(
                withIDToken: idToken,
                accessToken: google.accessToken.tokenString
            )
            let user = try await Auth.auth().signIn(with: credential).user
            return User(
                uid: UserId(user.uid),
                name: user.displayName,
                email: user.email,
                img: google.profile?.imageURL(withDimension: 256)?.absoluteString,
                role: .guest
            )
        } catch {
            Log.e("Google sign in failed", error)
            return nil
        }
    }

    /// "123-abc.apps.googleusercontent.com" -> "com.googleusercontent.apps.123-abc"
    private static func declaresUrlScheme(for clientId: String) -> Bool {
        let reversed = clientId.split(separator: ".").reversed().joined(separator: ".")
        let urlTypes = Bundle.main.object(forInfoDictionaryKey: "CFBundleURLTypes") as? [[String: Any]] ?? []
        return urlTypes
            .flatMap { $0["CFBundleURLSchemes"] as? [String] ?? [] }
            .contains { $0.caseInsensitiveCompare(reversed) == .orderedSame }
    }

    @MainActor
    private static func topViewController() -> UIViewController? {
        let root = UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap { $0.windows }
            .first { $0.isKeyWindow }?
            .rootViewController
        var top = root
        while let presented = top?.presentedViewController {
            top = presented
        }
        return top
    }
}

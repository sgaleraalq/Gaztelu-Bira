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
import FirebaseRemoteConfig

final class RemoteConfigManager {

    static let minimumFetchIntervalSeconds: TimeInterval = 3600
    private static let minVersionKey = "min_version"

    /// The iOS counterpart of Android's `remote_config_defaults.xml`.
    private static let defaults: [String: NSObject] = [
        minVersionKey: "1.0.0" as NSString
    ]

    private let remoteConfig: RemoteConfig

    init(remoteConfig: RemoteConfig) {
        self.remoteConfig = remoteConfig
        let settings = RemoteConfigSettings()
        settings.minimumFetchInterval = RemoteConfigManager.minimumFetchIntervalSeconds
        remoteConfig.configSettings = settings
        remoteConfig.setDefaults(RemoteConfigManager.defaults)
    }

    /// Asks the server at most once every `minimumFetchIntervalSeconds`; in between, the values
    /// already fetched are the ones activated. Fetching on every launch would end up throttled.
    ///
    /// - Returns: false when nothing could be fetched or activated.
    func fetchAndActivate() async -> Bool {
        do {
            _ = try await remoteConfig.fetchAndActivate()
            return true
        } catch {
            Log.w("Fetch failed or throttled", error)
            return false
        }
    }

    func getMinAppVersion() -> String {
        let value: String? = remoteConfig.configValue(forKey: RemoteConfigManager.minVersionKey).stringValue
        return value ?? ""
    }
}

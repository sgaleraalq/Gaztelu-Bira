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

final class RemoteConfigAppVersion: AppVersionRemote {

    private let remoteConfigManager: RemoteConfigManager

    init(remoteConfigManager: RemoteConfigManager) {
        self.remoteConfigManager = remoteConfigManager
    }

    func isSupported() async -> Bool {
        guard await remoteConfigManager.fetchAndActivate() else { return true }
        return isVersionAtLeast(
            current: getLocalVersion(),
            minimum: remoteConfigManager.getMinAppVersion()
        )
    }

    private func getLocalVersion() -> String {
        Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String ?? ""
    }
}

/// Compares "major.minor.patch" versions part by part from the left, and the first part that
/// differs decides: 1.5.0 is above 1.4.9 even though its patch is lower. A missing part counts as
/// 0, and a suffix after the number ("0-debug") is ignored.
func isVersionAtLeast(current: String, minimum: String) -> Bool {
    let currentParts = versionParts(current)
    let minimumParts = versionParts(minimum)
    for index in 0..<max(currentParts.count, minimumParts.count) {
        let part = index < currentParts.count ? currentParts[index] : 0
        let minimumPart = index < minimumParts.count ? minimumParts[index] : 0
        if part != minimumPart { return part > minimumPart }
    }
    return true
}

private func versionParts(_ version: String) -> [Int] {
    version
        .split(separator: ".", omittingEmptySubsequences: false)
        .map { part in Int(part.prefix { $0.isASCII && $0.isNumber }) ?? 0 }
}

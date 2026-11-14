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

private let preferencesName = "gaztelu_bira_preferences"
private let keyLastSync = "gbmultiplatform.lastSync"
private let keySelectedSeason = "gbmultiplatform.selectedSeason"

/// The iOS counterpart of Android's `PreferencesImpl`, on `UserDefaults` instead of
/// `SharedPreferences`. Nothing else writes these keys, so the values are mirrored in memory and
/// that mirror is what the streams follow.
final class PreferencesImpl: Preferences {

    private let settings: UserDefaults
    private let selected: MutableStateFlow<SeasonId?>
    private let synced: MutableStateFlow<Int?>

    init() {
        let settings = UserDefaults(suiteName: preferencesName) ?? .standard
        self.settings = settings
        selected = MutableStateFlow(settings.string(forKey: keySelectedSeason).map { SeasonId($0) })
        synced = MutableStateFlow(
            settings.object(forKey: keyLastSync) == nil ? nil : settings.integer(forKey: keyLastSync)
        )
    }

    func selectedSeason() -> AsyncStream<SeasonId?> {
        selected.stream().distinctUntilChanged()
    }

    func lastSync() -> AsyncStream<Int?> {
        synced.stream().distinctUntilChanged()
    }

    func resolveSelectedSeason() async -> SeasonId? {
        selected.value
    }

    func selectSeason(_ season: SeasonId?) async {
        if let season {
            settings.set(season.value, forKey: keySelectedSeason)
        } else {
            settings.removeObject(forKey: keySelectedSeason)
        }
        selected.value = season
    }

    func setLastSync(_ time: Int) async {
        settings.set(time, forKey: keyLastSync)
        synced.value = time
    }
}

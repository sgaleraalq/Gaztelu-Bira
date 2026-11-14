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

/// The season on screen: the one the user picked, or the current one while they have not picked
/// any (or picked one that is no longer stored).
///
/// It emits again after every sync, even when the season stays the same, so whoever loads that
/// season's data from here loads it again once fresh data has landed.
final class SelectedSeason {

    private let seasons: SeasonLocal
    private let preferences: Preferences

    init(seasons: SeasonLocal, preferences: Preferences) {
        self.seasons = seasons
        self.preferences = preferences
    }

    func callAsFunction() -> AsyncStream<SeasonId?> {
        AsyncStream { continuation in
            let task = Task { [self] in
                await withTaskGroup(of: Void.self) { group in
                    group.addTask {
                        for await _ in self.preferences.selectedSeason() {
                            continuation.yield(await self.first())
                        }
                    }
                    group.addTask {
                        /// Its first value is the one already stored: the other stream has just
                        /// answered for it, so only the syncs from here on count.
                        var isStoredValue = true
                        for await _ in self.preferences.lastSync() {
                            if isStoredValue {
                                isStoredValue = false
                                continue
                            }
                            continuation.yield(await self.first())
                        }
                    }
                }
                continuation.finish()
            }
            continuation.onTermination = { _ in task.cancel() }
        }
    }

    /// What the stream would emit right now. The iOS counterpart of `selectedSeason().first()`.
    func first() async -> SeasonId? {
        let chosen = await preferences.resolveSelectedSeason()
        let stored = (try? await seasons.getSeasons()) ?? []
        return stored.first { $0.id == chosen }?.id
            ?? stored.first { $0.isCurrent }?.id
    }
}

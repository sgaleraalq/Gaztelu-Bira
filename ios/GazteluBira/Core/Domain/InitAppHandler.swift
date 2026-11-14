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

protocol InitAppHandler {

    func updateAvailable() async -> Bool

    /// Whether the cache has anything to show yet. It is asked rather than remembered from the
    /// first launch, because a destructive migration empties the database without telling anyone.
    func hasLocalData() async -> Bool

    /// Downloads every season, with the app waiting for it.
    func firstTimeInit() async -> Result<Bool, Error>

    /// Refreshes what can still change while the app shows what it already has. A failure leaves
    /// the cache as it was.
    func initApp() async
}

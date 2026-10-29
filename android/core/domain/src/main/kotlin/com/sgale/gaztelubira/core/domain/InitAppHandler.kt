/*
 * Designed and developed by 2026 sgale (Sergio Galera)
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

package com.sgale.gaztelubira.core.domain

interface InitAppHandler {
    suspend fun updateAvailable(): Boolean

    /**
     * Whether the cache has anything to show yet. It is asked rather than remembered from the first
     * launch, because a destructive migration empties the database without telling anyone.
     */
    suspend fun hasLocalData(): Boolean

    /**
     * Downloads every season, with the app waiting for it.
     */
    suspend fun firstTimeInit(): Result<Boolean>

    /**
     * Refreshes what can still change while the app shows what it already has. A failure leaves the
     * cache as it was.
     */
    suspend fun initApp()
}

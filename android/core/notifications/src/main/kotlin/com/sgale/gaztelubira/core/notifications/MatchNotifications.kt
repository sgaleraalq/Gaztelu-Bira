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

package com.sgale.gaztelubira.core.notifications

/**
 * The pushes sent when a match is inserted.
 *
 * Nobody in the app sends them: a Cloud Function watches `seasons/{season}/matches/{match}` and
 * publishes to a topic (see client/functions). A device only has to say whether it wants to listen.
 */
interface MatchNotifications {

    /**
     * Starts receiving the pushes on this device. Safe to call on every launch: subscribing twice
     * is the same as subscribing once.
     */
    suspend fun subscribe()

    /**
     * Stops receiving them, for instance when the user logs out.
     */
    suspend fun unsubscribe()
}

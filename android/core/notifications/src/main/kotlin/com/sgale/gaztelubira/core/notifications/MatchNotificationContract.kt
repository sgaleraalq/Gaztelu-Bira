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
 * What the Cloud Function and the apps agree on. The same names live in
 * `client/functions/match-notification.js` and in `ios/GazteluBira/Notifications`: a change here
 * has to be made in the three places.
 */
internal object MatchNotificationContract {
    const val TOPIC = "matches"

    /**
     * Also written by hand in this module's manifest, as the default channel of the system tray.
     */
    const val CHANNEL_ID = "matches"

    const val KEY_TYPE = "type"
    const val KEY_SEASON_ID = "seasonId"
    const val KEY_MATCH_ID = "matchId"

    const val TYPE_MATCH_INSERTED = "match_inserted"
}

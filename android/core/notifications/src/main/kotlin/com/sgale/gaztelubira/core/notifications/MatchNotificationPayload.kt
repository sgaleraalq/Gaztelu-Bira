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

import android.content.Intent
import com.sgale.gaztelubira.core.notifications.MatchNotificationContract.KEY_MATCH_ID
import com.sgale.gaztelubira.core.notifications.MatchNotificationContract.KEY_SEASON_ID
import com.sgale.gaztelubira.core.notifications.MatchNotificationContract.KEY_TYPE
import com.sgale.gaztelubira.core.notifications.MatchNotificationContract.TYPE_MATCH_INSERTED

/**
 * The match a push talks about. The ids are plain strings so this module does not need the domain.
 */
data class MatchNotificationPayload(
    val seasonId: String,
    val matchId: String
) {
    companion object {

        /**
         * @return null when [data] is not the push of an inserted match.
         */
        fun fromData(data: Map<String, String>): MatchNotificationPayload? {
            if (data[KEY_TYPE] != TYPE_MATCH_INSERTED) return null

            val seasonId = data[KEY_SEASON_ID].orEmpty()
            val matchId = data[KEY_MATCH_ID].orEmpty()
            if (seasonId.isEmpty() || matchId.isEmpty()) return null

            return MatchNotificationPayload(seasonId, matchId)
        }

        /**
         * The match behind the notification that opened the app, if that is how it was opened.
         * Works for both ways a notification is born: the system tray puts the data of the push in
         * the extras of the launch intent, and the ones this module shows do the same.
         */
        fun fromIntent(intent: Intent?): MatchNotificationPayload? {
            val extras = intent?.extras ?: return null

            return fromData(
                listOf(KEY_TYPE, KEY_SEASON_ID, KEY_MATCH_ID)
                    .mapNotNull { key -> extras.getString(key)?.let { value -> key to value } }
                    .toMap()
            )
        }
    }
}

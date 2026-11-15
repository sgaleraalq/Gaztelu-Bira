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

package com.sgale.gaztelubira.core.notifications.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sgale.gaztelubira.core.notifications.MatchNotificationContract.CHANNEL_ID
import com.sgale.gaztelubira.core.notifications.MatchNotificationContract.KEY_MATCH_ID
import com.sgale.gaztelubira.core.notifications.MatchNotificationContract.KEY_SEASON_ID
import com.sgale.gaztelubira.core.notifications.MatchNotificationContract.KEY_TYPE
import com.sgale.gaztelubira.core.notifications.MatchNotificationContract.TYPE_MATCH_INSERTED
import com.sgale.gaztelubira.core.notifications.MatchNotificationPayload
import com.sgale.gaztelubira.core.notifications.R

internal object MatchNotificationPresenter {

    /**
     * The system tray posts its notifications with the tag of the push and this id, so using the
     * same pair makes a push that arrives twice replace itself instead of piling up.
     */
    private const val NOTIFICATION_ID = 0

    /**
     * Creating a channel that already exists changes nothing, so this can be called every time.
     */
    fun ensureChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notifications_matches_channel_name),
            IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.notifications_matches_channel_description)
        }

        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    fun show(
        context: Context,
        title: String,
        body: String,
        payload: MatchNotificationPayload
    ) {
        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) return

        ensureChannel(context)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_match)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setContentIntent(openApp(context, payload))
            .build()

        try {
            manager.notify(payload.matchId, NOTIFICATION_ID, notification)
        } catch (_: SecurityException) {
            // The permission was revoked between the check and the post: nothing to show.
        }
    }

    /**
     * Opens the app through its launcher activity, whichever it is, carrying the same extras the
     * system tray would put: whoever handles the launch reads them with
     * [MatchNotificationPayload.fromIntent] and does not care who built the notification.
     */
    private fun openApp(context: Context, payload: MatchNotificationPayload): PendingIntent? {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?: return null

        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra(KEY_TYPE, TYPE_MATCH_INSERTED)
        intent.putExtra(KEY_SEASON_ID, payload.seasonId)
        intent.putExtra(KEY_MATCH_ID, payload.matchId)

        return PendingIntent.getActivity(
            context,
            payload.matchId.hashCode(),
            intent,
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        )
    }
}

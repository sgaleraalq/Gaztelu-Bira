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

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sgale.gaztelubira.core.notifications.MatchNotificationPayload

/**
 * Only reached with the app on screen. In the background a push that carries a `notification`
 * block never gets here: the system tray shows it by itself, with the channel and the icon declared
 * in this module's manifest.
 */
internal class MatchNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val notification = message.notification ?: return
        val payload = MatchNotificationPayload.fromData(message.data) ?: return

        MatchNotificationPresenter.show(
            context = this,
            title = notification.title.orEmpty(),
            body = notification.body.orEmpty(),
            payload = payload
        )
    }
}

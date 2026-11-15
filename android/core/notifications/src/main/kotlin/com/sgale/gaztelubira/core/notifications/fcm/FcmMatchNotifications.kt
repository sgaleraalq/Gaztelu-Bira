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

import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import com.sgale.gaztelubira.core.notifications.MatchNotificationContract.TOPIC
import com.sgale.gaztelubira.core.notifications.MatchNotifications
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FcmMatchNotifications @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val messaging: FirebaseMessaging
) : MatchNotifications {

    /**
     * The channel goes first: with the app closed it is the system that shows the push, and it
     * needs the channel to exist already.
     */
    override suspend fun subscribe() {
        MatchNotificationPresenter.ensureChannel(context)
        messaging.subscribeToTopic(TOPIC).await()
    }

    override suspend fun unsubscribe() {
        messaging.unsubscribeFromTopic(TOPIC).await()
    }
}

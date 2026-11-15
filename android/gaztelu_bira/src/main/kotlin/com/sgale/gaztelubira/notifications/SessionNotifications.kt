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

package com.sgale.gaztelubira.notifications

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.sgale.gaztelubira.core.common.utils.TAG
import com.sgale.gaztelubira.core.domain.auth.SessionManager
import com.sgale.gaztelubira.core.notifications.MatchNotifications
import com.sgale.gaztelubira.core.notifications.permission.NotificationPermissionRequester
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The only place where the pushes meet the rest of the app: `:core:notifications` knows nothing
 * about users, and the session knows nothing about pushes.
 *
 * The rule is that the pushes follow the session. Whoever is signed in gets them, and logging out
 * stops them, so a phone that changes hands does not keep buzzing.
 */
class SessionNotifications @Inject constructor(
    private val sessionManager: SessionManager,
    private val notifications: MatchNotifications
) {

    /**
     * To be called from `onCreate`, which is when the activity still accepts the permission
     * launcher.
     *
     * The session starts empty on every launch until the profile is loaded, and that first
     * emptiness is not a logout: it is skipped, or every launch would unsubscribe and subscribe
     * again.
     */
    fun follow(activity: ComponentActivity) {
        val permission = NotificationPermissionRequester(activity)

        activity.lifecycleScope.launch {
            sessionManager.session
                .map { session -> session != null }
                .distinctUntilChanged()
                .dropWhile { signedIn -> !signedIn }
                .collect { signedIn ->
                    if (signedIn) {
                        permission.request()
                        attempt("subscribe to") { notifications.subscribe() }
                    } else {
                        attempt("unsubscribe from") { notifications.unsubscribe() }
                    }
                }
        }
    }

    /**
     * Without network the subscription fails. It is not retried here: the next launch does it.
     */
    private suspend fun attempt(what: String, action: suspend () -> Unit) {
        try {
            action()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.w(TAG, "Could not $what the match notifications", e)
        }
    }
}

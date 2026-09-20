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

package com.sgale.gaztelubira.core.network.auth

import android.app.Activity
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Hands the running Activity to the flows that need one as a host, such as the
 * Google account picker.
 *
 * The reference is weak and never outlives its Activity: an Activity that dies
 * without unregistering (a crash, a process the system kills) leaves nothing behind.
 */
@Singleton
class ActivityBridge @Inject constructor() {
    private var registered: WeakReference<Activity>? = null

    fun register(activity: Activity) {
        registered = WeakReference(activity)
    }

    /**
     * Only the Activity that registered can unregister: while one screen is being
     * recreated the new one may register before the old one is destroyed.
     */
    fun unregister(activity: Activity) {
        if (registered?.get() === activity) registered = null
    }

    /** @return the Activity that can host a flow right now, if there is one. */
    fun current(): Activity? =
        registered?.get()?.takeUnless { it.isFinishing || it.isDestroyed }
}

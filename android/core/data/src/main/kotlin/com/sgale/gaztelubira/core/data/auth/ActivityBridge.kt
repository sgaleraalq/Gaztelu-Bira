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

package com.sgale.gaztelubira.core.data.auth

import android.app.Activity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Hands the current [Activity] to singletons that need one to show UI — Credential Manager puts
 * the Google account chooser on top of the activity that is in front.
 *
 * The activity registers itself on create and clears itself on destroy, so nothing here outlives
 * the activity it points at. Same shape as [com.sgale.gaztelubira.core.domain.utils.PermissionBridge].
 */
@Singleton
class ActivityBridge @Inject constructor() {
    private var activity: Activity? = null

    fun setActivity(activity: Activity) {
        this.activity = activity
    }

    fun clear() {
        activity = null
    }

    fun requireActivity(): Activity =
        activity ?: error("No activity registered in ActivityBridge")
}

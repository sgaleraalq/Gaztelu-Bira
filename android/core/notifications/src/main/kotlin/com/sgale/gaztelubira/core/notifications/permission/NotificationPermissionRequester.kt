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

package com.sgale.gaztelubira.core.notifications.permission

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission

/**
 * Asks for the permission to post notifications, which only exists since Android 13: before that
 * they are allowed from the start.
 *
 * It has to be created while the activity is being created (in `onCreate` or as a property): that
 * is the only moment an activity accepts new result launchers.
 */
class NotificationPermissionRequester(
    private val activity: ComponentActivity
) {
    private val launcher = activity.registerForActivityResult(RequestPermission()) {
        // Nothing to do with the answer: without the permission the pushes simply are not shown.
    }

    /**
     * Shows the system dialog once. After a first "no" the system reports that it wants an
     * explanation before asking again, and that is taken as the answer: the user who changes their
     * mind has the settings of the app.
     */
    fun request() {
        if (SDK_INT < TIRAMISU) return
        if (activity.checkSelfPermission(POST_NOTIFICATIONS) == PERMISSION_GRANTED) return
        if (activity.shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)) return

        launcher.launch(POST_NOTIFICATIONS)
    }
}

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

package com.sgale.gaztelubira.core.common.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import com.sgale.gaztelubira.core.domain.utils.IToastManager
import com.sgale.gaztelubira.core.domain.utils.IToastManager.ToastDurationType
import com.sgale.gaztelubira.core.domain.utils.IToastManager.ToastDurationType.LONG
import com.sgale.gaztelubira.core.domain.utils.IToastManager.ToastDurationType.SHORT
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val SHORT_TOAST_DELAY = 2000L
private const val LONG_TOAST_DELAY = 3500L

@Singleton
class ToastManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) : IToastManager {
    private var currentToast: Toast? = null

    override fun showToast(
        msg: String,
        onFinish: () -> Unit,
        duration: ToastDurationType
    ) {
        val toastDuration = when (duration) {
            SHORT -> LENGTH_SHORT
            LONG -> LENGTH_LONG
        }

        val showAction = {
            currentToast?.cancel()

            currentToast = makeText(context, msg, toastDuration)
            currentToast?.show()

            val delay = if (toastDuration == LENGTH_SHORT) SHORT_TOAST_DELAY else LONG_TOAST_DELAY
            Handler(Looper.getMainLooper()).postDelayed({ onFinish() }, delay)
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            showAction()
        } else {
            Handler(Looper.getMainLooper()).post { showAction() }
        }
    }
}

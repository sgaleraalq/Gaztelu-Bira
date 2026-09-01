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

package com.sgale.gaztelubira.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sgale.gaztelubira.core.data.auth.ActivityBridge
import com.sgale.gaztelubira.core.designsystem.style.GBTheme
import com.sgale.gaztelubira.core.screens.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GazteluBiraActivity : ComponentActivity() {

    @Inject
    lateinit var activityBridge: ActivityBridge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBridge.setActivity(this)

        setContent {
            GBTheme {
                MainScreen()
            }
        }
    }

    override fun onDestroy() {
        activityBridge.clear()
        super.onDestroy()
    }
}

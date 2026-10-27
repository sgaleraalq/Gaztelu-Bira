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

package com.sgale.gaztelubira.core.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.common.utils.TAG
import com.sgale.gaztelubira.core.domain.InitAppHandler
import com.sgale.gaztelubira.core.domain.auth.SessionManager
import com.sgale.gaztelubira.core.domain.repository.Preferences
import com.sgale.gaztelubira.core.navigation.Destination.Home
import com.sgale.gaztelubira.core.navigation.Destination.Welcome
import com.sgale.gaztelubira.core.screens.splash.SplashController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Activity-scoped boot sequence: decides where the splash leads and warms up the session and the
 * local data. It owns no UI state; screens that need the user read it from [SessionManager].
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val splashController: SplashController,
    private val preferences: Preferences,
    private val initAppHandler: InitAppHandler,
) : ViewModel() {

    init {
        start()
    }

    /**
     * Runs on launch (once, not on every recreation of the activity) and again after every login or sign-up, always with the splash on screen.
     */
    fun start() {
        splashController.reset()

        if (!sessionManager.isAuthenticated()) {
            splashController.skip(Welcome)
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) { sessionManager.load() }
        }

        viewModelScope.launch {
            if (preferences.isFirstTime) firstTimeInit() else regularInit()
        }
    }

    private suspend fun firstTimeInit() {
        initAppHandler.firstTimeInit()
            .onSuccess { splashController.complete(Home) }
            .onFailure {
                Log.e(TAG, "First time init failed: ${it.message}")
                preferences.setFirstTime(true)
                splashController.complete(Welcome)
            }
    }

    private suspend fun regularInit() {
        splashController.skip(Home)
        withContext(Dispatchers.IO) { initAppHandler.initApp() }
    }
}

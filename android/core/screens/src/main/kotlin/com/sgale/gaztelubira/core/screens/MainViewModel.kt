/*
 * Designed and developed by 2025 sgaleraalq (Sergio Galera)
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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.auth.UserSession
import com.sgale.gaztelubira.core.domain.auth.usecase.IsUserAuthenticated
import com.sgale.gaztelubira.core.domain.repository.InitAppHandler
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.usecase.users.GetUser
import com.sgale.gaztelubira.core.domain.utils.IToastManager
import com.sgale.gaztelubira.core.screens.navigation.Destination.Home
import com.sgale.gaztelubira.core.screens.navigation.Destination.Welcome
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.home.HomeTab
import com.sgale.gaztelubira.core.screens.home.HomeTab.Stats
import com.sgale.gaztelubira.core.screens.splash.SplashContractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserSession: GetUser,
    private val isUserAuthenticated: IsUserAuthenticated,
    private val splashContractor: SplashContractor,
    private val preferences: IGBPreferences,
    private val initAppHandler: InitAppHandler,
    private val toastManager: IToastManager
) : ViewModel() {
    private var defaultHomeTab = Stats

    private val _userSession = MutableStateFlow<UserSession?>(null)
    val userSession: StateFlow<UserSession?> = _userSession

    private val _updateAvailable = MutableStateFlow(false)
    val updateAvailable = _updateAvailable

    fun getDefaultTab() = defaultHomeTab

    /**
     * Init Navigation
     */
    fun init(navState: NavigationState) {
        val userId = isUserAuthenticated()
        println("User authenticated with uid: $userId")

        if (userId != null) {
            joinApp(userId)
        } else {
            initWelcomeScreen(navState)
        }
    }

    fun reset() {
        _userSession.value = null
    }

    fun updateHomeTab(tab: HomeTab) {
        defaultHomeTab = tab
    }

    private fun initWelcomeScreen(navState: NavigationState) {
        navState.navigateTo(Welcome, true)
    }

    private fun joinApp(userId: String) {
        resolveUser(userId)
        initApp()
    }

    private fun initApp() {
        viewModelScope.launch {
            println("User session: $userSession")
            val isFirstTime = preferences.isFirstTime()

            if (isFirstTime) {
                val result = initAppHandler.firstTimeInit()

                result.onSuccess {
                    splashContractor.contractCompleted(Home)
                }.onFailure {
                    println("GBError: $it")
                    manageInitAppError()
                }
            } else {
                handleInit()
            }
        }
    }

    private suspend fun checkForUpdates() {
        val update = withContext(Dispatchers.IO) {
            initAppHandler.updateAvailable()
        }
        _updateAvailable.value = update
    }

    private suspend fun fetchInformation() {
        withContext(Dispatchers.IO) {
            initAppHandler.initApp()
        }
    }

    private suspend fun handleInit() {
        splashContractor.avoidSplash(Home)
        checkForUpdates()
        fetchInformation()
    }

    private fun manageInitAppError() {
        toastManager.showToast("There was a problem joining the app, try again.")
        splashContractor.contractCompleted(Welcome)
    }

    private fun resolveUser(userId: String) {
        viewModelScope.launch {
            _userSession.value = withContext(Dispatchers.IO) {
                getUserSession(userId)
            }
        }
    }
}

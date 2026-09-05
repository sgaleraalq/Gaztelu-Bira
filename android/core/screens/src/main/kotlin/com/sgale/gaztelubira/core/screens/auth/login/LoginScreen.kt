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

package com.sgale.gaztelubira.core.screens.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.screens.LocalMainViewModel
import com.sgale.gaztelubira.core.screens.auth.login.LoginEvent.LoggedIn
import com.sgale.gaztelubira.core.screens.navigation.Destination.Companion.toDestination
import com.sgale.gaztelubira.core.screens.navigation.Destination.Splash
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginActions
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginView

@Composable
internal fun LoginScreen(
    navState: NavigationState,
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>()
) {
    val mainViewModel = LocalMainViewModel.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, navState, mainViewModel) {
        viewModel.events.collect { event ->
            when (event) {
                LoggedIn -> {
                    mainViewModel.init(navState)
                    navState.navigateTo(Splash)
                }
            }
        }
    }

    LoginView(
        state = state,
        actions = remember(viewModel, navState) {
            LoginActions(
                onEmailChanged = viewModel::onEmailChanged,
                onPasswordChanged = viewModel::onPasswordChanged,
                onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
                onLogin = viewModel::onLogin,
                navigateTo = { destination -> navState.navigateTo(destination.toDestination()) }
            )
        }
    )
}

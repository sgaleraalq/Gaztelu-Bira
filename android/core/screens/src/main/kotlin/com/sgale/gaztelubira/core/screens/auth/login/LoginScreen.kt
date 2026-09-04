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

package com.sgale.gaztelubira.core.screens.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.sgale.gaztelubira.core.screens.LocalMainViewModel
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginActions
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginView

@Composable
internal fun LoginScreen(
    navState: NavigationState,
    viewModel: LoginScreenViewModel = hiltViewModel<LoginScreenViewModel>()
) {
    val mainViewModel = LocalMainViewModel.current
    val state by viewModel.state.collectAsState()

    val actions = remember(
        viewModel,
        navState,
        mainViewModel
    ) {
        LoginActions(
            onEmailChanged = viewModel::onEmailChanged,
            onPasswordChanged = viewModel::onPasswordChanged,
            onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
            onLogin = viewModel::onLogin,
            navigateTo = TODO()
        )
    }
    
    LoginView(state, actions)
}

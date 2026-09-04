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

package com.sgale.gaztelubira.core.screens.auth.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.screens.LocalMainViewModel
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpActions
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpView

@Composable
internal fun SignUpScreen(
    navState: NavigationState,
    viewModel: SignUpViewModel = hiltViewModel<SignUpViewModel>()
) {
    val mainViewModel = LocalMainViewModel.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    val actions = remember(
        viewModel,
        navState,
        mainViewModel
    ) {
        SignUpActions(
            onNameChange = viewModel::onNameChange,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
            onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
            onToggleRepeatPasswordVisibility = viewModel::onToggleRepeatPasswordVisibility,
            onSignUp = { viewModel.signUp(navState, mainViewModel) }
        )
    }

    SignUpView(state, actions)
}

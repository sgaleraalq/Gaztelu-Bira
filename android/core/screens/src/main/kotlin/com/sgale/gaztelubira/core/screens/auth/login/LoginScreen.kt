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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.sgale.gaztelubira.core.designsystem.components.GBProgressDialog
import com.sgale.gaztelubira.core.designsystem.components.GBScaffold
import com.sgale.gaztelubira.core.designsystem.style.primaryRed
import com.sgale.gaztelubira.core.screens.LocalMainViewModel
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.auth.AuthState.GoogleLoading
import com.sgale.gaztelubira.core.screens.auth.AuthState.Loading
import androidx.compose.ui.res.stringResource

@Composable
fun LoginScreen(
    state: NavigationState,
    viewModel: LoginScreenViewModel = hiltViewModel<LoginScreenViewModel>()
) {
    val mainViewModel = LocalMainViewModel.current
    val focusManager = LocalFocusManager.current

    val loginUser by viewModel.loginUser.collectAsState()
    val loginState by viewModel.state.collectAsState()

    val invalidEmail = stringResource(R.string.error_invalid_email)

    if (loginState is Loading) {
        focusManager.clearFocus()
    }

    GBScaffold { modifier ->
        LoginScreenUI(
            modifier = modifier,
            user = loginUser,
            loginState = loginState,
            changeEmail = { viewModel.changeEmail(it) },
            changePassword = { viewModel.changePassword(it) },
            changePasswordVisibility = { viewModel.changePasswordVisibility() },
            login = { viewModel.signInWithEmail(state, invalidEmail, mainViewModel) },
            loginGoogle = { viewModel.signInWithGoogle(state, mainViewModel) },
            navigateTo = { state.navigateTo(it) }
        )

        GBProgressDialog(
            modifier = Modifier.fillMaxSize(),
            show = loginState == GoogleLoading,
            color = primaryRed
        )
    }
}

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

package com.sgale.gaztelubira.multiplatform.ui.auth.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBProgressDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBScaffold
import com.sgale.gaztelubira.multiplatform.designsystem.style.primaryRed
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Loading
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Loading.Login.Google

/**
 * Shared entry point for the login screen: state in, actions out, nothing platform-specific.
 */
@Composable
fun LoginView(
    state: LoginUiState,
    actions: LoginActions
) {
    val focusManager = LocalFocusManager.current
    val requestInFlight = state.auth is Loading

    LaunchedEffect(requestInFlight) {
        if (requestInFlight) focusManager.clearFocus()
    }

    GBScaffold { modifier ->
        LoginViewUI(
            modifier = modifier,
            state = state,
            actions = actions
        )

        GBProgressDialog(
            modifier = Modifier.fillMaxSize(),
            show = state.auth == Google,
            color = primaryRed
        )
    }
}

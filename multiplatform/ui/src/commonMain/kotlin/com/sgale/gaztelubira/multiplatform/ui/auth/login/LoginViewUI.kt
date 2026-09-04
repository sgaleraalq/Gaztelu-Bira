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

package com.sgale.gaztelubira.multiplatform.ui.auth.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Loading.Login.Email
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Loading.Login.Google
import com.sgale.gaztelubira.multiplatform.ui.auth.common.AuthButton
import com.sgale.gaztelubira.multiplatform.ui.auth.common.AuthCard
import com.sgale.gaztelubira.multiplatform.ui.auth.common.AuthErrorMessage
import com.sgale.gaztelubira.multiplatform.ui.auth.common.AuthScaffold
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginActions.LoginDestination.SignUp
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginActions.LoginDestination.Splash
import com.sgale.gaztelubira.multiplatform.ui.auth.login.ui.ContinueAsGuest
import com.sgale.gaztelubira.multiplatform.ui.auth.login.ui.DontHaveAccount
import com.sgale.gaztelubira.multiplatform.ui.auth.login.ui.ForgotPassword
import com.sgale.gaztelubira.multiplatform.ui.auth.login.ui.GoogleLogin
import com.sgale.gaztelubira.multiplatform.ui.auth.login.ui.LoginTextFields
import com.sgale.gaztelubira.multiplatform.ui.auth.login.ui.OrSpacer
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.login
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LoginViewUI(
    modifier: Modifier,
    state: LoginUiState,
    actions: LoginActions
) {
    AuthScaffold(
        modifier = modifier,
        title = stringResource(Res.string.login)
    ) {
        LoginCard(state, actions)
    }
}

@Composable
private fun LoginCard(
    state: LoginUiState,
    actions: LoginActions
) {
    AuthCard {
        LoginTextFields(state.user, actions)
        AuthErrorMessage(state.errorMessage())
        Spacer(Modifier.height(16.dp))
        AuthButton(
            text = stringResource(Res.string.login),
            authState = state.auth,
            onClick = { actions.onLogin(Email) }
        )
        OrSpacer()
        GoogleLogin { actions.onLogin(Google) }
        ContinueAsGuest { actions.navigateTo(Splash) }
        Spacer(Modifier.height(16.dp))
        DontHaveAccount { actions.navigateTo(SignUp) }
        Spacer(Modifier.height(8.dp))
        ForgotPassword { /* TODO: needs a LoginDestination of its own */ }
    }
}

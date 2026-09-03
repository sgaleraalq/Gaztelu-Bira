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
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState
import com.sgale.gaztelubira.multiplatform.ui.auth.common.AuthButton
import com.sgale.gaztelubira.multiplatform.ui.auth.common.AuthCard
import com.sgale.gaztelubira.multiplatform.ui.auth.common.AuthScaffold
import com.sgale.gaztelubira.multiplatform.ui.auth.login.ui.DontHaveAccount
import com.sgale.gaztelubira.multiplatform.ui.auth.login.ui.ForgotPassword
import com.sgale.gaztelubira.multiplatform.ui.auth.login.ui.GoogleLogin
import com.sgale.gaztelubira.multiplatform.ui.auth.login.ui.LoginTextFields
import com.sgale.gaztelubira.multiplatform.ui.auth.login.ui.OrSpacer
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.login
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreenUI(
    modifier: Modifier,
    user: LoginUser,
    loginState: AuthState,
    changeEmail: (String) -> Unit,
    changePassword: (String) -> Unit,
    changePasswordVisibility: () -> Unit,
    login: () -> Unit,
    loginGoogle: () -> Unit,
//    navigateTo: (Destination) -> Unit
) {
    val loginText = stringResource(Res.string.login)
    AuthScaffold(modifier, loginText) {
        LoginCard(
            user = user,
            loginState = loginState,
            changeEmail = changeEmail,
            changePassword = changePassword,
            changePasswordVisibility = changePasswordVisibility,
            login = login,
            loginGoogle = { loginGoogle() },
//            navigateTo = { navigateTo(it) }
        )
    }
}

@Composable
fun LoginCard(
    user: LoginUser,
    loginState: AuthState,
    changeEmail: (String) -> Unit,
    changePassword: (String) -> Unit,
    changePasswordVisibility: () -> Unit,
    login: () -> Unit,
    loginGoogle: () -> Unit,
//    navigateTo: (Destination) -> Unit
) {
    val loginTxt = stringResource(Res.string.login)
    AuthCard {
        LoginTextFields(
            user = user,
            changeEmail = changeEmail,
            changePassword = changePassword,
            changePasswordVisibility = changePasswordVisibility
        )
        Spacer(Modifier.height(16.dp))
        AuthButton(loginTxt, loginState) { login() }
        OrSpacer()
        GoogleLogin { loginGoogle() }
//            ContinueAsGuest { navigateTo(Home) }
        Spacer(Modifier.height(16.dp))
        DontHaveAccount { /* navigateTo(SignUp) */ }
        Spacer(Modifier.height(8.dp))
        ForgotPassword { /* TODO */ }
    }
}

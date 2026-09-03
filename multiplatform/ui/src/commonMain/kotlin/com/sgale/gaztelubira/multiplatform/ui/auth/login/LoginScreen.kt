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

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBScaffold
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.error_invalid_email
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
//    state: NavigationState,
//    viewModel: LoginScreenViewModel = hiltViewModel<LoginScreenViewModel>()
) {
//    val mainViewModel = LocalMainViewModel.current
    val focusManager = LocalFocusManager.current

//    val loginUser by viewModel.loginUser.collectAsState()
//    val loginState by viewModel.state.collectAsState()

    val invalidEmail = stringResource(Res.string.error_invalid_email)

//    if (loginState is AuthState.Loading) {
//        focusManager.clearFocus()
//    }

    GBScaffold(
        title = "",
        content = { modifier ->
            //        LoginScreenUI(
//            modifier = modifier,
//            user = loginUser,
//            loginState = loginState,
//            changeEmail = { /* viewModel.changeEmail(it) */ },
//            changePassword = { /* viewModel.changePassword(it) */ },
//            changePasswordVisibility = { /* viewModel.changePasswordVisibility() */ },
//            login = { /* viewModel.signInWithEmail(state, invalidEmail, mainViewModel) */ },
//            loginGoogle = { /* viewModel.signInWithGoogle(state, mainViewModel) */ },
//            navigateTo = { /* state.navigateTo(it) */ }
//        )

//        GBProgressDialog(
//            modifier = Modifier.fillMaxSize(),
//            show = loginState == GoogleLoading,
//            color = primaryRed
//        )
        }
    )
}

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

package com.sgale.gaztelubira.multiplatform.ui.ios

import androidx.compose.ui.window.ComposeUIViewController
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Loading.Login.Email
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Loading.Login.Google
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginActions
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginUiState
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginView
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpActions
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpUiState
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpView
import com.sgale.gaztelubira.multiplatform.ui.splash.SplashActions
import com.sgale.gaztelubira.multiplatform.ui.splash.SplashUiState
import com.sgale.gaztelubira.multiplatform.ui.splash.SplashView
import platform.UIKit.UIViewController

/**
 * The iOS counterparts of Android's `SplashScreen`, `LoginScreen` and `SignUpScreen`: the view
 * model lives in Swift, and what reaches Compose is its state and the callbacks it answers to.
 *
 * The callbacks arrive one by one and the `XActions` are put together here, so Swift never has to
 * build a Kotlin class out of lambdas or take a sealed type as a parameter.
 *
 * `GBTheme` is applied by each controller for the same reason `GazteluBiraActivity` applies it on
 * Android: every one of them is a host of its own.
 */
fun splashViewController(
    state: IosState<SplashUiState>,
    navigate: () -> Unit
): UIViewController {
    val actions = SplashActions(navigate = navigate)
    return ComposeUIViewController {
        GBTheme {
            SplashView(state.value, actions)
        }
    }
}

fun loginViewController(
    state: IosState<LoginUiState>,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginWithEmail: () -> Unit,
    onLoginWithGoogle: () -> Unit,
    navigateTo: (IosRoute) -> Unit
): UIViewController {
    val actions = LoginActions(
        onEmailChanged = onEmailChanged,
        onPasswordChanged = onPasswordChanged,
        onTogglePasswordVisibility = onTogglePasswordVisibility,
        onLogin = { provider ->
            when (provider) {
                Email -> onLoginWithEmail()
                Google -> onLoginWithGoogle()
            }
        },
        navigateTo = navigateTo.asUiNavigation()
    )
    return ComposeUIViewController {
        GBTheme {
            LoginView(state.value, actions)
        }
    }
}

fun signUpViewController(
    state: IosState<SignUpUiState>,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleRepeatPasswordVisibility: () -> Unit,
    onSignUp: () -> Unit
): UIViewController {
    val actions = SignUpActions(
        onNameChange = onNameChange,
        onEmailChange = onEmailChange,
        onPasswordChange = onPasswordChange,
        onRepeatPasswordChange = onRepeatPasswordChange,
        onTogglePasswordVisibility = onTogglePasswordVisibility,
        onToggleRepeatPasswordVisibility = onToggleRepeatPasswordVisibility,
        onSignUp = onSignUp
    )
    return ComposeUIViewController {
        GBTheme {
            SignUpView(state.value, actions)
        }
    }
}

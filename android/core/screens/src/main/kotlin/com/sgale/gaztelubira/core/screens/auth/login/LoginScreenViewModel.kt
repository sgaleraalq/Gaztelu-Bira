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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.auth.AuthResult
import com.sgale.gaztelubira.core.domain.auth.AuthResult.Error
import com.sgale.gaztelubira.core.domain.auth.AuthResult.Success
import com.sgale.gaztelubira.core.domain.auth.IAuthRepository
import com.sgale.gaztelubira.core.screens.MainViewModel
import com.sgale.gaztelubira.core.screens.navigation.Destination.Splash
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Default
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Login
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Login.Email
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Login.Google
import com.sgale.gaztelubira.multiplatform.ui.auth.common.valid
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginUiState
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginUiState.LoginUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class LoginScreenViewModel @Inject constructor(
    private val repository: IAuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    internal val state: StateFlow<LoginUiState> = _state

    private fun updateUser(block: (LoginUser) -> LoginUser) {
        _state.update { state ->
            state.copy(user = block(state.user))
        }
    }

    private fun updateAuthState(newState: AuthState) {
        _state.update { state ->
            state.copy(auth = newState)
        }
    }

    internal fun onEmailChanged(value: String) = updateUser { it.copy(email = value) }
    internal fun onPasswordChanged(value: String) = updateUser { it.copy(password = value) }

    internal fun onTogglePasswordVisibility() = _state.update { state ->
        state.copy(user = state.user.copy(isPasswordVisible = !state.user.isPasswordVisible))
    }

    internal fun onLogin(type: Login) {
        when (type) {
            Email -> loginWithEmail()
            Google -> loginWithGoogle()
        }
    }

    private fun loginWithEmail(
        state: NavigationState,
        mainViewModel: MainViewModel
    ) {
        val email = _state.value.user.email
        val password = _state.value.user.password

        if (!email.valid()) {
//            showToast(invalidEmailMsg)
            return
        }

        viewModelScope.launch {
            updateAuthState(Email)
            val authResult = withContext(Dispatchers.IO) {
                repository.loginWithEmail(email, password)
            }

            resolveAuth(authResult, state, mainViewModel)
        }
    }

    /**
     * After Google pop up results
     */
    private fun loginWithGoogle(
        state: NavigationState,
        mainViewModel: MainViewModel
    ) {
        viewModelScope.launch {
            updateAuthState(Google)
            val authResult = withContext(Dispatchers.IO) {
                repository.loginWithGoogle()
            }
            resolveAuth(authResult, state, mainViewModel)
        }
    }

    private fun resolveAuth(
        authResult: AuthResult,
        state: NavigationState,
        mainViewModel: MainViewModel
    ) {
        if (authResult is Success) {
            mainViewModel.init(state)
            state.navigateTo(Splash)
        } else {
            authResult as Error
//            showToast(authResult.message)
            updateAuthState(Default)
        }
    }
}

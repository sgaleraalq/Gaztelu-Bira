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
import com.sgale.gaztelubira.core.domain.utils.IToastManager
import com.sgale.gaztelubira.core.domain.utils.valid
import com.sgale.gaztelubira.core.screens.MainViewModel
import com.sgale.gaztelubira.core.screens.navigation.Destination.Splash
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.auth.AuthState
import com.sgale.gaztelubira.core.screens.auth.AuthState.Default
import com.sgale.gaztelubira.core.screens.auth.AuthState.GoogleLoading
import com.sgale.gaztelubira.core.screens.auth.AuthState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val toastManager: IToastManager,
    private val repository: IAuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow<AuthState>(Default)
    val state = _state

    private val _loginUser = MutableStateFlow(LoginUser())
    val loginUser = _loginUser

    fun changeEmail(email: String) {
        _loginUser.value = _loginUser.value.copy(
            email = email
        )
    }

    fun changePassword(password: String) {
        _loginUser.value = _loginUser.value.copy(
            password = password
        )
    }

    fun changePasswordVisibility() {
        _loginUser.value = _loginUser.value.copy(
            isPasswordVisible = !_loginUser.value.isPasswordVisible
        )
    }

    fun signInWithEmail(
        state: NavigationState,
        invalidEmailMsg: String,
        mainViewModel: MainViewModel
    ) {
        val email = _loginUser.value.email
        val password = _loginUser.value.password

        if (!email.valid()) {
            showToast(invalidEmailMsg)
            return
        }

        viewModelScope.launch {
            _state.value = Loading
            val authResult = withContext(Dispatchers.IO) {
                repository.loginWithEmail(email, password)
            }

            resolveAuth(authResult, state, mainViewModel)
        }
    }

    /**
     * After google pop up results
     */
    fun signInWithGoogle(
        state: NavigationState,
        mainViewModel: MainViewModel
    ) {
        viewModelScope.launch {
            _state.value = GoogleLoading
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
            showToast(authResult.message)
            _state.value = Default
        }
    }

    private fun showToast(msg: String) {
        toastManager.showToast(msg)
    }
}

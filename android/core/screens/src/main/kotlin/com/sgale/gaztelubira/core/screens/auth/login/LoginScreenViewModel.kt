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
import com.sgale.gaztelubira.core.domain.auth.AuthResult.Success
import com.sgale.gaztelubira.core.domain.auth.IAuthRepository
import com.sgale.gaztelubira.core.screens.auth.login.LoginEvent.LoggedIn
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Default
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Loading.Login
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Loading.Login.Email
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Loading.Login.Google
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginUiState
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginUiState.LoginUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class LoginScreenViewModel @Inject constructor(
    private val repository: IAuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    internal val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _events = Channel<LoginEvent>(BUFFERED)
    internal val events: Flow<LoginEvent> = _events.receiveAsFlow()

    private fun updateUser(block: (LoginUser) -> LoginUser) {
        _state.update { state ->
            state.copy(
                user = block(state.user),
                error = null,
                auth = if (state.auth == AuthState.Error) Default else state.auth
            )
        }
    }

    internal fun onEmailChanged(value: String) = updateUser { it.copy(email = value) }
    internal fun onPasswordChanged(value: String) = updateUser { it.copy(password = value) }
    internal fun onTogglePasswordVisibility() = _state.update { state ->
        state.copy(user = state.user.copy(isPasswordVisible = !state.user.isPasswordVisible))
    }

    internal fun onLogin(provider: Login) {
        val user = _state.value.user

        /**
         * Only the email form has anything to validate: Google vouches for its own addresses.
         */
        if (provider == Email) {
            user.validate()?.let { validationError ->
                _state.update { it.copy(error = validationError, auth = Default) }
                return
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(auth = provider, error = null) }

            val result = withContext(Dispatchers.IO) {
                when (provider) {
                    Email -> repository.loginWithEmail(user.email, user.password)
                    Google -> repository.loginWithGoogle()
                }
            }

            if (result is Success) {
                _state.update { it.copy(auth = Default) }
                _events.send(LoggedIn)
            } else {
                _state.update { it.copy(auth = AuthState.Error) }
            }
        }
    }
}

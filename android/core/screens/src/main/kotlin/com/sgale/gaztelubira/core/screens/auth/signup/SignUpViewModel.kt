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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.auth.AuthResult.Success
import com.sgale.gaztelubira.core.domain.auth.usecase.SignUpEmail
import com.sgale.gaztelubira.core.screens.MainViewModel
import com.sgale.gaztelubira.core.screens.navigation.Destination.Splash
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Default
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Error
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState.Loading.SignUp
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpUiState
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpUiState.SignUpUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class SignUpViewModel @Inject constructor(
    private val signUpWithEmail: SignUpEmail
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpUiState())
    internal val state: StateFlow<SignUpUiState> = _state.asStateFlow()

    /**
     * Editing clears the last failure — but never interrupts a sign-up already in flight.
     */
    private fun updateUser(block: (SignUpUser) -> SignUpUser) {
        _state.update { state ->
            state.copy(
                user = block(state.user),
                error = null,
                auth = if (state.auth == Error) Default else state.auth
            )
        }
    }

    internal fun onNameChange(value: String) = updateUser { it.copy(name = value) }

    internal fun onEmailChange(value: String) = updateUser { it.copy(email = value) }

    internal fun onPasswordChange(value: String) = updateUser { it.copy(password = value) }

    internal fun onRepeatPasswordChange(value: String) = updateUser { it.copy(repeatPassword = value) }

    /**
     * Revealing a password is not an edit, so it leaves any showing error alone.
     */
    internal fun onTogglePasswordVisibility() = _state.update { state ->
        state.copy(user = state.user.copy(passwordVisible = !state.user.passwordVisible))
    }

    internal fun onToggleRepeatPasswordVisibility() = _state.update { state ->
        state.copy(
            user = state.user.copy(repeatPasswordVisible = !state.user.repeatPasswordVisible)
        )
    }

    internal fun signUp(
        navState: NavigationState,
        mainViewModel: MainViewModel
    ) {
        val user = _state.value.user

        user.validate()?.let { validationError ->
            _state.update { it.copy(error = validationError, auth = Default) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(auth = SignUp, error = null) }

            val result = withContext(Dispatchers.IO) {
                signUpWithEmail(user.name, user.email, user.password)
            }

            if (result is Success) {
                _state.update { it.copy(auth = Default) }
                mainViewModel.init(navState)
                navState.navigateTo(Splash)
            } else {
                _state.update { it.copy(auth = Error) }
            }
        }
    }
}

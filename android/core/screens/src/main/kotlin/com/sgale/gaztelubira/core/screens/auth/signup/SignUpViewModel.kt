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
import com.sgale.gaztelubira.core.domain.utils.IToastManager
import com.sgale.gaztelubira.core.screens.MainViewModel
import com.sgale.gaztelubira.core.screens.auth.AuthState
import com.sgale.gaztelubira.core.screens.auth.AuthState.Default
import com.sgale.gaztelubira.core.screens.auth.AuthState.Error
import com.sgale.gaztelubira.core.screens.auth.AuthState.Loading
import com.sgale.gaztelubira.core.screens.navigation.Destination.Splash
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpField
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpField.EMAIL
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpField.NAME
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpField.PASSWORD
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpField.PASSWORD_VISIBLE
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpField.REPEAT_PASSWORD
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpField.REPEAT_PASSWORD_VISIBLE
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpUiState
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpUiState.ValidationError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpWithEmail: SignUpEmail,
    private val toastManager: IToastManager
): ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    internal val uiState: StateFlow<SignUpUiState> = _uiState

    private val _state = MutableStateFlow<AuthState>(Default)
    val state: StateFlow<AuthState> = _state

    private val _error = MutableStateFlow<ValidationError?>(null)
    val error: StateFlow<ValidationError?> = _error

    fun updateField(field: SignUpField, value: Any? = null) {
        val current = _uiState.value.user
        _uiState.value = _uiState.value.copy(
            user = when(field) {
                NAME -> current.copy(name = value as String)
                EMAIL -> current.copy(email = value as String)
                PASSWORD -> current.copy(password = value as String)
                REPEAT_PASSWORD -> current.copy(repeatPassword = value as String)
                PASSWORD_VISIBLE -> current.copy(passwordVisible = !(current.passwordVisible))
                REPEAT_PASSWORD_VISIBLE -> current.copy(repeatPasswordVisible = !(current.repeatPasswordVisible))
            }
        )
    }

    fun signUp(
        state: NavigationState,
        msg: String,
        mainViewModel: MainViewModel
    ) {
        val error = _uiState.value.user.isNotValid()
        if (error != null) {
            _error.value = error
            _state.value = Error
            return
        }

        val name = _uiState.value.user.name
        val email = _uiState.value.user.email
        val password = _uiState.value.user.password

        viewModelScope.launch {
            _state.value = Loading
            val result = withContext(Dispatchers.IO){
                signUpWithEmail(name, email, password)
            }
            _state.value = Default
            if (result is Success) {
                mainViewModel.init(state)
                state.navigateTo(Splash)
            } else {
                showToast(msg)
            }
        }
    }

    fun showToast(msg: String) {
        toastManager.showToast(msg)
    }

    fun changeUiState(newState: AuthState) {
        _state.value = newState
    }
}

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
import com.sgale.gaztelubira.core.screens.navigation.Destination.Splash
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.auth.AuthState
import com.sgale.gaztelubira.core.screens.auth.AuthState.Default
import com.sgale.gaztelubira.core.screens.auth.AuthState.Error
import com.sgale.gaztelubira.core.screens.auth.AuthState.Loading
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpUser.ValidationError
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpViewModel.SignUpField.Email
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpViewModel.SignUpField.Name
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpViewModel.SignUpField.Password
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpViewModel.SignUpField.PasswordVisible
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpViewModel.SignUpField.RepeatPassword
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpViewModel.SignUpField.RepeatPasswordVisible
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpWithEmail: SignUpEmail,
    private val toastManager: IToastManager
): ViewModel() {
    enum class SignUpField {
        Name, Email, Password, RepeatPassword, PasswordVisible, RepeatPasswordVisible
    }
    private val _signUpUser = MutableStateFlow(SignUpUser())
    val signUpUser: StateFlow<SignUpUser> = _signUpUser

    private val _state = MutableStateFlow<AuthState>(Default)
    val state: StateFlow<AuthState> = _state

    private val _error = MutableStateFlow<ValidationError?>(null)
    val error: StateFlow<ValidationError?> = _error

    fun updateField(field: SignUpField, value: Any? = null) {
        val current = _signUpUser.value
        _signUpUser.value = when(field) {
            Name -> current.copy(name = value as String)
            Email -> current.copy(email = value as String)
            Password -> current.copy(password = value as String)
            RepeatPassword -> current.copy(repeatPassword = value as String)
            PasswordVisible -> current.copy(passwordVisible = !(current.passwordVisible))
            RepeatPasswordVisible -> current.copy(repeatPasswordVisible = !(current.repeatPasswordVisible))
        }
    }

    fun signUp(
        state: NavigationState,
        msg: String,
        mainViewModel: MainViewModel
    ) {
        val error = _signUpUser.value.isNotValid()
        if (error != null) {
            _error.value = error
            _state.value = Error
            return
        }

        val name = _signUpUser.value.name
        val email = _signUpUser.value.email
        val password = _signUpUser.value.password

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

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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sgale.gaztelubira.core.designsystem.components.GBScaffold
import com.sgale.gaztelubira.core.screens.LocalMainViewModel
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.auth.AuthState.Default
import com.sgale.gaztelubira.core.screens.auth.AuthState.Error
import androidx.compose.ui.res.stringResource

@Composable
fun SignUpScreen(
    state: NavigationState,
    viewModel: SignUpViewModel = hiltViewModel<SignUpViewModel>()
) {
    val mainViewModel = LocalMainViewModel.current

    val error by viewModel.error.collectAsState()
    val signUpState by viewModel.state.collectAsState()
    val signUpUser by viewModel.signUpUser.collectAsState()
    val signUpError = stringResource(R.string.error_sign_up)

    val errorMsg = stringResource(error?.messageRes ?: R.string.error_generic)

    LaunchedEffect(signUpState) {
        if (signUpState is Error) {
            viewModel.showToast(errorMsg)
            viewModel.changeUiState(Default)
        }
    }


    GBScaffold { modifier ->
        SignUpScreenUI(
            modifier = modifier,
            user = signUpUser,
            changeUserValue = { field, value -> viewModel.updateField(field, value) },
            signUpState = signUpState,
            signUp = { viewModel.signUp(state, signUpError, mainViewModel) }
        )
    }
}

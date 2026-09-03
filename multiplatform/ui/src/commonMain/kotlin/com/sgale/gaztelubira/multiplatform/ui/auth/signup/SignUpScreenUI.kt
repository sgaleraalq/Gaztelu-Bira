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

package com.sgale.gaztelubira.multiplatform.ui.auth.signup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState
import com.sgale.gaztelubira.multiplatform.ui.auth.common.AuthButton
import com.sgale.gaztelubira.multiplatform.ui.auth.common.AuthCard
import com.sgale.gaztelubira.multiplatform.ui.auth.common.AuthScaffold
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.ui.SignUpTextFields
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.sign_up
import com.sgale.gaztelubira.multiplatform.ui.resources.sign_up_exclamation
import org.jetbrains.compose.resources.stringResource

@Composable
fun SignUpScreenUI(
    modifier: Modifier,
    user: SignUpUser,
    changeUserValue: (SignUpField, Any) -> Unit,
    signUpState: AuthState,
    signUp: () -> Unit
) {
    val signUpText = stringResource(Res.string.sign_up_exclamation)
    AuthScaffold(modifier, signUpText) {
        SignUpCard(
            user = user,
            changeUserValue = { field, value -> changeUserValue(field, value) },
            signUpState = signUpState,
            signUp = { signUp() }
        )
    }
}

@Composable
fun SignUpCard(
    user: SignUpUser,
    changeUserValue: (SignUpField, Any) -> Unit,
    signUpState: AuthState,
    signUp: () -> Unit
) {
    val signUpTxt = stringResource(Res.string.sign_up)
    AuthCard {
        SignUpTextFields(user) { field, value -> changeUserValue(field, value) }
        Spacer(Modifier.height(16.dp))
        AuthButton(signUpTxt, signUpState) { signUp () }
    }
}

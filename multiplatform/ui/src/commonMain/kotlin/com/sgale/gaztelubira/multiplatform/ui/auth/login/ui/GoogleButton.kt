/*
 * Designed and developed by 2026 sgaleraalq (Sergio Galera)
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


package com.sgale.gaztelubira.multiplatform.ui.auth.login.ui

import androidx.compose.runtime.Composable
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.continue_as_guest
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_google
import com.sgale.gaztelubira.multiplatform.ui.resources.login_with_google
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun GoogleLogin(
    loginGoogle: () -> Unit
) {
    GBElevatedButton(
        text = stringResource(Res.string.login_with_google),
        onClick = { loginGoogle() },
        icon = painterResource(Res.drawable.ic_google)
    )
}

@Composable
fun ContinueAsGuest(
    onClick: () -> Unit
) {
    GBElevatedButton(
        text = stringResource(Res.string.continue_as_guest),
        onClick = { onClick() }
    )
}

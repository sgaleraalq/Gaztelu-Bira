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

package com.sgale.gaztelubira.core.screens.auth.login.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.components.GBText
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.designsystem.style.sign_up_color
import androidx.compose.ui.res.stringResource
import com.sgale.gaztelubira.core.screens.R

@Composable
fun DontHaveAccount(
    navigateToSignUp: () -> Unit
){
    Row(Modifier.fillMaxWidth()) {
        GBText(
            text = stringResource(R.string.dont_have_account),
            style = gBTypography().bodyMedium
        )
        Spacer(Modifier.width(8.dp))
        GBText(
            modifier = Modifier.clickable{ navigateToSignUp() },
            text = stringResource(R.string.sign_up_exclamation),
            style = gBTypography().bodyMedium,
            textColor = sign_up_color
        )
    }
}

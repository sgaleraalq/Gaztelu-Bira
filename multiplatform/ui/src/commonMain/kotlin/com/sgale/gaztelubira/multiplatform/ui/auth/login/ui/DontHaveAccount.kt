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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.sign_up_color
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.dont_have_account
import com.sgale.gaztelubira.multiplatform.ui.resources.sign_up_exclamation
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DontHaveAccount(
    navigateToSignUp: () -> Unit
){
    Row(Modifier.fillMaxWidth()) {
        GBText(
            text = stringResource(Res.string.dont_have_account),
            style = gBTypography().bodyMedium
        )
        Spacer(Modifier.width(8.dp))
        GBText(
            modifier = Modifier.clickable{ navigateToSignUp() },
            text = stringResource(Res.string.sign_up_exclamation),
            style = gBTypography().bodyMedium,
            textColor = sign_up_color
        )
    }
}

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

package com.sgale.gaztelubira.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.R
import com.sgale.gaztelubira.core.designsystem.style.gBTypography

@Composable
fun GBUpdateDialog() {
    GBDialog(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        show = true,
        dismiss = {}
    ) { modifier ->
        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = spacedBy(12.dp)
        ) {
            GBUpdateDialogTitle()
            GBUpdateDialogText()
            GBUpdateDialogButton()
        }
    }
}

@Composable
fun GBUpdateDialogTitle() {
    GBText(
        text = stringResource(R.string.good_news),
        style = gBTypography().headlineLarge.copy(
            color = White,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
fun GBUpdateDialogText() {
    GBText(
        text = stringResource(R.string.update_available),
        style = gBTypography().bodyMedium.copy(
            color = White
        )
    )
}

@Composable
fun GBUpdateDialogButton() {
//    // TODO missing url
//    val openUrl = rememberOpenUrl()
//
//    GBElevatedButton(
//        text = stringResource(R.string.update),
//        onClick = { openUrl("url") }
//    )
}

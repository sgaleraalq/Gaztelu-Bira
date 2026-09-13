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

package com.sgale.gaztelubira.multiplatform.ui.insert.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.softRed
import com.sgale.gaztelubira.multiplatform.ui.insert.InvalidInformationReason
import org.jetbrains.compose.resources.stringResource

/**
 * Tells the user which option is still holding the insert button back. The text comes from the
 * reason itself, so every insert form shares this one composable.
 */
@Composable
internal fun InvalidInformationMessage(reason: InvalidInformationReason?) {
    if (reason == null) return

    GBText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        text = stringResource(reason.reason),
        alignment = Center,
        textColor = softRed,
        style = gBTypography().bodyMedium,
        maxLines = 2
    )
}

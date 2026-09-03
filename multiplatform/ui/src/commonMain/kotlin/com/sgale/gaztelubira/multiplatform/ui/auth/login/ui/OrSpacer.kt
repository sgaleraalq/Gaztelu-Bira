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

package com.sgale.gaztelubira.multiplatform.ui.auth.login.ui

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.white_in_gray_box
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.or
import org.jetbrains.compose.resources.stringResource

@Composable
fun OrSpacer() {
    Row(
        modifier = Modifier.padding(12.dp),
        horizontalArrangement = spacedBy(18.dp),
        verticalAlignment = CenterVertically
    ) {
        HorizontalDivider(Modifier.weight(1f), thickness = 1.dp, color = white_in_gray_box.copy(0.8f))
        GBText(
            text = stringResource(Res.string.or).uppercase(),
            textColor = white_in_gray_box.copy(0.8f)
        )
        HorizontalDivider(Modifier.weight(1f), thickness = 1.dp, color = white_in_gray_box.copy(0.8f))
    }
}

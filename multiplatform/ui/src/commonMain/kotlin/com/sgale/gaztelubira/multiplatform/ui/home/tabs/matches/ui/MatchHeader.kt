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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.matches.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.model.GBMatchType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MatchHeader(
    matchName: String,
    matchType: GBMatchType
) {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalAlignment = CenterVertically
    ) {
        GBText(
            modifier = Modifier.weight(1f),
            text = matchName,
            alignment = Start,
            style = MaterialTheme.typography.bodyMedium
        )
        Row(
            verticalAlignment = CenterVertically,
            horizontalArrangement = spacedBy(8.dp)
        ) {
            GBText(
                text = stringResource(matchType.label)
                    .lowercase()
                    .replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodySmall
            )
            Icon(
                modifier = Modifier.size(12.dp),
                painter = painterResource(matchType.icon),
                contentDescription = null,
                tint = Unspecified
            )
        }
    }
}

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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expRs or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sgale.gaztelubira.multiplatform.designsystem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.app_name
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_button_add
import com.sgale.gaztelubira.multiplatform.ui.resources.img_gaztelu_bira
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GBTopAppBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.app_name),
    showAdminButton: Boolean = false,
    onButtonClicked: () -> Unit = {}
) {
    val logoModifier = Modifier
        .size(36.dp)
        .clip(RoundedCornerShape(50))
        .border(width = 1.dp, color = White, shape = RoundedCornerShape(50))

    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Bottom,
        horizontalArrangement = spacedBy(24.dp)
    ) {
        GBImage(
            modifier = logoModifier,
            painter = painterResource(Res.drawable.img_gaztelu_bira)
        )

        GBText(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 4.dp),
            text = title,
            alignment = Start,
            textColor = White,
            style = MaterialTheme.typography.titleLarge
        )

        if (showAdminButton) {
            GBAddButton { onButtonClicked() }
        }
    }
}

@Composable
fun GBAddButton(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit
) {
    Icon(
        modifier = modifier.size(36.dp).clickable { onButtonClicked() },
        painter = painterResource(Res.drawable.ic_button_add),
        contentDescription = null,
        tint = Unspecified
    )
}

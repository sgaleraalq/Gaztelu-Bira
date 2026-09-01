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

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.ContentPadding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.designsystem.style.lightGray

@Composable
fun GBElevatedButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    backgroundColor: Color = White,
    textColor: Color = Black,
    textStyle: TextStyle = gBTypography().bodyMedium,
    roundness: Int = 8,
    padding: PaddingValues = ContentPadding
) {
    ElevatedButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        contentPadding = padding,
        shape = RoundedCornerShape(roundness.dp),
        elevation = ButtonDefaults.buttonElevation(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = lightGray
        )
    ) {
        GBText(
            text = text,
            style = textStyle,
            textColor = textColor
        )
    }
}

@Composable
fun GBElevatedButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    backgroundColor: Color = White,
    textColor: Color = Black,
    roundness: Int = 8
) {
    ElevatedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(roundness.dp),
        elevation = ButtonDefaults.buttonElevation(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Center
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(icon),
                contentDescription = null,
                tint = Unspecified
            )
            Spacer(Modifier.width(24.dp))
            GBText(
                text = text,
                style = gBTypography().bodyMedium.copy(
                    fontWeight = Bold
                ),
                textColor = Black
            )
        }
    }
}

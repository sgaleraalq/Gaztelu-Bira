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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GBAnimatedMessage(
    msg: String?,
    dismissMsg: () -> Unit
) {
    var message by remember { mutableStateOf(msg) }

    LaunchedEffect(Unit) {
        delay(2.milliseconds)
        dismissMsg()
    }

    AnimatedVisibility(
        visible = message != null,
        enter = slideInVertically(
            initialOffsetY = { it }
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { it }
        ) + fadeOut()
    ) {
        GBMessage(text = message ?: "")
    }
}

@Composable
fun GBMessage(
    text: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
    ) {
        GBText(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            text = text,
            style = gBTypography().bodyLarge.copy(
                fontWeight = Bold,
                color = White
            ),
            alignment = TextAlign.Center
        )
    }
}

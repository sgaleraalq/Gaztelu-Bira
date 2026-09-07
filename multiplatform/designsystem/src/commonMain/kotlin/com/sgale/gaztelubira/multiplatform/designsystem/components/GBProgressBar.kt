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


package com.sgale.gaztelubira.multiplatform.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.primaryRed
import com.sgale.gaztelubira.multiplatform.designsystem.style.white_in_gray_box
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.loading_resources
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.milliseconds

private const val LOADING_DURATION_MS = 2_000
private const val FINISH_DURATION_MS = 500

/**
 * How far the bar is allowed to creep while the work is still running. It never reaches the end on
 * its own, so hitting 100% always means the caller said so.
 */
private const val LOADING_CAP = 0.75f

@Composable
fun GBProgressBar(
    modifier: Modifier,
    completed: Boolean,
    onFinish: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = spacedBy(12.dp)
    ) {
        GBProgressBarText()
        GBProgressBarBar(completed, onFinish)
    }
}

@Composable
private fun GBProgressBarText() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        GBText(
            text = stringResource(Res.string.loading_resources),
            style = gBTypography().bodyMedium,
            alignment = Center
        )
        Spacer(Modifier.width(8.dp))
        LoadingDots()
    }
}

@Composable
private fun LoadingDots() {
    val animatables = remember { List(3) { Animatable(0f) } }

    LaunchedEffect(Unit) {
        while (true) {
            animatables.forEach { animatable ->
                launch {
                    animatable.animateTo(
                        targetValue = -5f,
                        animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                    )
                    animatable.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                    )
                }
                delay(150.milliseconds)
            }
            delay(1500.milliseconds)
        }
    }

    Row(horizontalArrangement = Arrangement.Center) {
        animatables.forEach { anim ->
            GBText(
                modifier = Modifier
                    .offset(y = anim.value.dp)
                    .padding(horizontal = 2.dp),
                text = ".",
                style = gBTypography().bodyMedium
            )
        }
    }
}

@Composable
private fun GBProgressBarBar(
    completed: Boolean,
    onFinish: () -> Unit
) {
    val progress = remember { Animatable(0f) }
    val currentOnFinish by rememberUpdatedState(onFinish)

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = LOADING_CAP,
            animationSpec = tween(LOADING_DURATION_MS, easing = LinearEasing)
        )
    }

    /**
     * Animating the same [Animatable] cancels the creep above and picks up from wherever it got to,
     * so the bar never jumps backwards no matter when the work finishes.
     */
    LaunchedEffect(completed) {
        if (!completed) return@LaunchedEffect
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(FINISH_DURATION_MS, easing = LinearEasing)
        )
        currentOnFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(5.dp)
            .background(white_in_gray_box)
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.value)
                .background(primaryRed)
        )
    }
}

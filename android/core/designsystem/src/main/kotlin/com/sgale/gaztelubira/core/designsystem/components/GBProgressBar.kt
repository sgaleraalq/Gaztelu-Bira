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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.R
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.designsystem.style.primaryRed
import com.sgale.gaztelubira.core.designsystem.style.white_in_gray_box
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GBProgressBar(
    modifier: Modifier,
    completed: Boolean,
    avoid: Boolean,
    onFinish: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = spacedBy(12.dp)
    ) {
        GBProgressBarText(avoid)
        GBProgressBarBar(completed, avoid) { onFinish() }
    }
}

@Composable
fun GBProgressBarText(avoid: Boolean) {
    if (avoid) return
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        GBText(
            text = stringResource(R.string.loading_resources),
            style = gBTypography().bodyMedium,
            alignment = Center
        )
        Spacer(Modifier.width(8.dp))
        LoadingDots()
    }
}

@Composable
fun LoadingDots() {
    val animatables = remember { List(3) { Animatable(0f) } }

    LaunchedEffect(Unit) {
        while (true) {
            animatables.forEachIndexed { index, animatable ->
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
fun GBProgressBarBar(
    completed: Boolean,
    avoid: Boolean,
    onFinish: () -> Unit
) {
    if (avoid) { onFinish(); return }

    var loadingFraction by remember { mutableFloatStateOf(0f) }
    val maxTimeBeforeFinish = 2_000_000_000L
    val finishDuration = 500_000_000L
    val maxFractionBeforeFinish = 0.75f

    LaunchedEffect(completed) {
        if (!completed) {
            animateFraction(
                state = { loadingFraction = it },
                from = 0f,
                to = maxFractionBeforeFinish,
                durationNanos = maxTimeBeforeFinish
            )
        } else {
            animateFraction(
                state = { loadingFraction = it },
                from = loadingFraction,
                to = 1f,
                durationNanos = finishDuration
            )
            onFinish()
        }
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
                .fillMaxWidth(loadingFraction)
                .background(primaryRed)
        )
    }
}

suspend fun animateFraction(
    state: (Float) -> Unit,
    from: Float,
    to: Float,
    durationNanos: Long
) {
    var lastTime = withFrameNanos { it }
    var elapsed = 0L
    while (elapsed < durationNanos) {
        val now = withFrameNanos { it }
        val delta = now - lastTime
        lastTime = now
        elapsed += delta
        state((from + (to - from) * (elapsed.toFloat() / durationNanos)).coerceIn(0f, 1f))
    }
    state(to)
}

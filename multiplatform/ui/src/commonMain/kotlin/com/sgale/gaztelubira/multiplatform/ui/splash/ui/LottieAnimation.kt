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

package com.sgale.gaztelubira.multiplatform.ui.splash.ui

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBLocalImage
import com.sgale.gaztelubira.multiplatform.ui.AppAnimations
import com.sgale.gaztelubira.multiplatform.ui.AppAnimations.SPLASH
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import io.github.alexzhirkevich.compottie.Compottie.IterateForever
import io.github.alexzhirkevich.compottie.ExperimentalCompottieApi
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.Resource
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter

@OptIn(ExperimentalCompottieApi::class)
@Composable
fun LottieAnimation(modifier: Modifier) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.Resource(
            path = SPLASH,
            reader = Res::readBytes
        )
    }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = IterateForever
    )

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(composition) {
        if (composition != null) visible = true
    }

    val transition = updateTransition(visible, label = "LottieTransition")
    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) },
        label = "alpha"
    ) { if (it) 1f else 0f }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Center
    ) {
        GBLocalImage(
            modifier = Modifier.size(100.dp).graphicsLayer { this.alpha = 1f - alpha },
            painter = AppImages.appLogo,
            contentScale = Fit
        )

        composition?.let {
            Image(
                modifier = Modifier.graphicsLayer { this.alpha = alpha },
                painter = rememberLottiePainter(
                    composition = it,
                    progress = { progress }
                ),
                contentDescription = null
            )
        }
    }
}

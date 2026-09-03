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

package com.sgale.gaztelubira.core.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import com.sgale.gaztelubira.core.screens.splash.ui.LottieAnimation
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBProgressBar
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBScaffold

@Composable
fun SplashScreenUI(
    completed: Boolean,
    avoid: Boolean,
    navigate: () -> Unit
) {
    GBScaffold(
        topBarTitle = "",
        showTopAppBar = true,
        content = { modifier ->
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                LottieAnimation(Modifier.align(Center))
                GBProgressBar(
                    modifier = Modifier.align(BottomCenter),
                    completed = completed,
                    avoid = avoid,
                    onFinish = { navigate() }
                )
            }
    )
}

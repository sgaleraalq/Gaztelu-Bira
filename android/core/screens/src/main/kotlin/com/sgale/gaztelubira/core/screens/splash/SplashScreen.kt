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

package com.sgale.gaztelubira.core.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.splash.SplashActions
import com.sgale.gaztelubira.multiplatform.ui.splash.SplashView

@Composable
internal fun SplashScreen(
    navState: NavigationState,
    viewModel: SplashViewModel = hiltViewModel<SplashViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SplashView(
        state = state,
        actions = remember(viewModel, navState) {
            SplashActions(
                navigate = { viewModel.navigate(navState) }
            )
        }
    )
}

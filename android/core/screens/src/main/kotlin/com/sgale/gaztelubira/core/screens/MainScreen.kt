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

package com.sgale.gaztelubira.core.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.sgale.gaztelubira.core.screens.navigation.MainNavigation
import com.sgale.gaztelubira.core.screens.navigation.rememberNavigationState

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel<MainViewModel>()
) {
    val navigationState = rememberNavigationState()

    LaunchedEffect(true) {
        mainViewModel.init(navigationState)
    }

    CompositionLocalProvider(LocalMainViewModel provides mainViewModel) {
        Scaffold { _ ->
            MainNavigation(navigationState)
        }
    }
}

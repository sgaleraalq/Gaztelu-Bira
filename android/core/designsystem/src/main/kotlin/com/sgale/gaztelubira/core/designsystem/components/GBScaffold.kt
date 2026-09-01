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
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sgale.gaztelubira.core.domain.model.team.TeamModel

@Composable
fun GBScaffold(
    appTeam: TeamModel? = null,
    showTopAppBar: Boolean = false,
    topBarTitle: String? = null,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            if (showTopAppBar) {
                GBTopAppBar(
                    appTeam = appTeam,
                    topBarTitle = topBarTitle,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                )
            }
        },
        bottomBar = { bottomBar() }
    ) {
        GBBackground()
        content(Modifier.padding(it).fillMaxSize())
    }
}

@Composable
fun GBScaffold(
    @DrawableRes appLogo: Int,
    @StringRes appName: Int,
    showTopAppBar: Boolean = false,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            if (showTopAppBar) {
                GBTopAppBar(
                    appLogo = appLogo,
                    appName = appName,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                )
            }
        },
        bottomBar = { bottomBar() }
    ) {
        GBBackground()
        content(Modifier.padding(it).fillMaxSize())
    }
}

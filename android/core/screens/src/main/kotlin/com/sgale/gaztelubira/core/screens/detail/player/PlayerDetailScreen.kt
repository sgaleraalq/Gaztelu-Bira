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

package com.sgale.gaztelubira.core.screens.detail.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.screens.navigation.Destination.Companion.navigateTo
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.showToast
import com.sgale.gaztelubira.multiplatform.ui.detail.player.PlayerDetailActions
import com.sgale.gaztelubira.multiplatform.ui.detail.player.PlayerDetailView
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.not_yet_available
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PlayerDetailScreen(
    navState: NavigationState,
    playerId: String,
    isManager: Boolean,
    viewModel: PlayerDetailViewModel = hiltViewModel<PlayerDetailViewModel>()
) {
    val context = LocalContext.current
    val notAvailableYetMsg = stringResource(Res.string.not_yet_available)

    LaunchedEffect(true) {
        viewModel.updateState(playerId, isManager)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()


    val actions = remember(navState) {
        PlayerDetailActions(
            showToast = { showToast(context, notAvailableYetMsg) },
            navigateTo = { destination -> navState.navigateTo(destination) }
        )
    }

    PlayerDetailView(state, actions)
}

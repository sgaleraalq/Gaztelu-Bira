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

package com.sgale.gaztelubira.core.screens.player_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sgale.gaztelubira.core.designsystem.components.GBScaffold
import com.sgale.gaztelubira.core.screens.LocalMainViewModel

internal const val LOGO_SIZE = 50

@Composable
fun PlayerDetailScreen(
    playerId: String,
    isManager: Boolean,
    navigateBack: () -> Unit,
    viewModel: PlayerDetailViewModel = hiltViewModel<PlayerDetailViewModel>()
) {
    val mainViewModel = LocalMainViewModel.current
    val user by mainViewModel.userSession.collectAsState()

    val playerStats by viewModel.playerStats.collectAsState()
    val playerInformation by viewModel.playerInformation.collectAsState()
    val playerState by viewModel.playerState.collectAsState()

    LaunchedEffect(true, user) {
        if (user == null) return@LaunchedEffect
        viewModel.loadPlayerInformation(user?.team, playerId)
    }

    GBScaffold { modifier ->
        PlayerDetailScreenUI(
            modifier = modifier,
            user = user,
            isManager = isManager,
            playerInformation = playerInformation,
            playerStats = playerStats,
            playerState = playerState,
            navigateBack = { navigateBack() }
        )
    }
}

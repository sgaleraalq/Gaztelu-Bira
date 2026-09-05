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

package com.sgale.gaztelubira.core.screens.home.tabs.matches

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sgale.gaztelubira.core.domain.auth.UserSession
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.screens.BuildConfig
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.home.tabs.matches.ui.MatchesList
import com.sgale.gaztelubira.core.screens.navigation.Destination
import com.sgale.gaztelubira.core.screens.navigation.Destination.InsertMatch
import com.sgale.gaztelubira.core.screens.navigation.Destination.MatchDetail
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBAnimatedMessage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTopAppBar

@Composable
fun MatchesScreenUI(
    user: UserSession?,
    matches: List<MatchModel>,
    hasEnoughPlayers: Boolean,
    calculateResult: GetMatchResultUseCase,
    navigateTo: (Destination) -> Unit
) {
    val debug = BuildConfig.DEBUG
    var showGBMessage by remember { mutableStateOf(false) }
    val notEnoughPlayersMsg = stringResource(R.string.not_enough_players)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GBTopAppBar(
            showAdminButton = user?.isAdmin() == true,
            onButtonClicked = {
                if (hasEnoughPlayers || debug) {
                    navigateTo(InsertMatch)
                } else {
                    showGBMessage = true
                }
            }
        )
        MatchesList(
            matches = matches,
            matchResult = { match -> calculateResult(match, user?.team) },
            onMatchClicked = { matchId ->
                navigateTo(MatchDetail(matchId))
            }
        )
    }

    if (showGBMessage) {
        GBAnimatedMessage(
            msg = notEnoughPlayersMsg,
            dismissMsg = { showGBMessage = false }
        )
    }
}

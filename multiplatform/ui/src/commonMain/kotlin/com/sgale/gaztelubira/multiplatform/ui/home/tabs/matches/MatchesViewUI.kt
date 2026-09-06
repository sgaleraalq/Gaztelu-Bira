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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.matches

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBAnimatedMessage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTopAppBar
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromMatchesTab.MatchDetail
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.matches.ui.Matches
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.not_enough_players
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MatchesViewUI(
    state: MatchesUiState,
    actions: MatchesActions
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GBTopAppBar(
            showAdminButton = state.isAdmin,
            onButtonClicked = actions.onAddMatchClicked
        )
        Matches(
            matches = state.matches,
            onMatchClicked = { matchId -> actions.navigateTo(MatchDetail(matchId)) }
        )
    }

    GBAnimatedMessage(
        show = state.showNotEnoughPlayers,
        msg = stringResource(Res.string.not_enough_players),
        dismissMsg = actions.onDismissMessage
    )
}

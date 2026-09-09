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

package com.sgale.gaztelubira.multiplatform.ui.detail.match

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBProgressDialog
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.Back
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Details
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Lineup
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Loading
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Stats
import com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.MatchDetailHeader
import com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.MatchDetailInformationBar
import com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.MatchDetailStateDetails
import com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.MatchDetailStateLineUp
import com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.MatchDetailStateStats

@Composable
internal fun MatchDetailViewUI(
    state: MatchDetailUiState,
    actions: MatchDetailActions
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MatchDetailHeader(
            localTeam = state.localTeam,
            localGoals = state.localGoals,
            visitorTeam = state.visitorTeam,
            visitorGoals = state.visitorGoals,
            onBackPressed = { actions.navigateTo(Back) }
        )
        MatchDetailInformationBar(
            state = state.uiState,
            onDetailsClicked = { actions.changeUiState(Details(state.information)) },
            onLineUpsClicked = { actions.changeUiState(Lineup(state.lineUp)) },
            onStatsClicked = { actions.changeUiState(Stats(state.stats)) }
        )

        when (state.uiState) {
            Loading -> GBProgressDialog(show = true, color = Red)
            is Details -> MatchDetailStateDetails(Modifier.weight(1f), state.uiState)
            is Lineup -> MatchDetailStateLineUp(Modifier.weight(1f), state.team, state.uiState)
            is Stats -> MatchDetailStateStats(Modifier.weight(1f), state.uiState)
        }
    }
}

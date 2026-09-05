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

package com.sgale.gaztelubira.core.screens.match_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import com.sgale.gaztelubira.core.domain.auth.UserSession
import com.sgale.gaztelubira.core.screens.match_detail.MatchDetailState.Details
import com.sgale.gaztelubira.core.screens.match_detail.MatchDetailState.Lineup
import com.sgale.gaztelubira.core.screens.match_detail.MatchDetailState.Loading
import com.sgale.gaztelubira.core.screens.match_detail.MatchDetailState.Stats
import com.sgale.gaztelubira.core.screens.match_detail.states.information.MatchDetailStateDetails
import com.sgale.gaztelubira.core.screens.match_detail.states.line_up.MatchDetailStateLineUp
import com.sgale.gaztelubira.core.screens.match_detail.states.stats.MatchDetailStateStats
import com.sgale.gaztelubira.core.screens.match_detail.ui.MatchDetailHeader
import com.sgale.gaztelubira.core.screens.match_detail.ui.MatchDetailInformationBar
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBProgressDialog

@Composable
fun MatchDetailScreenUI(
    modifier: Modifier,
    user: UserSession?,
    data: MatchDetailUiState,
    changeUiState: (MatchDetailState) -> Unit,
    navigateBack: () -> Unit
) {
    Column(modifier.fillMaxSize()) {
        MatchDetailHeader(
            localTeam = data.localTeam,
            localGoals = data.localGoals,
            visitorTeam = data.visitorTeam,
            visitorGoals = data.visitorGoals,
            onBackPressed = { navigateBack() }
        )
        MatchDetailInformationBar(
            state = data.uiState,
            onDetailsClicked = { changeUiState(Details(data.information)) },
            onLineUpsClicked = { changeUiState(Lineup(data.lineUp)) },
            onStatsClicked = { changeUiState(Stats(data.stats)) }
        )

        when (data.uiState) {
            Loading -> GBProgressDialog(show = true, color = Red)
            is Details -> MatchDetailStateDetails(Modifier.weight(1f), data.uiState)
            is Lineup -> MatchDetailStateLineUp(Modifier.weight(1f), user?.team, data.uiState)
            is Stats -> MatchDetailStateStats(Modifier.weight(1f), data.uiState)
        }
    }
}

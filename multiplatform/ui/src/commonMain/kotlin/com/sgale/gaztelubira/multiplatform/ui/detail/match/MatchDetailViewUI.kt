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
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Details
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Lineup
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Loading
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Stats
import com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.header.MatchDetailHeader
import com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.header.MatchDetailInformationBar
import com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.detail.MatchDetailStateDetails
import com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.detail.MatchDetailStateLineUp
import com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.detail.MatchDetailStateStats

@Composable
internal fun MatchDetailViewUI(
    modifier: Modifier,
    state: MatchDetailUiState,
    actions: MatchDetailActions
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        MatchDetailHeader(
            state = state,
            onBackPressed = { actions.navigateTo(Back) }
        )
        MatchDetailInformationBar(
            state = state,
            actions = actions,
        )

        DisplayDetail(
            modifier = Modifier.weight(1f),
            state = state
        )
    }
}

@Composable
private fun DisplayDetail(
    modifier: Modifier,
    state: MatchDetailUiState
) = when (state.uiState) {
    Loading -> GBProgressDialog(show = true, color = Red)
    is Details -> MatchDetailStateDetails(modifier = modifier, state = state.uiState)
    is Lineup -> MatchDetailStateLineUp(modifier = modifier, team = state.team, state = state.uiState)
    is Stats -> MatchDetailStateStats(modifier = modifier, state = state.uiState)
}

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

package com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.ui.detail.match.MatchDetailActions
import com.sgale.gaztelubira.multiplatform.ui.detail.match.MatchDetailUiState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Details
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Lineup
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Stats
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.details
import com.sgale.gaztelubira.multiplatform.ui.resources.line_ups
import com.sgale.gaztelubira.multiplatform.ui.resources.stats
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MatchDetailInformationBar(
    state: MatchDetailUiState,
    actions: MatchDetailActions
) {
    Column(
        modifier = Modifier
            .padding(bottom = 8.dp).padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            MatchDetailBox(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.details),
                isHighlighted = state.uiState is Details,
                onClick = { actions.changeUiState(Details(state.information)) }
            )
            MatchDetailBox(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.line_ups),
                isHighlighted = state.uiState is Lineup,
                onClick = { actions.changeUiState(Lineup(state.lineUp)) }
            )
            MatchDetailBox(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.stats),
                isHighlighted = state.uiState is Stats,
                onClick = { actions.changeUiState(Stats(state.stats)) }
            )
        }
        MatchDetailPointer(state.uiState)
    }
}

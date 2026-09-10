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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBBackButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTeam
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.model.GBTeam
import com.sgale.gaztelubira.multiplatform.ui.detail.match.MatchDetailUiState

@Composable
internal fun MatchDetailHeader(
    state: MatchDetailUiState,
    onBackPressed: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically
    ) {
        GBBackButton(
            onClick = { onBackPressed() }
        )
        GBMatchDetailResult(
            modifier = Modifier.weight(1f),
            state = state
        )
        GBBackButton(
            isVisible = false,
            onClick = {}
        )
    }
}

@Composable
private fun GBMatchDetailResult(
    modifier: Modifier,
    state: MatchDetailUiState,
) {
    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically
    ) {
        GBTeamDetailResult(
            modifier = Modifier.weight(1f),
            teamModel = state.localTeam,
            goals = state.localGoals
        )
        GBText(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = ":"
        )
        GBTeamDetailResult(
            modifier = Modifier.weight(1f),
            teamModel = state.visitorTeam,
            goals = state.visitorGoals,
            isLocal = false
        )
    }

}

@Composable
private fun GBTeamDetailResult(
    modifier: Modifier,
    teamModel: GBTeam?,
    goals: Int,
    isLocal: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically
    ) {
        if (!isLocal) {
            GBText(
                text = goals.toString(),
                style = gBTypography().headlineMedium
            )
        }
        Spacer(Modifier.weight(1f))
        GBTeam(
            modifier = Modifier.size(36.dp),
            image = teamModel?.logo
        )
        Spacer(Modifier.weight(1f))
        if (isLocal) {
            GBText(
                text = goals.toString(),
                style = gBTypography().headlineMedium
            )
        }
    }
}

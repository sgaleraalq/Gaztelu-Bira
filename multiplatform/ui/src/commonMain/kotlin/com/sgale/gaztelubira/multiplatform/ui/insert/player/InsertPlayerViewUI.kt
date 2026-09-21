/*
 * Designed and developed by 2026 sgale (Sergio Galera)
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

package com.sgale.gaztelubira.multiplatform.ui.insert.player

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.player.ui.InsertPlayerButton
import com.sgale.gaztelubira.multiplatform.ui.insert.player.ui.dialogs.InsertPlayerDialogs
import com.sgale.gaztelubira.multiplatform.ui.insert.player.ui.InsertPlayerImages
import com.sgale.gaztelubira.multiplatform.ui.insert.player.ui.InsertPlayerMainInformation

@Composable
internal fun InsertPlayerViewUI(
    modifier: Modifier,
    state: InsertPlayerUiState,
    actions: InsertPlayerActions,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        InsertPlayerForm(
            state = state,
            actions = actions
        )
        InsertPlayerButton(
            modifier = Modifier.weight(1f),
            state = state,
            actions = actions
        )
    }

    InsertPlayerDialogs(
        state = state,
        actions = actions
    )
}

@Composable
private fun InsertPlayerForm(
    state: InsertPlayerUiState,
    actions: InsertPlayerActions
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(8.dp)
    ) {
        InsertPlayerMainInformation(
            state = state,
            actions = actions
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        InsertPlayerImages(
            state = state,
            actions = actions
        )
    }
}

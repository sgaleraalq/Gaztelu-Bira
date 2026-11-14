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

package com.sgale.gaztelubira.multiplatform.ui.insert.match

import androidx.compose.runtime.Composable
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBScaffold
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.MatchDialogs

/**
 * Shared entry point for the insert match screen: state in, actions out, nothing
 * platform-specific.
 *
 * The screen draws its own header instead of asking the scaffold for the app's top bar: this one
 * carries a back button and the matchday.
 */
@Composable
fun InsertMatchView(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    GBScaffold { modifier ->
        InsertMatchViewUI(
            modifier = modifier,
            state = state,
            actions = actions
        )
        MatchDialogs(
            state = state,
            actions = actions
        )
    }
}

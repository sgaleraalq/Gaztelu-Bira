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

package com.sgale.gaztelubira.multiplatform.ui.insert.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBBackButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBScaffold
import com.sgale.gaztelubira.multiplatform.designsystem.style.gb_text_field_label_color
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.Back
import com.sgale.gaztelubira.multiplatform.ui.insert.player.ui.PlayerDetailImage
import com.sgale.gaztelubira.multiplatform.ui.insert.player.ui.PlayerDetailInformationBox

@Composable
internal fun PlayerDetailViewUI(
    state: PlayerDetailUiState,
    actions: PlayerDetailActions,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        PlayerDetailImage(
            bodyImage = state.player.image,
            placeholder = if (state.isManager) AppImages.manager else AppImages.bodyPlayer
        )
        PlayerDetailInformationBox(
            modifier = Modifier.align(BottomCenter),
            state = state,
            actions = actions
        )
        GBBackButton(
            modifier = Modifier.align(TopStart),
            showBackground = true,
            color = gb_text_field_label_color,
            onClick = { actions.navigateTo(Back) }
        )
    }
}

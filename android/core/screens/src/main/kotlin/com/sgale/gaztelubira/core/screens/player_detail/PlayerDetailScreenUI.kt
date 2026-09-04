/*
 * Designed and developed by 2025 sgaleraalq (Sergio Galera)
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

package com.sgale.gaztelubira.core.screens.player_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import com.sgale.gaztelubira.core.domain.auth.UserSession
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerStatsModel
import com.sgale.gaztelubira.core.screens.player_detail.ui.PlayerDetailImage
import com.sgale.gaztelubira.core.screens.player_detail.ui.PlayerDetailInformationBox
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBBackButton
import com.sgale.gaztelubira.multiplatform.designsystem.style.gb_text_field_label_color
import com.sgale.gaztelubira.multiplatform.ui.AppImages

@Composable
fun PlayerDetailScreenUI(
    modifier: Modifier,
    user: UserSession?,
    isManager: Boolean,
    playerInformation: PlayerModel?,
    playerStats: PlayerStatsModel?,
    playerState: PlayerDetailState?,
    navigateBack: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        PlayerDetailImage(
            bodyImage = playerInformation?.bodyImage,
            placeholder = if (isManager) AppImages.manager else AppImages.bodyPlayer
        )
        PlayerDetailInformationBox(
            logoUrl = user?.team?.logo,
            modifier = Modifier.align(BottomCenter),
            player = playerInformation,
            playerStats = playerStats,
            state = playerState
        )
        GBBackButton(
            modifier = Modifier.align(TopStart),
            showBackground = true,
            color = gb_text_field_label_color
        ) {
            navigateBack()
        }
    }
}

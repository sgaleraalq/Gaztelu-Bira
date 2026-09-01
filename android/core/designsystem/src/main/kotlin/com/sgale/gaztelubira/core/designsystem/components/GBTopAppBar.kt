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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expRs or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sgale.gaztelubira.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.dp
import com.gbmultiplatform.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.designsystem.R
import com.sgale.gaztelubira.core.designsystem.components.SaverStatus.Team

@Composable
fun GBTopAppBar(
    modifier: Modifier = Modifier,
    appTeam: TeamModel?,
    showAdminButton: Boolean? = false,
    topBarTitle: String?,
    onButtonClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Bottom,
        horizontalArrangement = spacedBy(24.dp)
    ) {
        /**
         * App or Team logo
         */
        GBImage(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(50))
                .border(width = 1.dp, color = White, shape = RoundedCornerShape(50)),
            image = appTeam?.logo,
            saverStatus = Team
        )

        /**
         * Team name
         */
        GBText(
            modifier = Modifier.weight(1f).padding(bottom = 4.dp),
            text = topBarTitle ?: appTeam?.name,
            alignment = Start,
            textColor = White,
            style = MaterialTheme.typography.titleLarge
        )

        if (showAdminButton == true) {
            GBAddButton { onButtonClicked() }
        }
    }
}

@Composable
fun GBTopAppBar(
    @DrawableRes appLogo: Int,
    @StringRes appName: Int,
    modifier: Modifier = Modifier,
    showAdminButton: Boolean? = false,
    onButtonClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Bottom,
        horizontalArrangement = spacedBy(24.dp)
    ) {
        /**
         * App or Team logo
         */
        GBImage(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(50))
                .border(width = 1.dp, color = White, shape = RoundedCornerShape(50)),
            image = appLogo
        )

        /**
         * Team name
         */
        GBText(
            modifier = Modifier.weight(1f).padding(bottom = 4.dp),
            text = stringResource(appName),
            alignment = Start,
            textColor = White,
            style = MaterialTheme.typography.titleLarge
        )

        if (showAdminButton == true) {
            Icon(
                modifier = Modifier.size(36.dp).clickable { onButtonClicked() },
                painter = painterResource(R.drawable.ic_button_add),
                contentDescription = null,
                tint = Unspecified
            )
        }
    }
}

@Composable
fun GBAddButton(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit
) {
    Icon(
        modifier = modifier.size(36.dp).clickable { onButtonClicked() },
        painter = painterResource(R.drawable.ic_button_add),
        contentDescription = null,
        tint = Unspecified
    )
}

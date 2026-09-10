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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBPlayerImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.lightGray
import com.sgale.gaztelubira.multiplatform.model.GBPlayerStatsDetail
import com.sgale.gaztelubira.multiplatform.model.GBStatValue
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PlayerCard(
    player: GBPlayerStatsDetail?,
    onDismiss: () -> Unit
) {
    if (player == null) return

    GBDialog(
        dismiss = onDismiss,
        color = lightGray
    ) { modifier ->
        PlayerStatsCard(modifier, player)
    }
}

@Composable
private fun PlayerStatsCard(
    modifier: Modifier,
    player: GBPlayerStatsDetail
) {

    Column(
        modifier = modifier
            .size(500.dp)
            .padding(12.dp)
    ) {
        PlayerInformation(
            image = player.faceImage,
            name = player.name
        )

        PlayerStats(
            player = player
        )
    }
}

@Composable
private fun PlayerInformation(
    image: String?,
    name: String
) {
    Column(Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            contentAlignment = Center
        ) {
            GBPlayerImage(
                modifier = Modifier.size(100.dp),
                image = image,
                borderColor = Black
            )
        }
        GBText(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, start = 8.dp, end = 8.dp),
            text = name,
            alignment = TextAlign.Center,
            textColor = Black
        )
    }
}

@Composable
private fun PlayerStats(
    player: GBPlayerStatsDetail
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        player.stats.forEach { statValue ->
            PlayerStat(statValue)
        }
    }
}

@Composable
private fun PlayerStat(
    statValue: GBStatValue
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(statValue.stat.icon),
            contentDescription = null,
            tint = Unspecified
        )
        Spacer(Modifier.width(12.dp))
        GBText(
            modifier = Modifier.weight(1f),
            text = stringResource(statValue.stat.label),
            alignment = TextAlign.Start,
            style = gBTypography().bodySmall,
            textColor = Black
        )
        GBText(
            text = statValue.value,
            alignment = TextAlign.Start,
            style = gBTypography().bodySmall,
            textColor = Black
        )
    }
}

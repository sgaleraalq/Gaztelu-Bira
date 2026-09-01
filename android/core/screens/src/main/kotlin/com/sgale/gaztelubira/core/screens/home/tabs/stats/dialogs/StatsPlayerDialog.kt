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

package com.sgale.gaztelubira.core.screens.home.tabs.stats.dialogs

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.components.GBDialog
import com.sgale.gaztelubira.core.designsystem.components.GBPlayerImage
import com.sgale.gaztelubira.core.designsystem.components.GBText
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.designsystem.style.lightGray
import com.sgale.gaztelubira.core.domain.model.player.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.domain.model.player.Stat.Assists
import com.sgale.gaztelubira.core.domain.model.player.Stat.CleanSheets
import com.sgale.gaztelubira.core.domain.model.player.Stat.Fails
import com.sgale.gaztelubira.core.domain.model.player.Stat.GamesPlayed
import com.sgale.gaztelubira.core.domain.model.player.Stat.Goals
import com.sgale.gaztelubira.core.domain.model.player.Stat.GoalsProvoked
import com.sgale.gaztelubira.core.domain.model.player.Stat.PenaltiesProvoked
import com.sgale.gaztelubira.core.domain.model.player.Stat.Percentage
import com.sgale.gaztelubira.core.domain.model.player.Stat.RedCards
import com.sgale.gaztelubira.core.domain.model.player.Stat.Saves
import com.sgale.gaztelubira.core.domain.model.player.Stat.YellowCards
import com.sgale.gaztelubira.core.domain.utils.formatDecimal
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GBSelectedPlayerDialog(
    player: PlayerStatsModel?,
    calculatePercentage: (PlayerStatsModel) -> Double,
    onDismiss: () -> Unit
) {
    if (player == null) return

    val scrollState = rememberScrollState()

    GBDialog(
        dismiss = { onDismiss() },
        color = lightGray
    ) { modifier ->
        Column(
            modifier = modifier
                .size(500.dp)
                .padding(12.dp)
        ) {
            GBPlayerDialogHeader(
                image = player.player.faceImage,
                name = player.player.name
            )

            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Stat.entries.forEach { stat ->
                    val value: Number = when (stat) {
                        Goals -> player.stats.values.sumOf { it.goals }
                        GoalsProvoked -> player.stats.values.sumOf { it.goalsProvoked }
                        Assists -> player.stats.values.sumOf { it.assists }
                        CleanSheets -> player.stats.values.sumOf { it.cleanSheets }
                        PenaltiesProvoked -> player.stats.values.sumOf { it.penaltiesProvoked }
                        Saves -> player.stats.values.sumOf { it.saves }
                        Fails -> player.stats.values.sumOf { it.fails }
                        YellowCards -> player.stats.values.sumOf { it.yellowCards }
                        RedCards -> player.stats.values.sumOf { it.redCards }
                        GamesPlayed -> player.stats.values.sumOf { it.gamesPlayed }
                        Percentage -> calculatePercentage(player)
                    }

                    val sValue = if (stat == Percentage) {
                        val formatted = formatDecimal(value as Double)
                        "$formatted %"
                    } else {
                        value.toString()
                    }

                    GBPlayerDialogStat(
                        statName = stat.statName,
                        statIcon = stat.icon,
                        stat = sValue
                    )
                }
            }
        }
    }
}

@Composable
fun GBPlayerDialogHeader(
    image: String?,
    name: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                 .fillMaxWidth()
                .padding(vertical = 8.dp),
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
fun GBPlayerDialogStat(
    statName: Int,
    statIcon: Int,
    stat: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(statIcon),
            contentDescription = null,
            tint = Unspecified
        )
        Spacer(Modifier.width(12.dp))
        GBText(
            modifier = Modifier.weight(1f),
            text = stringResource(statName),
            alignment = Start,
            style = gBTypography().bodySmall,
            textColor = Black
        )
        GBText(
            text = stat,
            alignment = Start,
            style = gBTypography().bodySmall,
            textColor = Black
        )
    }
}

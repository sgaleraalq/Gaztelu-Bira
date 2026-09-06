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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg_not_selected
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.lightGray
import com.sgale.gaztelubira.multiplatform.designsystem.style.login_container_color
import com.sgale.gaztelubira.multiplatform.designsystem.style.softRed
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.model.GBStat.PERCENTAGE
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.GBStatsSettings
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.GBStatsSettings.ChangePunctuation
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.GBStatsSettings.ChangeStat
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.GBStatsSettings.Hidden
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.GBStatsSettings.Menu
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.StatsActions
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.change_punctuation
import com.sgale.gaztelubira.multiplatform.ui.resources.change_stat
import com.sgale.gaztelubira.multiplatform.ui.resources.current_stat
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_left
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_right
import com.sgale.gaztelubira.multiplatform.ui.resources.need_to_set_percentage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun StatsSettingsDialog(
    settings: GBStatsSettings,
    selectedStat: GBStat,
    punctuation: GBPunctuation,
    actions: StatsActions
) {
    val atMenu = settings == Menu

    GBDialog(
        dismiss = { actions.onSettingsChanged(Hidden) },
        color = lightGray
    ) { modifier ->
        Column(
            modifier = modifier.height(500.dp).width(300.dp)
        ) {
            SettingsTitle(
                title = settings.title?.let { stringResource(it) }.orEmpty(),
                showBack = !atMenu,
                onBack = { actions.onSettingsChanged(Menu) }
            )
            HorizontalDivider(color = Black)
            Box(Modifier.weight(1f)) {
                SettingsMenu(
                    visible = atMenu,
                    selectedStat = selectedStat,
                    punctuation = punctuation,
                    actions = actions
                )
                SettingsSection(
                    settings = settings,
                    visible = !atMenu,
                    selectedStat = selectedStat,
                    actions = actions
                )
            }
        }
    }
}

@Composable
private fun SettingsTitle(
    title: String,
    showBack: Boolean,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(12.dp)
    ) {
        if (showBack) {
            GBIcon(
                modifier = Modifier.size(24.dp).clickable { onBack() }.padding(6.dp),
                icon = painterResource(Res.drawable.ic_arrow_left)
            )
        }
        GBText(
            modifier = Modifier.weight(1f),
            text = title,
            style = gBTypography().titleMedium,
            textColor = Black
        )
    }
}

@Composable
private fun SettingsMenu(
    visible: Boolean,
    selectedStat: GBStat,
    punctuation: GBPunctuation,
    actions: StatsActions
) {
    /**
     * Points only mean anything while the leaderboard is ranked by percentage — every other stat
     * is a plain count — so the entry explains itself instead of silently doing nothing.
     */
    val punctuationSubtitle =
        if (selectedStat == PERCENTAGE) null else stringResource(Res.string.need_to_set_percentage)
    val subtitleColor = if (selectedStat == PERCENTAGE) login_container_color else softRed

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally { fullWidth -> -minOf(fullWidth, 300) } + fadeIn(),
        exit = slideOutHorizontally { fullWidth -> -minOf(fullWidth, 300) } + fadeOut()
    ) {
        Column(Modifier.fillMaxWidth()) {
            SettingsButton(
                text = stringResource(Res.string.change_stat),
                subtitle = stringResource(Res.string.current_stat) + ": " +
                    stringResource(selectedStat.label),
                onButtonClicked = { actions.onSettingsChanged(ChangeStat) }
            )
            HorizontalDivider(thickness = 0.5.dp, color = Black)
            SettingsButton(
                text = stringResource(Res.string.change_punctuation),
                subtitle = punctuationSubtitle,
                subtitleColor = subtitleColor,
                enabled = selectedStat == PERCENTAGE,
                onButtonClicked = { actions.onSettingsChanged(ChangePunctuation(punctuation)) }
            )
            HorizontalDivider(thickness = 0.5.dp, color = Black)
        }
    }
}

@Composable
private fun SettingsButton(
    modifier: Modifier = Modifier,
    text: String,
    subtitle: String?,
    subtitleColor: Color = login_container_color,
    enabled: Boolean = true,
    onButtonClicked: () -> Unit
) {
    val clickableModifier = if (enabled) modifier.clickable { onButtonClicked() } else modifier

    Row(
        modifier = clickableModifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            GBText(
                text = text,
                style = gBTypography().bodyMedium,
                textColor = if (enabled) Black else elevated_button_bg_not_selected
            )
            subtitle?.let {
                GBText(
                    text = it,
                    style = gBTypography().bodySmall.copy(fontSize = 8.sp, fontStyle = Italic),
                    textColor = subtitleColor
                )
            }
        }
        GBIcon(
            modifier = Modifier.size(12.dp),
            icon = painterResource(Res.drawable.ic_arrow_right),
            tint = if (enabled) Black else elevated_button_bg_not_selected
        )
    }
}

@Composable
private fun SettingsSection(
    settings: GBStatsSettings,
    visible: Boolean,
    selectedStat: GBStat,
    actions: StatsActions
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally { it },
        exit = slideOutHorizontally { it }
    ) {
        when (settings) {
            Hidden, Menu -> Unit
            ChangeStat -> SettingsStats(selectedStat = selectedStat, actions = actions)
            is ChangePunctuation -> SettingsPunctuation(
                draft = settings.draft,
                actions = actions
            )
        }
    }
}

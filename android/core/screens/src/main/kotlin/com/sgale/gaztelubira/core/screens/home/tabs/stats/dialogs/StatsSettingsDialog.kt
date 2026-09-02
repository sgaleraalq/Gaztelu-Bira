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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.designsystem.components.GBDialog
import com.sgale.gaztelubira.core.designsystem.components.GBIcon
import com.sgale.gaztelubira.core.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg_not_selected
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.lightGray
import com.sgale.gaztelubira.multiplatform.designsystem.style.login_container_color
import com.sgale.gaztelubira.multiplatform.designsystem.style.softRed
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.domain.model.player.Stat.Percentage
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation
import com.sgale.gaztelubira.core.screens.home.tabs.stats.dialogs.SettingsDialogState.ChangePunctuation
import com.sgale.gaztelubira.core.screens.home.tabs.stats.dialogs.SettingsDialogState.ChangeStat
import com.sgale.gaztelubira.core.screens.home.tabs.stats.dialogs.SettingsDialogState.Default
import androidx.compose.ui.res.stringResource

@Composable
internal fun StatsSettingsDialog(
    selectedStat: Stat,
    punctuation: Punctuation,
    dismiss: () -> Unit,
    changeSelectedStat: (Stat) -> Unit,
    changePunctuation: (Punctuation) -> Unit,
    viewModel: StatsSettingsViewModel = hiltViewModel()
) {
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    GBDialog(
        dismiss = {
            viewModel.changeState(Default)
            dismiss()
        },
        color = lightGray
    ) { modifier ->
        Column(
            modifier = modifier
                .height(500.dp)
                .width(300.dp)
        ) {
            StatsSettingsTitle(
                title = stringResource(dialogState.title),
                showButtons = dialogState == Default,
                changeState = { viewModel.changeState(it) }
            )
            HorizontalDivider(color = Black)
            Box(
                modifier = Modifier.weight(1f)
            ) {
                SettingsIndicators(
                    selectedStat = selectedStat,
                    punctuation = punctuation,
                    showButtons = dialogState == Default,
                    changeState = { viewModel.changeState(it) }
                )
                SettingsValues(
                    state = dialogState,
                    showValues = dialogState != Default,
                    changeSelectedStat = { changeSelectedStat(it) },
                    changePunctuation = { changePunctuation(it) },
                    updatePunctuation = { viewModel.updatePunctuation(it) },
                    dismiss = {
                        viewModel.changeState(Default)
                        dismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun StatsSettingsTitle(
    title: String,
    showButtons: Boolean,
    changeState: (SettingsDialogState) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(12.dp)
    ) {
        if (!showButtons) {
            GBIcon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { changeState(Default) }
                    .padding(6.dp),
                icon = R.drawable.ic_arrow_left
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
private fun SettingsIndicators(
    selectedStat: Stat,
    punctuation: Punctuation,
    showButtons: Boolean,
    changeState: (SettingsDialogState) -> Unit
) {
    val punctuationString = when (selectedStat) {
        Percentage -> null // TODO?
        else -> stringResource(R.string.need_to_set_percentage)
    }

    val subtitleColor = when (selectedStat) {
        Percentage -> login_container_color
        else -> softRed
    }

    AnimatedVisibility(
        visible = showButtons,
        enter = slideInHorizontally { fullWidth -> -minOf(fullWidth, 300) } + fadeIn(),
        exit = slideOutHorizontally { fullWidth -> -minOf(fullWidth, 300) } + fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            SettingsButton(
                text = stringResource(R.string.change_stat),
                subtitle = stringResource(R.string.current_stat)
                        + ": " + stringResource(selectedStat.statName),
                onButtonClicked = { changeState(ChangeStat(selectedStat)) }
            )
            HorizontalDivider(thickness = 0.5.dp, color = Black)
            SettingsButton(
                text = stringResource(R.string.change_punctuation),
                subtitle = punctuationString,
                onButtonClicked = { changeState(ChangePunctuation(punctuation)) },
                subtitleColor = subtitleColor,
                enabled = selectedStat == Percentage,
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
    onButtonClicked: () -> Unit,
) {
    val clickableModifier = if (enabled) {
        modifier.clickable { onButtonClicked() }
    } else {
        modifier
    }

    Row(
        modifier = clickableModifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 16.dp
            ),
        verticalAlignment = CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            GBText(
                text = text,
                style = gBTypography().bodyMedium,
                textColor = if (enabled) Black else elevated_button_bg_not_selected
            )

            subtitle?.let {
                GBText(
                    text = subtitle,
                    style = gBTypography().bodySmall.copy(
                        fontSize = 8.sp,
                        fontStyle = Italic
                    ),
                    textColor = subtitleColor
                )
            }
        }

        GBIcon(
            modifier = Modifier.size(12.dp),
            icon = R.drawable.ic_arrow_right,
            tint = if (enabled) Black else elevated_button_bg_not_selected
        )
    }
}

@Composable
private fun SettingsValues(
    state: SettingsDialogState,
    showValues: Boolean,
    changeSelectedStat: (Stat) -> Unit,
    changePunctuation: (Punctuation) -> Unit,
    updatePunctuation: (Punctuation) -> Unit,
    dismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = showValues,
        enter = slideInHorizontally { it },
        exit = slideOutHorizontally { it }
    ) {
        when (state) {
            Default -> Unit
            is ChangePunctuation -> {
                SettingsPunctuation(
                    state = state,
                    changePunctuation = { changePunctuation(it) },
                    updatePunctuation = { updatePunctuation(it) },
                    dismiss = { dismiss() }
                )
            }

            is ChangeStat -> {
                SettingsStats(
                    state = state,
                    changeSelectedStat = { changeSelectedStat(it) },
                    dismiss = { dismiss() }
                )
            }
        }
    }
}

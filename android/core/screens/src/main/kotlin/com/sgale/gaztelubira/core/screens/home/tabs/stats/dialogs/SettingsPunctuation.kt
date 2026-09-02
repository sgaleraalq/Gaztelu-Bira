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

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderDefaults.Thumb
import androidx.compose.material3.SliderDefaults.Track
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.Assists
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.CleanSheets
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.Fails
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.Goals
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.MinRedCards
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.MinYellowCards
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.PenaltiesProvoked
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation.PunctuationField.Saves
import com.sgale.gaztelubira.core.screens.home.tabs.stats.dialogs.SettingsDialogState.ChangePunctuation
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.gray_box_in_black_bg
import com.sgale.gaztelubira.multiplatform.designsystem.style.player_card_name_text_color
import com.sgale.gaztelubira.multiplatform.designsystem.style.softRed
import kotlin.math.roundToInt

private val STANDARD_VALUES = listOf(0,1,2,3,4,5)
private val CARD_VALUES = listOf(1,3,5)
@Composable
fun SettingsPunctuation(
    state: SettingsDialogState,
    changePunctuation: (Punctuation) -> Unit,
    updatePunctuation: (Punctuation) -> Unit,
    dismiss: () -> Unit
) {
    state as ChangePunctuation

    Column {
        Sliders(
            modifier = Modifier.weight(1f),
            state = state,
            updatePunctuation = { updatePunctuation(it) }
        )
        UpdateSettingsPunctuationButton {
            changePunctuation(state.punctuation)
            dismiss()
        }
    }
}

@Composable
fun Sliders(
    modifier: Modifier,
    state: ChangePunctuation,
    updatePunctuation: (Punctuation) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(modifier.padding(12.dp)) {
        Column(
            modifier = modifier.verticalScroll(scrollState).padding(12.dp)
        ) {
            // Goals
            SliderBox(
                title = stringResource(R.string.goals),
                points = state.punctuation.goals,
                possibleValues = STANDARD_VALUES,
                onPositionChanged = {
                    val newPunctuation = state.punctuation.updateValue(it, Goals)
                    updatePunctuation(newPunctuation)
                }
            )

            // Assists
            SliderBox(
                title = stringResource(R.string.assists),
                points = state.punctuation.assists,
                possibleValues = STANDARD_VALUES,
                onPositionChanged = {
                    val newPunctuation = state.punctuation.updateValue(it, Assists)
                    updatePunctuation(newPunctuation)
                }
            )

            // Clean Sheets
            SliderBox(
                title = stringResource(R.string.clean_sheets),
                points = state.punctuation.cleanSheets,
                possibleValues = STANDARD_VALUES,
                onPositionChanged = {
                    val newPunctuation = state.punctuation.updateValue(it, CleanSheets)
                    updatePunctuation(newPunctuation)
                }
            )

            // Penalties
            SliderBox(
                title = stringResource(R.string.penalties),
                points = state.punctuation.penaltiesProvoked,
                possibleValues = STANDARD_VALUES,
                onPositionChanged = {
                    val newPunctuation = state.punctuation.updateValue(it, PenaltiesProvoked)
                    updatePunctuation(newPunctuation)
                }
            )

            // Saves
            SliderBox(
                title = stringResource(R.string.saves),
                points = state.punctuation.saves,
                possibleValues = STANDARD_VALUES,
                onPositionChanged = {
                    val newPunctuation = state.punctuation.updateValue(it, Saves)
                    updatePunctuation(newPunctuation)
                }
            )

            // Fails
            SliderBox(
                title = stringResource(R.string.fails),
                points = state.punctuation.fails,
                possibleValues = STANDARD_VALUES,
                isNegative = true,
                onPositionChanged = {
                    val newPunctuation = state.punctuation.updateValue(it, Fails)
                    updatePunctuation(newPunctuation)
                }
            )

            // Yellow Cards
            SliderBox(
                title = stringResource(R.string.yellow_cards),
                points = state.punctuation.minYellowCards,
                possibleValues = CARD_VALUES,
                isCard = true,
                onPositionChanged = {
                    val newPunctuation = state.punctuation.updateValue(it, MinYellowCards)
                    updatePunctuation(newPunctuation)
                }
            )

            // Red Cards
            SliderBox(
                title = stringResource(R.string.red_cards),
                points = state.punctuation.minRedCards,
                possibleValues = CARD_VALUES,
                isCard = true,
                onPositionChanged = {
                    val newPunctuation = state.punctuation.updateValue(it, MinRedCards)
                    updatePunctuation(newPunctuation)
                }
            )
            // TODO add scroll
//            VerticalScrollbar(
//                adapter = rememberScrollbarAdapter(scrollState)
//            )
        }
    }
}

@Composable
fun SliderBox(
    title: String,
    points: Int?,
    possibleValues: List<Int>,
    isNegative: Boolean = false,
    isCard: Boolean = false,
    onPositionChanged: (Int) -> Unit = {}
) {
    SliderTitle(title)
    SliderRow(
        points = points,
        possibleValues = possibleValues,
        isNegative = isNegative,
        isCard = isCard,
        onPositionChanged = { onPositionChanged(it) }
    )
}

@Composable
fun SliderTitle(
    title: String
) {
    GBText(
        modifier = Modifier.fillMaxWidth(),
        text = title,
        style = gBTypography().bodySmall,
        textColor = Black
    )
}

@Composable
fun SliderRow(
    points: Int?,
    possibleValues: List<Int>,
    isNegative: Boolean,
    isCard: Boolean,
    onPositionChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(end = 12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(12.dp)
    ) {
        PunctuationSlider(
            modifier = Modifier.weight(1f),
            points = points,
            possibleValues = possibleValues,
            isNegative = isNegative,
            onPositionChanged = { onPositionChanged(it) }
        )
        SliderPointsText(
            points = points,
            isCard = isCard
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PunctuationSlider(
    modifier: Modifier = Modifier,
    points: Int?,
    possibleValues: List<Int>,
    isNegative: Boolean,
    onPositionChanged: (Int) -> Unit
) {
    if (points == null || possibleValues.isEmpty()) return

    val currentValue = if (isNegative) -points else points
    val currentIndex = possibleValues.indexOf(currentValue).takeIf { it >= 0 } ?: 0
    val steps = (possibleValues.size - 2).coerceAtLeast(0)
    val valueRange = 0f..(possibleValues.size - 1).toFloat()

    val sliderColors = SliderDefaults.colors(
        thumbColor = softRed,
        activeTrackColor = softRed,
        activeTickColor = softRed,
        inactiveTrackColor = player_card_name_text_color,
        inactiveTickColor = gray_box_in_black_bg
    )

    Slider(
        modifier = modifier,
        value = currentIndex.toFloat(),
        onValueChange = { newIndex ->
            val index = newIndex.roundToInt()
            val selectedValue = possibleValues.getOrElse(index) { possibleValues.first() }
            val finalValue = if (isNegative) -selectedValue else selectedValue
            onPositionChanged(finalValue)
        },
        valueRange = valueRange,
        steps = steps,
        colors = sliderColors,
        thumb = {
            Thumb(
                interactionSource = remember { MutableInteractionSource() },
                thumbSize = DpSize(14.dp, 14.dp),
                colors = sliderColors
            )
        },
        track = { sliderState ->
            Track(
                modifier = Modifier.height(4.dp),
                sliderState = sliderState,
                colors = sliderColors,
                thumbTrackGapSize = 0.dp,
                drawStopIndicator = null
            )
        }
    )
}

@Composable
fun SliderPointsText(
    points: Int?,
    isCard: Boolean
) {
    val text = if (isCard) {
        "1/$points pts"
    } else {
        "$points pts"
    }

    GBText(
        text = text,
        textColor = Black,
        style = gBTypography().bodySmall
    )
}

@Composable
fun UpdateSettingsPunctuationButton(
    onClick: () -> Unit
) {
    GBElevatedButton(
        modifier = Modifier.padding(horizontal = 12.dp).padding(bottom = 8.dp).fillMaxWidth(),
        text = stringResource(R.string.update_punctuation),
        onClick = { onClick() }
    )
}

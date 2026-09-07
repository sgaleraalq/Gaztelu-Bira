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
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.gray_box_in_black_bg
import com.sgale.gaztelubira.multiplatform.designsystem.style.player_card_name_text_color
import com.sgale.gaztelubira.multiplatform.designsystem.style.softRed
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.ASSISTS
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.CLEAN_SHEETS
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.FAILS
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.GOALS
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.MIN_RED_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.MIN_YELLOW_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.PENALTIES_PROVOKED
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation.PunctuationField.SAVES
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.StatsActions
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsSettings.Hidden
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.assists
import com.sgale.gaztelubira.multiplatform.ui.resources.clean_sheets
import com.sgale.gaztelubira.multiplatform.ui.resources.fails
import com.sgale.gaztelubira.multiplatform.ui.resources.goals
import com.sgale.gaztelubira.multiplatform.ui.resources.penalties
import com.sgale.gaztelubira.multiplatform.ui.resources.red_cards
import com.sgale.gaztelubira.multiplatform.ui.resources.saves
import com.sgale.gaztelubira.multiplatform.ui.resources.update_punctuation
import com.sgale.gaztelubira.multiplatform.ui.resources.yellow_cards
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

private val STANDARD_VALUES = listOf(0, 1, 2, 3, 4, 5)
private val CARD_VALUES = listOf(1, 3, 5)

private data class SliderSpec(
    val label: StringResource,
    val value: Int,
    val field: PunctuationField,
    val possibleValues: List<Int> = STANDARD_VALUES,
    val isNegative: Boolean = false,
    val isCard: Boolean = false
)

@Composable
internal fun SettingsPunctuation(
    draft: GBPunctuation,
    actions: StatsActions
) {
    Column {
        Sliders(
            modifier = Modifier.weight(1f),
            draft = draft,
            onFieldChanged = { value, field ->
                actions.onPunctuationDraftChanged(draft.updateValue(value, field))
            }
        )
        GBElevatedButton(
            modifier = Modifier.padding(horizontal = 12.dp).padding(bottom = 8.dp).fillMaxWidth(),
            text = stringResource(Res.string.update_punctuation),
            onClick = {
                actions.onPunctuationConfirmed(draft)
                actions.onSettingsChanged(Hidden)
            }
        )
    }
}

@Composable
private fun Sliders(
    modifier: Modifier,
    draft: GBPunctuation,
    onFieldChanged: (Int, PunctuationField) -> Unit
) {
    val scrollState = rememberScrollState()
    val specs = listOf(
        SliderSpec(
            label = Res.string.goals,
            value = draft.goals,
            field = GOALS
        ),
        SliderSpec(
            label = Res.string.assists,
            value = draft.assists,
            field = ASSISTS
        ),
        SliderSpec(
            label = Res.string.clean_sheets,
            value = draft.cleanSheets,
            field = CLEAN_SHEETS
        ),
        SliderSpec(
            label = Res.string.penalties,
            value = draft.penaltiesProvoked,
            field = PENALTIES_PROVOKED
        ),
        SliderSpec(
            label = Res.string.saves,
            value = draft.saves,
            field = SAVES
        ),
        SliderSpec(
            label = Res.string.fails,
            value = draft.fails,
            field = FAILS,
            isNegative = true
        ),
        SliderSpec(
            label = Res.string.yellow_cards,
            value = draft.minYellowCards,
            field = MIN_YELLOW_CARDS,
            possibleValues = CARD_VALUES,
            isCard = true
        ),
        SliderSpec(
            label = Res.string.red_cards,
            value = draft.minRedCards,
            field = MIN_RED_CARDS,
            possibleValues = CARD_VALUES,
            isCard = true
        )
    )

    Row(
        modifier = modifier.padding(12.dp)
    ) {
        Column(
            modifier = modifier.verticalScroll(scrollState).padding(12.dp)
        ) {
            specs.forEach { spec ->
                GBText(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(spec.label),
                    style = gBTypography().bodySmall,
                    textColor = Black
                )
                SliderRow(
                    spec = spec,
                    onPositionChanged = { onFieldChanged(it, spec.field) }
                )
            }
        }
    }
}

@Composable
private fun SliderRow(
    spec: SliderSpec,
    onPositionChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(end = 12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(12.dp)
    ) {
        PunctuationSlider(
            modifier = Modifier.weight(1f),
            points = spec.value,
            possibleValues = spec.possibleValues,
            isNegative = spec.isNegative,
            onPositionChanged = onPositionChanged
        )
        GBText(
            text = if (spec.isCard) "1/${spec.value} pts" else "${spec.value} pts",
            textColor = Black,
            style = gBTypography().bodySmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PunctuationSlider(
    modifier: Modifier = Modifier,
    points: Int,
    possibleValues: List<Int>,
    isNegative: Boolean,
    onPositionChanged: (Int) -> Unit
) {
    if (possibleValues.isEmpty()) return

    val currentValue = if (isNegative) -points else points
    val currentIndex = possibleValues.indexOf(currentValue).takeIf { it >= 0 } ?: 0
    val steps = (possibleValues.size - 2).coerceAtLeast(0)

    val sliderColors = SliderDefaults.colors(
        thumbColor = softRed,
        activeTrackColor = softRed,
        activeTickColor = softRed,
        inactiveTrackColor = player_card_name_text_color,
        inactiveTickColor = gray_box_in_black_bg
    )

    val onValueChanged: (Float) -> Unit = { newIndex ->
        val selected = possibleValues.getOrElse(newIndex.roundToInt()) { possibleValues.first() }
        onPositionChanged(if (isNegative) -selected else selected)
    }

    val thumb = @Composable {
        Thumb(
            interactionSource = remember { MutableInteractionSource() },
            thumbSize = DpSize(14.dp, 14.dp),
            colors = sliderColors
        )
    }

    val track: @Composable (SliderState) -> Unit = { sliderState ->
        Track(
            modifier = Modifier.height(4.dp),
            sliderState = sliderState,
            colors = sliderColors,
            thumbTrackGapSize = 0.dp,
            drawStopIndicator = null
        )
    }

    Slider(
        modifier = modifier,
        value = currentIndex.toFloat(),
        onValueChange = { onValueChanged(it) },
        valueRange = 0f..(possibleValues.size - 1).toFloat(),
        steps = steps,
        colors = sliderColors,
        thumb = { thumb() },
        track = { track(it) }
    )
}

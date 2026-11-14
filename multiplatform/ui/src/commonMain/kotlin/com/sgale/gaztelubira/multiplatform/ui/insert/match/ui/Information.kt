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

package com.sgale.gaztelubira.multiplatform.ui.insert.match.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isNotLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchActions
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Description
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Location
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.INFORMATION
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_date
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_info
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_location
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_date
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_information_subtitle
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_information_title
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_location
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_notes
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_notes_hint
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_time
import org.jetbrains.compose.resources.stringResource

private const val MILLIS_PER_DAY = 86_400_000L
private const val DATE_PLACEHOLDER = "--/--/----"

private val informationColor = Color(0xFF4C8DF6)

@Composable
internal fun MatchInformation(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    val enabled = state.state.isNotLoading()
    MatchCard {
        MatchSectionHeader(
            icon = Res.drawable.ic_info,
            iconColor = informationColor,
            title = stringResource(Res.string.insert_match_information_title),
            subtitle = stringResource(Res.string.insert_match_information_subtitle),
            expanded = INFORMATION in state.expanded,
            onClick = { actions.toggleSection(INFORMATION) }
        )
        AnimatedVisibility(visible = INFORMATION in state.expanded) {
            Column {
                HorizontalDivider(color = matchCardBorder)
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = spacedBy(8.dp)
                ) {
                    Row(horizontalArrangement = spacedBy(8.dp)) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = spacedBy(8.dp)
                        ) {
                            MatchLabel(stringResource(Res.string.insert_match_date))
                            MatchPickerField(
                                modifier = Modifier.fillMaxWidth(),
                                text = state.date?.let(::formatDate) ?: DATE_PLACEHOLDER,
                                icon = Res.drawable.ic_date,
                                enabled = enabled,
                                onClick = { actions.updateDialogState(InsertMatchDialog.Date) }
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = spacedBy(8.dp)
                        ) {
                            MatchLabel(stringResource(Res.string.insert_match_time))
                            MatchPickerField(
                                modifier = Modifier.fillMaxWidth(),
                                text = formatTime(state.hour, state.minute),
                                icon = Res.drawable.ic_date,
                                enabled = enabled,
                                onClick = { actions.updateDialogState(InsertMatchDialog.Time) }
                            )
                        }
                    }

                    MatchLabel(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(Res.string.insert_match_location)
                    )
                    MatchTextField(
                        value = state.location,
                        onValueChanged = { actions.updateField(Location(it)) },
                        enabled = enabled,
                        icon = Res.drawable.ic_location
                    )

                    MatchLabel(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(Res.string.insert_match_notes)
                    )
                    MatchTextField(
                        value = state.description,
                        onValueChanged = { actions.updateField(Description(it)) },
                        hint = stringResource(Res.string.insert_match_notes_hint),
                        enabled = enabled,
                        singleLine = false,
                        minLines = 3
                    )
                }
            }
        }
    }
}

internal fun formatTime(hour: Int, minute: Int): String =
    "${hour.twoDigits()}:${minute.twoDigits()}"

/**
 * "dd/MM/yyyy" for a day given as milliseconds since 1970, read in UTC — which is how the date
 * picker hands days over, so no time zone can move the date the user chose to the day before.
 *
 * The shared module has no date library, hence the arithmetic: the usual days-to-civil conversion
 * over 400-year eras, counting years from March so the leap day falls at the end.
 */
internal fun formatDate(millis: Long): String {
    val shifted = millis.floorDiv(MILLIS_PER_DAY) + 719_468
    val era = shifted.floorDiv(146_097)
    val dayOfEra = shifted - era * 146_097
    val yearOfEra = (dayOfEra - dayOfEra / 1_460 + dayOfEra / 36_524 - dayOfEra / 146_096) / 365
    val dayOfYear = dayOfEra - (365 * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
    val shiftedMonth = (5 * dayOfYear + 2) / 153
    val day = dayOfYear - (153 * shiftedMonth + 2) / 5 + 1
    val month = if (shiftedMonth < 10) shiftedMonth + 3 else shiftedMonth - 9
    val year = yearOfEra + era * 400 + if (month <= 2) 1 else 0
    return "${day.toInt().twoDigits()}/${month.toInt().twoDigits()}/$year"
}

private fun Int.twoDigits(): String = toString().padStart(2, '0')

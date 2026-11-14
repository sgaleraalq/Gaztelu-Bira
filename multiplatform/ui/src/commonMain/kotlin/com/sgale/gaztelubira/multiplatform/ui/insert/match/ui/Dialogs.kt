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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.End
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTeam
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.designsystem.style.primaryRed
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBTeam
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchActions
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.match.candidates
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog.None
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchPlayerTarget.Manager
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_cancel
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_confirm
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_no_players
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_no_teams
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_select_a_player
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_select_a_team
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_time
import org.jetbrains.compose.resources.stringResource

private val LIST_MAX_HEIGHT = 420.dp

/**
 * Whichever dialog the state says is open. They are all drawn from here so the form itself never
 * has to know one is showing.
 */
@Composable
internal fun MatchDialogs(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    val dismiss = { actions.updateDialogState(None) }
    when (val dialog = state.dialog) {
        None -> Unit
        InsertMatchDialog.Rival -> RivalDialog(
            teams = state.rivals,
            onTeamSelected = { actions.updateField(InsertMatchField.Rival(it)) },
            dismiss = dismiss
        )
        InsertMatchDialog.Date -> DateDialog(
            date = state.date,
            onDateSelected = { actions.updateField(InsertMatchField.Date(it)) },
            dismiss = dismiss
        )
        InsertMatchDialog.Time -> TimeDialog(
            hour = state.hour,
            minute = state.minute,
            onTimeSelected = { hour, minute -> actions.updateField(InsertMatchField.Time(hour, minute)) },
            dismiss = dismiss
        )
        is InsertMatchDialog.Player -> PlayerDialog(
            players = state.candidates(dialog.target),
            isStaff = dialog.target == Manager,
            onPlayerSelected = actions.pickPlayer,
            dismiss = dismiss
        )
    }
}

@Composable
private fun RivalDialog(
    teams: List<GBTeam>,
    onTeamSelected: (GBTeam) -> Unit,
    dismiss: () -> Unit
) {
    ListDialog(
        title = stringResource(Res.string.insert_match_select_a_team),
        emptyMessage = stringResource(Res.string.insert_match_no_teams),
        items = teams,
        key = { it.id },
        dismiss = dismiss
    ) { team ->
        ListRow(
            name = team.name,
            onClick = { onTeamSelected(team) }
        ) {
            GBTeam(
                modifier = Modifier.size(36.dp),
                image = team.logo,
                placeholder = AppImages.teamCrest
            )
        }
    }
}

@Composable
private fun PlayerDialog(
    players: List<GBPlayer>,
    isStaff: Boolean,
    onPlayerSelected: (GBPlayer) -> Unit,
    dismiss: () -> Unit
) {
    ListDialog(
        title = stringResource(Res.string.insert_match_select_a_player),
        emptyMessage = stringResource(Res.string.insert_match_no_players),
        items = players,
        key = { it.id },
        dismiss = dismiss
    ) { player ->
        ListRow(
            name = player.dorsal?.let { "$it · ${player.name}" } ?: player.name,
            onClick = { onPlayerSelected(player) }
        ) {
            GBImage(
                modifier = Modifier.size(36.dp).clip(CircleShape),
                image = player.image,
                placeholder = if (isStaff) AppImages.manager else AppImages.facePlayer
            )
        }
    }
}

@Composable
private fun <T> ListDialog(
    title: String,
    emptyMessage: String,
    items: List<T>,
    key: (T) -> Any,
    dismiss: () -> Unit,
    row: @Composable (T) -> Unit
) {
    GBDialog(
        color = matchCardColor,
        dismiss = dismiss
    ) { modifier ->
        Column(
            modifier = modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalArrangement = spacedBy(8.dp)
        ) {
            GBText(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = title,
                style = GBTheme.typography.titleMedium.copy(fontWeight = Bold)
            )
            if (items.isEmpty()) {
                GBText(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    text = emptyMessage,
                    textColor = matchMutedText,
                    style = GBTheme.typography.bodyMedium,
                    maxLines = 2
                )
            } else {
                LazyColumn(modifier = Modifier.heightIn(max = LIST_MAX_HEIGHT)) {
                    items(items, key = key) { item -> row(item) }
                }
            }
        }
    }
}

@Composable
private fun ListRow(
    name: String,
    onClick: () -> Unit,
    image: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(14.dp)
    ) {
        image()
        GBText(
            modifier = Modifier.weight(1f),
            text = name,
            style = GBTheme.typography.bodyLarge,
            overflow = Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateDialog(
    date: Long?,
    onDateSelected: (Long) -> Unit,
    dismiss: () -> Unit
) {
    val pickerState = rememberDatePickerState(initialSelectedDateMillis = date)
    DatePickerDialog(
        onDismissRequest = dismiss,
        confirmButton = {
            DialogButton(
                text = stringResource(Res.string.insert_match_confirm),
                onClick = { pickerState.selectedDateMillis?.let(onDateSelected) ?: dismiss() }
            )
        },
        dismissButton = {
            DialogButton(
                text = stringResource(Res.string.insert_match_cancel),
                onClick = dismiss
            )
        }
    ) {
        DatePicker(state = pickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeDialog(
    hour: Int,
    minute: Int,
    onTimeSelected: (Int, Int) -> Unit,
    dismiss: () -> Unit
) {
    val pickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute,
        is24Hour = true
    )
    GBDialog(
        color = matchCardColor,
        dismiss = dismiss
    ) { modifier ->
        Column(
            modifier = modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = spacedBy(16.dp)
        ) {
            GBText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.insert_match_time),
                style = GBTheme.typography.titleMedium.copy(fontWeight = Bold)
            )
            TimeInput(state = pickerState)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = End
            ) {
                DialogButton(
                    text = stringResource(Res.string.insert_match_cancel),
                    onClick = dismiss
                )
                DialogButton(
                    text = stringResource(Res.string.insert_match_confirm),
                    onClick = { onTimeSelected(pickerState.hour, pickerState.minute) }
                )
            }
        }
    }
}

@Composable
private fun DialogButton(
    text: String,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        GBText(
            text = text,
            textColor = primaryRed,
            style = GBTheme.typography.labelLarge.copy(fontWeight = Bold)
        )
    }
}

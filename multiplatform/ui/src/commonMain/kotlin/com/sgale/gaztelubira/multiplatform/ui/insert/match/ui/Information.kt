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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBBasicTextField
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.CUP
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchActions
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.CupName
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.CupRound
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Description
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Location
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_cup_name
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_cup_round
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_description
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_location
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private val FIELD_BACKGROUND = Color(0xFF2A2A2A)

@Composable
internal fun InsertMatchInformation(
    modifier: Modifier,
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(top = 16.dp),
        verticalArrangement = spacedBy(12.dp)
    ) {
        GBText(
            modifier = Modifier.fillMaxWidth(),
            text = state.date,
            style = GBTheme.typography.bodySmall.copy(fontSize = 10.sp)
        )
        SingleLineField(
            title = Res.string.insert_match_location,
            value = state.location,
            onValueChanged = { actions.updateField(Location(it)) }
        )
        if (state.type == CUP) {
            SingleLineField(
                title = Res.string.insert_match_cup_name,
                value = state.cupName,
                onValueChanged = { actions.updateField(CupName(it)) }
            )
            SingleLineField(
                title = Res.string.insert_match_cup_round,
                value = state.cupRound,
                onValueChanged = { actions.updateField(CupRound(it)) }
            )
        }
        FieldTitle(Res.string.insert_match_description)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(color = FIELD_BACKGROUND, shape = RoundedCornerShape(4.dp))
        ) {
            GBBasicTextField(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                value = state.description,
                onValueChanged = { actions.updateField(Description(it)) },
                style = GBTheme.typography.bodyMedium.copy(color = White),
                showNextLine = true
            )
        }
    }
}

@Composable
private fun SingleLineField(
    title: StringResource,
    value: String,
    onValueChanged: (String) -> Unit
) {
    FieldTitle(title)
    Box(
        modifier = Modifier
            .height(36.dp)
            .fillMaxWidth()
            .background(color = FIELD_BACKGROUND, shape = RoundedCornerShape(4.dp)),
        contentAlignment = Center
    ) {
        GBBasicTextField(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            value = value,
            onValueChanged = onValueChanged,
            style = GBTheme.typography.bodyMedium.copy(color = White),
            singleLine = true
        )
    }
}

@Composable
private fun FieldTitle(title: StringResource) {
    GBText(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(title),
        style = GBTheme.typography.titleMedium
    )
}

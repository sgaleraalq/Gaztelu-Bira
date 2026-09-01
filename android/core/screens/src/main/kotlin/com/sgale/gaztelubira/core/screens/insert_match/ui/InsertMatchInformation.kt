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

package com.sgale.gaztelubira.core.screens.insert_match.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
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
import com.sgale.gaztelubira.core.designsystem.components.GBBasicTextField
import com.sgale.gaztelubira.core.designsystem.components.GBText
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.domain.utils.getActualTimeAsLong
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchInformation
import androidx.compose.ui.res.stringResource

@Composable
internal fun InsertMatchInformation(
    modifier: Modifier,
    insertMatchInformation: InsertMatchInformation,
    updateDescription: (String) -> Unit,
    updateLocation: (String) -> Unit
) {
    Spacer(Modifier.height(16.dp))
    InsertMatchDate(Modifier.fillMaxWidth(), getActualTimeAsLong())
    InsertMatchLocationTitle()
    InsertMatchLocation(
        insertMatchInformation = insertMatchInformation,
        updateLocation = { updateLocation(it) }
    )
    InsertMatchDescriptionTitle()
    InsertMatchDescription(
        modifier = modifier,
        insertMatchInformation = insertMatchInformation,
        updateDescription = { updateDescription(it) }
    )
}

@Composable
internal fun InsertMatchLocationTitle() {
    GBText(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.location),
        style = gBTypography().titleMedium
    )
}

@Composable
internal fun InsertMatchDescriptionTitle() {
    GBText(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.description),
        style = gBTypography().titleLarge
    )
}

@Composable
internal fun InsertMatchLocation(
    insertMatchInformation: InsertMatchInformation,
    updateLocation: (String) -> Unit
) {
    val textStyle = gBTypography().bodyMedium

    Box(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .height(36.dp)
            .fillMaxWidth()
            .background(
                color = Color(0xFF2A2A2A),
                shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Center
    ) {
        GBBasicTextField(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            value = insertMatchInformation.location,
            onValueChanged = { updateLocation(it) },
            style = textStyle.copy(color = White),
            singleLine = true
        )
    }
}

@Composable
internal fun InsertMatchDescription(
    modifier: Modifier,
    insertMatchInformation: InsertMatchInformation,
    updateDescription: (String) -> Unit
) {
    Box(
        modifier = modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .background(
                color = Color(0xFF2A2A2A),
                shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Center
    ) {
        GBBasicTextField(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            value = insertMatchInformation.description,
            onValueChanged = {
                updateDescription(it)
            },
            style = gBTypography().bodyMedium.copy(
                color = White
            ),
            showNextLine = true
        )
    }
}

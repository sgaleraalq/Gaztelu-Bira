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

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBInsertButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_text_color
import com.sgale.gaztelubira.multiplatform.designsystem.style.gb_dialog_background
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isNotLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchActions
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.Formation
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.Information
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.Main
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.Stats
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.common_stats
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_right
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_formation
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_information
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_stats
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_information
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_formation
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val SECTIONS = listOf(Information, Formation, Stats)

@Composable
internal fun InsertMatchSections(
    modifier: Modifier,
    onSectionSelected: (InsertMatchSection) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(12.dp)
    ) {
        SECTIONS.forEach { section ->
            InsertMatchSectionCard(
                section = section,
                onClick = { onSectionSelected(section) }
            )
        }
    }
}

@Composable
internal fun InsertMatchSectionCard(
    section: InsertMatchSection,
    onClick: () -> Unit
) {
    val (icon, title) = section.content() ?: return

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(12.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = elevated_button_text_color,
            contentColor = elevated_button_bg
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = spacedBy(24.dp)
        ) {
            GBIcon(
                icon = painterResource(icon),
                tint = Black
            )
            GBText(
                modifier = Modifier.weight(1f),
                text = stringResource(title),
                textColor = gb_dialog_background
            )
            GBIcon(
                icon = painterResource(Res.drawable.ic_arrow_right)
            )
        }
    }
}

@Composable
internal fun InsertMatchButton(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    val invalidMessage = state.handler.invalidReason?.let { stringResource(it.reason) }

    GBInsertButton(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(Res.string.insert_match_title),
        loading = state.state.isLoading(),
        enabled = state.state.isNotLoading(),
        onInsert = {
            if (invalidMessage != null) {
                actions.onMissingField(invalidMessage)
            } else {
                actions.insertMatch()
            }
        }
    )
}

private fun InsertMatchSection.content(): Pair<DrawableResource, StringResource>? =
    when (this) {
        Main -> null
        Information -> Res.drawable.ic_information to Res.string.insert_information
        Formation -> Res.drawable.ic_formation to Res.string.insert_match_formation
        Stats -> Res.drawable.ic_stats to Res.string.common_stats
    }

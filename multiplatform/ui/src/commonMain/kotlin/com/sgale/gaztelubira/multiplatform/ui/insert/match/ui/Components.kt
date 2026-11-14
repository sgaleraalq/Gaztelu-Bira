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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Sentences
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.designsystem.style.primaryRed
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_down
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_up
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_button_add
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_close_fill
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * The palette of the form. It is darker and flatter than the rest of the app on purpose: the form
 * is long, and its cards have to read as blocks against the background.
 */
internal val matchBackground = Color(0xFF0B0D12)
internal val matchCardColor = Color(0xFF12151C)
internal val matchCardBorder = Color(0xFF262B36)
internal val matchFieldColor = Color(0xFF1A1E27)
internal val matchMutedText = Color(0xFF8A93A6)

internal val matchCardShape = RoundedCornerShape(16.dp)
internal val matchFieldShape = RoundedCornerShape(10.dp)

@Composable
internal fun MatchCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(matchCardShape)
            .background(matchCardColor)
            .border(width = 1.dp, color = matchCardBorder, shape = matchCardShape),
        content = content
    )
}

/**
 * The row a foldable block is opened and closed from.
 */
@Composable
internal fun MatchSectionHeader(
    icon: DrawableResource,
    iconColor: Color,
    title: String,
    subtitle: String,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(matchFieldShape)
                .background(iconColor.copy(alpha = 0.15f))
                .border(width = 1.dp, color = iconColor.copy(alpha = 0.4f), shape = matchFieldShape),
            contentAlignment = Center
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(icon),
                contentDescription = null,
                tint = iconColor
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            GBText(
                text = title,
                style = GBTheme.typography.titleMedium.copy(fontWeight = Bold)
            )
            GBText(
                text = subtitle,
                textColor = matchMutedText,
                style = GBTheme.typography.bodySmall
            )
        }
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(if (expanded) Res.drawable.ic_arrow_up else Res.drawable.ic_arrow_down),
            contentDescription = null,
            tint = matchMutedText
        )
    }
}

/**
 * One option of a group where only one is chosen: the competition, the side, the formation.
 */
@Composable
internal fun MatchChip(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(matchFieldShape)
            .background(if (selected) primaryRed else matchFieldColor)
            .border(
                width = 1.dp,
                color = if (selected) primaryRed else matchCardBorder,
                shape = matchFieldShape
            )
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Center
    ) {
        GBText(
            text = text,
            textColor = if (selected) White else matchMutedText,
            style = GBTheme.typography.labelLarge.copy(fontWeight = Bold),
            overflow = Ellipsis
        )
    }
}

@Composable
internal fun MatchLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    GBText(
        modifier = modifier,
        text = text,
        textColor = matchMutedText,
        style = GBTheme.typography.labelMedium
    )
}

@Composable
internal fun MatchTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    hint: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = true,
    minLines: Int = 1,
    icon: DrawableResource? = null
) {
    val style = GBTheme.typography.bodyMedium.copy(color = White)
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .clip(matchFieldShape)
            .background(matchFieldColor)
            .border(width = 1.dp, color = matchCardBorder, shape = matchFieldShape)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        value = value,
        onValueChange = onValueChanged,
        enabled = enabled,
        textStyle = style,
        singleLine = singleLine,
        minLines = minLines,
        cursorBrush = SolidColor(primaryRed),
        keyboardOptions = KeyboardOptions(capitalization = Sentences),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = if (singleLine) CenterVertically else Top,
                horizontalArrangement = spacedBy(10.dp)
            ) {
                if (icon != null) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = matchMutedText
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        GBText(
                            text = hint,
                            textColor = matchMutedText.copy(alpha = 0.6f),
                            style = GBTheme.typography.bodyMedium,
                            maxLines = minLines,
                            overflow = Ellipsis
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

/**
 * Looks like a text field but opens a picker: the date and the time.
 */
@Composable
internal fun MatchPickerField(
    modifier: Modifier = Modifier,
    text: String,
    icon: DrawableResource,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(matchFieldShape)
            .background(matchFieldColor)
            .border(width = 1.dp, color = matchCardBorder, shape = matchFieldShape)
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(10.dp)
    ) {
        GBText(
            modifier = Modifier.weight(1f),
            text = text,
            style = GBTheme.typography.bodyMedium
        )
        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = matchMutedText
        )
    }
}

/**
 * The square "+" that opens a picker to add someone.
 */
@Composable
internal fun MatchAddButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(matchFieldShape)
            .background(matchFieldColor)
            .border(width = 1.dp, color = matchCardBorder, shape = matchFieldShape)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Center
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(Res.drawable.ic_button_add),
            contentDescription = null,
            tint = White
        )
    }
}

/**
 * A player already placed somewhere, with the cross that takes them out again.
 */
@Composable
internal fun MatchPlayerChip(
    player: GBPlayer,
    enabled: Boolean,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(matchFieldColor)
            .border(width = 1.dp, color = matchCardBorder, shape = CircleShape)
            .clickable(enabled = enabled) { onRemove() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(6.dp)
    ) {
        GBText(
            text = player.name,
            style = GBTheme.typography.labelMedium,
            alignment = TextAlign.Center
        )
        Icon(
            modifier = Modifier.size(12.dp),
            painter = painterResource(Res.drawable.ic_close_fill),
            contentDescription = null,
            tint = matchMutedText
        )
    }
}

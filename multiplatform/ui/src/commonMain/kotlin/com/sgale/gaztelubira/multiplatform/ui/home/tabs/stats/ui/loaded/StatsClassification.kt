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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.loaded

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBAsyncImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg_not_selected
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.model.GBPlayerStat
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import org.jetbrains.compose.resources.stringResource

private val PLAYER_CLASSIFICATION_SIZE = 36.dp

@Composable
internal fun StatsClassification(
    modifier: Modifier,
    players: List<GBPlayerStat>,
    selectPlayer: (String) -> Unit,
    onImgLoadingError: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(
            items = players.drop(3).filter { it.dorsal != null },
            key = { _, player -> player.id }
        ) { index, player ->
            ClassificationCard(
                player = player,
                position = index + 4,
                onImgLoadingError = onImgLoadingError,
                onClick = { selectPlayer(player.id) }
            )
        }

        itemsIndexed(
            items = players.filter { it.dorsal == null },
            key = { _, player -> player.id }
        ) { _, player ->
            UnrankedPlayer(player, onImgLoadingError)
        }
    }
}

@Composable
private fun ClassificationCard(
    player: GBPlayerStat,
    position: Int,
    onClick: () -> Unit,
    onImgLoadingError: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 12.dp)
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(16.dp)
    ) {
        ClassificationPosition(
            position = position
        )
        ClassificationPlayerImage(
            image = player.faceImage,
            onImgLoadingError = onImgLoadingError
        )
        ClassificationName(
            modifier = Modifier.weight(1f),
            player = player
        )
        PositionArrow(
            classificationChanged = player.changedPosition,
            arrowFirst = false
        )
        GBText(
            modifier = Modifier.width(32.dp),
            text = player.value,
            alignment = TextAlign.Center
        )
    }
}

@Composable
private fun ClassificationPosition(
    position: Int?
) {
    GBText(
        modifier = Modifier.width(24.dp),
        text = position?.toString() ?: "",
        style = gBTypography().bodyMedium,
        alignment = TextAlign.End
    )
}

@Composable
private fun ClassificationPlayerImage(
    image: String?,
    onImgLoadingError: (String) -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    GBAsyncImage(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(White)
            .size(PLAYER_CLASSIFICATION_SIZE),
        image = image,
        contentScale = Fit,
        placeholder = AppImages.facePlayer,
        isLoading = isLoading,
        finishLoading = { isLoading = false },
        onError = { onImgLoadingError(it) }
    )
}

@Composable
private fun ClassificationName(
    modifier: Modifier,
    player: GBPlayerStat
) {
    val dorsal = player.dorsal?.let { "$it. " }.orEmpty()

    Column(modifier) {
        GBText(
            text = dorsal + player.name,
            style = gBTypography().bodySmall
        )
        player.position?.let { position ->
            GBText(
                text = stringResource(position.label),
                textColor = elevated_button_bg_not_selected,
                style = gBTypography().bodySmall.copy(
                    fontSize = 10.sp,
                    fontStyle = Italic
                )
            )
        }
    }
}

@Composable
private fun UnrankedPlayer(
    player: GBPlayerStat,
    onImgLoadingError: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Gray.copy(alpha = 0.5f), shape = RoundedCornerShape(4.dp))
            .padding(vertical = 6.dp)
            .padding(end = 12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(16.dp)
    ) {
        ClassificationPosition(null)
        ClassificationPlayerImage(player.faceImage, onImgLoadingError)
        ClassificationName(Modifier.weight(1f), player)
    }
}

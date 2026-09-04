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

package com.sgale.gaztelubira.core.screens.player_detail.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBPlayerImage
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun PlayerDetailImage(
    bodyImage: String?,
    placeholder: Painter
) {
    GBImage(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(2f / 3f),
        imageModifier = Modifier.fillMaxSize(),
        image = bodyImage,
        contentScale = Fit,
        placeholder = placeholder
    )
}

@Composable
fun TeamImage(
    modifier: Modifier,
    logo: String?,
    logoSize: Int
) {
    GBPlayerImage(
        modifier = modifier.size(logoSize.dp).offset(y = (-(logoSize / 2)).dp),
        image = logo
    )
}

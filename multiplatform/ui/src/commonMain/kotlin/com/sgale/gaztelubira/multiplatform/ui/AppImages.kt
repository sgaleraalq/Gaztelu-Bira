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

package com.sgale.gaztelubira.multiplatform.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.img_body_player
import com.sgale.gaztelubira.multiplatform.ui.resources.img_face_player
import com.sgale.gaztelubira.multiplatform.ui.resources.img_football_field
import com.sgale.gaztelubira.multiplatform.ui.resources.img_gaztelu_bira
import com.sgale.gaztelubira.multiplatform.ui.resources.img_manager
import com.sgale.gaztelubira.multiplatform.ui.resources.img_no_football_logo
import com.sgale.gaztelubira.multiplatform.ui.resources.img_placeholder
import org.jetbrains.compose.resources.painterResource

/**
 * The app's own placeholder artwork, resolved into `Painter`s for the design system.
 *
 * These live here rather than in `designsystem/` on purpose: which image stands in for a missing
 * player or a missing crest is a decision about this app's content, not about the components.
 */
object AppImages {
    val appLogo: Painter
        @Composable get() = painterResource(Res.drawable.img_gaztelu_bira)

    val facePlayer: Painter
        @Composable get() = painterResource(Res.drawable.img_face_player)

    val bodyPlayer: Painter
        @Composable get() = painterResource(Res.drawable.img_body_player)

    val manager: Painter
        @Composable get() = painterResource(Res.drawable.img_manager)

    val footballField: Painter
        @Composable get() = painterResource(Res.drawable.img_football_field)

    val teamCrest: Painter
        @Composable get() = painterResource(Res.drawable.img_no_football_logo)

    val undefined: Painter
        @Composable get() = painterResource(Res.drawable.img_placeholder)
}

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

package com.sgale.gaztelubira.core.domain.model.match

import androidx.compose.ui.graphics.Color

enum class MatchResult(
    val transColor: Color,
    val solidColor: Color
) {
    Victory(
        transColor = Color(0x3315FF99),
        solidColor = Color(0xFF00C853)
    ),
    Draw(
        transColor = Color(0x33FFD60A),
        solidColor = Color(0xFFFFD60A)
    ),
    Defeat(
        transColor = Color(0x33FF3B30),
        solidColor = Color(0xFFFF3B30)
    ),
    Undefined(
        transColor = Color(0xFF333333),
        solidColor = Color(0xFF999999)
    )
}

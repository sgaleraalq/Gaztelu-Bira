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

package com.sgale.gaztelubira.core.data.db.entities

import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import kotlinx.serialization.Serializable

@Serializable
data class StatsMatchEntity(
    val assists: List<FirebaseId> = emptyList(),
    val cleanSheets: List<FirebaseId> = emptyList(),
    val fails: List<FirebaseId> = emptyList(),
    val goals: List<FirebaseId> = emptyList(),
    val goalsProvoked: List<FirebaseId> = emptyList(),
    val penaltiesProvoked: List<FirebaseId> = emptyList(),
    val redCards: List<FirebaseId> = emptyList(),
    val saves: List<FirebaseId> = emptyList(),
    val yellowCards: List<FirebaseId> = emptyList()
)

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

package com.sgale.gaztelubira.core.data.network.response

import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MatchResponse(
    val id: FirebaseId = "",
    val date: Long = 0L,
    val matchName: String = "",
    val matchType: String = "",
    val localTeam: FirebaseId = "",
    val visitorTeam: FirebaseId = "",
    val localGoals: Int = 0,
    val visitorGoals: Int = 0
)

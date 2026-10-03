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

package com.sgale.gaztelubira.core.database.migration.match.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.TeamId
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId

@Entity
internal data class MatchEntity(
    @PrimaryKey
    val id: MatchId,
    val seasonId: SeasonId,

    /**
     * Information
     */
    val date: Long,
    val description: String,
    val location: String,

    /**
     * Competition
     */
    val journey: Int,
    val name: String,
    val round: String,
    val type: String,

    /**
     * Score
     */
    val formation: String,
    val localGoals: Int,
    val localTeam: TeamId,
    val visitorGoals: Int,
    val visitorTeam: TeamId,
)

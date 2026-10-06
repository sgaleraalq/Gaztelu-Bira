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

package com.sgale.gaztelubira.core.database.migration.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId

/**
 * One row per player called up for a match, so they live and die with their match: deleting a
 * [MatchEntity] cascades to its players.
 */
@Entity(
    primaryKeys = ["matchId", "playerId"],
    foreignKeys = [
        ForeignKey(
            entity = MatchEntity::class,
            parentColumns = ["id"],
            childColumns = ["matchId"],
            onDelete = CASCADE
        )
    ]
)
internal data class MatchLineupEntity(
    val matchId: MatchId,
    val playerId: PlayerId,
    val seasonId: SeasonId,
    val state: String,
    val formationPosition: Int?,
)

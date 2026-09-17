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

package com.sgale.gaztelubira.core.data.mappers

import com.sgale.gaztelubira.core.data.db.entities.PlayerEntity
import com.sgale.gaztelubira.core.data.db.entities.TeamEntity
import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.model.team.Team
import com.sgale.gaztelubira.core.domain.model.user.UserModel
import com.sgale.gaztelubira.core.network.response.player.PlayerResponse
import com.sgale.gaztelubira.core.network.response.team.TeamResponse
import com.sgale.gaztelubira.core.network.response.user.UserResponse

/**
 * Match Model
 */
fun Match.asMatchResponse() = MatchMapper.asResponse(this)
fun Match.asMatchEntity() = MatchMapper.asEntity(this)

/**
 * Match Stats
 */
fun MatchStatsModel.asMatchStatsResponse() = MatchStatsMapper.asResponse(this)
fun MatchStatsModel.asMatchStatsEntity() = MatchStatsMapper.asEntity(this)

/**
 * Player Model
 */
fun List<PlayerResponse>.asPlayerModel() =
    this.map { PlayerMapper.responseAsModel(it) }
fun PlayerResponse.asPlayerModel() = PlayerMapper.responseAsModel(this)
fun PlayerEntity.asPlayerDomain() = PlayerMapper.entityAsDomain(this)
fun Player.asPlayerResponse() = PlayerMapper.asResponse(this)
fun Player.asPlayerEntity() = PlayerMapper.asEntity(this)

/**
 * Player Stats
 */
fun PlayerStats.asPlayerStatsEntity() = PlayerStatsMapper.asEntity(this)

/**
 * Team Model
 */
fun List<TeamResponse>.asTeamModel() =
    this.map { TeamMapper.responseAsModel(it) }
fun TeamResponse.asTeamModel() = TeamMapper.responseAsModel(this)
fun TeamEntity.asTeamModel() = TeamMapper.entityAsDomain(this)
fun Team.asTeamResponse() = TeamMapper.asResponse(this)
fun Team.asTeamEntity() = TeamMapper.asEntity(this)

/**
 * User Model
 */
fun UserModel.asResponse() = UserMapper.asResponse(this)
fun UserResponse.asModel() = UserMapper.responseAsModel(this)

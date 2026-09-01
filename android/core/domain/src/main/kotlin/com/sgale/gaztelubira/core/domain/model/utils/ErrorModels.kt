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

package com.sgale.gaztelubira.core.domain.model.utils

import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.match.MatchType.League
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.team.TeamModel

val ErrorTeam = TeamModel(
    id = "error_team",
    name = "Error Team",
    logo = "https://firebasestorage.googleapis.com/v0/b/gbmultiplatform.firebasestorage.app/o/error_team.png?alt=media&token=8890839e-cc50-41db-a648-1502145e37d4"
)

val ErrorPlayer = PlayerModel(
    id = "error_player",
    name = "Error Player",
    faceImage = null,
    bodyImage = null,
    dorsal = 0,
    position = null
)

val ErrorMatch = MatchModel(
    id = "error_team",
    date = 0L,
    matchName = "Error Match",
    matchType = League,
    localTeam = ErrorTeam,
    visitorTeam = ErrorTeam,
    localGoals = 0,
    visitorGoals = 0
)

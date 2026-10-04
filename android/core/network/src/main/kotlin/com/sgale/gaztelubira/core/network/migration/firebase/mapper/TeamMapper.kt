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

package com.sgale.gaztelubira.core.network.migration.firebase.mapper

import com.sgale.gaztelubira.core.domain.migration.model.TeamId
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.migration.model.team.Team
import com.sgale.gaztelubira.core.network.migration.firebase.NetworkMapper
import com.sgale.gaztelubira.core.network.migration.firebase.response.TeamResponse

internal object TeamMapper : NetworkMapper<Team, TeamResponse, TeamId, SeasonId> {
    override fun Team.asResponse(): TeamResponse =
        TeamResponse(
            logo = logo,
            name = name
        )

    override fun TeamResponse.asModel(id: TeamId, parentId: SeasonId?) =
        Team(
            id = id,
            seasonId = requireNotNull(parentId) { "Team $id needs the season it belongs to" },
            name = name,
            logo = logo
        )
}

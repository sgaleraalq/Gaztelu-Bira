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

package com.sgale.gaztelubira.core.data.mappers

import com.sgale.gaztelubira.core.data.db.entities.TeamEntity
import com.sgale.gaztelubira.core.data.network.response.TeamResponse
import com.sgale.gaztelubira.core.domain.model.team.TeamModel

object TeamMapper :
    Mapper<TeamResponse, TeamModel, TeamEntity> {
    override fun asResponse(domain: TeamModel) =
        TeamResponse(
            id = domain.id,
            name = domain.name,
            logo = domain.logo ?: ""
        )

    override fun asEntity(domain: TeamModel) =
        TeamEntity(
            id = domain.id,
            name = domain.name,
            logo = domain.logo ?: ""
        )

    override fun entityAsDomain(entity: TeamEntity) =
        TeamModel(
            id = entity.id,
            name = entity.name,
            logo = entity.logo
        )
    override fun responseAsModel(response: TeamResponse) =
        TeamModel(
            id = response.id,
            name = response.name,
            logo = response.logo
        )
}

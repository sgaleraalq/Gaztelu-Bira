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

package com.sgale.gaztelubira.core.preview

import com.sgale.gaztelubira.core.preview.RandomValues.RANDOM_TEAM_LOGOS
import com.sgale.gaztelubira.core.preview.RandomValues.RANDOM_TEAM_NAMES
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.utils.generateRandomUUID

object TeamProvider {
    fun provideRandomTeam() = TeamModel(
        id = generateRandomUUID(),
        name = RANDOM_TEAM_NAMES.random(),
        logo = RANDOM_TEAM_LOGOS.random()
    )

    fun provideRandomTeams(numberOfTeams: Int): List<TeamModel> {
        return List(numberOfTeams) {
            provideRandomTeam()
        }
    }
}

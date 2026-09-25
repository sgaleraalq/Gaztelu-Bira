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

package com.sgale.gaztelubira.core.network.migration.firebase.season

import com.google.firebase.Timestamp
import com.sgale.gaztelubira.core.domain.migration.model.season.Season
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.network.migration.firebase.NetworkMapper
import java.util.Date

internal object SeasonMapper: NetworkMapper<Season, SeasonResponse, SeasonId> {
    override fun Season.asResponse() =
        SeasonResponse(
            name = name,
            startsAt = Timestamp(Date(startsAt)),
            endsAt = Timestamp(Date(endsAt)),
            current = true // TODO
        )

    override fun SeasonResponse.asModel(
        id: SeasonId
    ) = Season(
        id = id,
        name = name,
        startsAt = startsAt.seconds,
        endsAt = endsAt.seconds,
        isCurrent = current
    )
}

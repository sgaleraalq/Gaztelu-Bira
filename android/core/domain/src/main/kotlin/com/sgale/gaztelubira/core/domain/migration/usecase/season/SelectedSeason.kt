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

package com.sgale.gaztelubira.core.domain.migration.usecase.season

import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.migration.repository.preferences.Preferences
import com.sgale.gaztelubira.core.domain.migration.repository.season.SeasonLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * The season every screen should be looking at: the one the user picked, or the
 * current one while they have picked none.
 *
 * It lives here and not in a ViewModel so the rule is written once — otherwise
 * every screen repeats it and they drift apart.
 *
 * The choice is checked against what is stored, so a season that was picked once
 * and no longer exists falls back to the current one instead of leaving the app
 * pointing at nothing.
 *
 * @return null only while nothing has been downloaded yet.
 */
class SelectedSeason @Inject constructor(
    private val seasons: SeasonLocal,
    private val preferences: Preferences
) {
    operator fun invoke(): Flow<SeasonId?> =
        preferences.selectedSeason
            .map { chosen ->
                val stored = seasons.getSeasons()
                stored.firstOrNull { it.id == chosen }?.id
                    ?: stored.firstOrNull { it.isCurrent }?.id
            }
            .distinctUntilChanged()
}

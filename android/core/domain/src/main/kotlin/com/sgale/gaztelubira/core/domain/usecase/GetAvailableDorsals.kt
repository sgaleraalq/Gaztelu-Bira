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

package com.sgale.gaztelubira.core.domain.usecase

import com.sgale.gaztelubira.core.domain.repository.SeasonLocal
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val MIN_DORSAL = 1
private const val MAX_DORSAL = 99

/**
 * Dorsals nobody wears yet in the selected season's squad.
 */
class GetAvailableDorsals @Inject constructor(
    private val selectedSeason: SelectedSeason,
    private val seasonLocal: SeasonLocal
) {
    suspend operator fun invoke(): List<Int> {
        val season = selectedSeason().first() ?: return emptyList()
        val taken = seasonLocal.getSeasonPlayers(season).mapNotNull { it.dorsal }.toSet()
        return (MIN_DORSAL..MAX_DORSAL).filterNot { it in taken }
    }
}

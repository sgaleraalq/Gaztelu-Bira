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

import java.lang.System.currentTimeMillis

const val MATCHES_INSERTION = "matchesInsertion"
const val PLAYERS_INSERTION = "playersInsertion"
const val STATS_INSERTION = "statsInsertion"
const val TEAMS_INSERTION = "teamsInsertion"
const val MATCHES_STATS_INSERTION = "matchesStatsInsertion"
const val PLAYERS_STATS_INSERTION = "playersStatsInsertion"

private fun getActualTimeAsLong() = currentTimeMillis()

data class MatchesTimestamp(
    val matchesInsertion: Long = getActualTimeAsLong()
)

data class PlayerTimestamp(
    val playersInsertion: Long = getActualTimeAsLong()
)

data class StatsTimestamp(
    val statsInsertion: Long = getActualTimeAsLong()
)

data class TeamTimestamp(
    val teamsInsertion: Long = getActualTimeAsLong()
)

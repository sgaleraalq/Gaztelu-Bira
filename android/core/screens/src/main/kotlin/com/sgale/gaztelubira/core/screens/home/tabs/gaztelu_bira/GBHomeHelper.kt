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

package com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira

import com.sgale.gaztelubira.core.preview.TeamProvider.provideRandomTeam
import com.sgale.gaztelubira.core.domain.model.match.MatchResult
import com.sgale.gaztelubira.core.domain.model.team.TeamModel

object GBHomeHelper {
    fun provideGBInformation(appTeam: TeamModel?) =
        GBInformation(
            id = "",
            team = appTeam ?: provideRandomTeam(),
            points = randomNumber(),
            games = randomNumber(),
            wins = randomNumber(),
            draws = randomNumber(),
            loses = randomNumber(),
            goalsFor = randomNumber(),
            goalsAgainst = randomNumber(),
            streak = provideStreak()
        )

    private fun provideStreak() =
        Streak(
            currentStreak = randomNumber(),
            lastGames = provideRandomLastGames()
        )

    private fun provideRandomLastGames() =
        List(randomNumber()) {
            MatchResult.entries
                .filter { it != MatchResult.Undefined }
                .random()
        }

    private fun randomNumber() = (0..100).random()
}
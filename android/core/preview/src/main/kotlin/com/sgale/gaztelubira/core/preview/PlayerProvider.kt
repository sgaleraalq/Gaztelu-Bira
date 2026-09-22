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

package com.sgale.gaztelubira.core.preview

import com.sgale.gaztelubira.core.domain.legacy.model.player.Player
import com.sgale.gaztelubira.core.domain.legacy.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.DEFENDER
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.FORWARD
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.GOALKEEPER
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.MANAGER
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.MIDFIELDER
import com.sgale.gaztelubira.core.domain.legacy.model.stats.Stats
import com.sgale.gaztelubira.core.domain.utils.generateRandomUUID
import com.sgale.gaztelubira.core.preview.RandomValues.RANDOM_IMAGES
import com.sgale.gaztelubira.core.preview.RandomValues.RANDOM_NAMES

object PlayerProvider {

    fun providePlayerStatsList() = List((10..20).random()) {
        providePlayerStats()
    }

    fun providePlayerInformationList() = List((10..20).random()) {
        providePlayerInformation()
    }

    fun providePlayerStats(): PlayerStats {
        val randomId = generateRandomUUID()
        val randomGoals = (0..10).random()
        val randomAssists = (0..10).random()
        val randomPenaltiesProvoked = (0..5).random()
        val randomCleanSheets = (0..5).random()
        val randomSaves = (0..10).random()
        val randomYellowCards = (0..5).random()
        val randomRedCards = (0..2).random()
        val randomGamesPlayed = (5..30).random()
        val randomGoalsProvoked = (5..30).random()
        val randomFails = (5..30).random()
        val randomPercentage = (0..100).random() / 100.0

        val stats = Stats(
            goals = randomGoals,
            goalsProvoked = randomGoalsProvoked,
            assists = randomAssists,
            fails = randomFails,
            penaltiesProvoked = randomPenaltiesProvoked,
            cleanSheets = randomCleanSheets,
            saves = randomSaves,
            yellowCards = randomYellowCards,
            redCards = randomRedCards,
            gamesPlayed = randomGamesPlayed,
        )

        return PlayerStats(
            id = randomId,
            player = providePlayerInformation(),
            stats = mapOf("" to stats),
            percentage = randomPercentage
        )
    }

    fun providePlayerInformation(): Player {
        val position = listOf(MANAGER, GOALKEEPER, DEFENDER, MIDFIELDER, FORWARD).random()

        return Player(
            id = generateRandomUUID(),
            faceImage = RANDOM_IMAGES.random(),
            bodyImage = RANDOM_IMAGES.random(),
            name = RANDOM_NAMES.random(),
            dorsal = (1..99).random(),
            position = position
        )
    }
}

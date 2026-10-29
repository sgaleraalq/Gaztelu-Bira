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

package com.sgale.gaztelubira.core.domain.utils

import com.sgale.gaztelubira.core.domain.model.match.MatchCompetition
import com.sgale.gaztelubira.core.domain.model.match.MatchCompetition.Cup
import com.sgale.gaztelubira.core.domain.model.match.MatchCompetition.League
import com.sgale.gaztelubira.core.domain.model.match.MatchId
import com.sgale.gaztelubira.core.domain.model.season.SeasonId
import java.text.Normalizer

private val DIACRITICS = Regex("\\p{Mn}+")
private val NON_ALPHANUMERIC = Regex("[^a-z0-9]+")

/**
 * Builds a document id from a display name, the same way the seed does:
 * "Adrián Orta" -> "adrian_orta", "Esperanza de Arazuri" -> "esperanza_de_arazuri".
 */
fun String.toFirebaseId(): String =
    Normalizer.normalize(trim(), Normalizer.Form.NFD)
        .replace(DIACRITICS, "")
        .lowercase()
        .replace(NON_ALPHANUMERIC, "_")
        .trim('_')

/**
 * The same id the seed gives: `2026_2027_l05` for journey 5, `2026_2027_cup01` for the first cup
 * match. Deterministic on purpose, so the same journey inserted twice lands on one document.
 *
 * @param cupNumber Which cup match of the season this is, from 1. Ignored for league matches.
 */
fun matchIdOf(season: SeasonId, competition: MatchCompetition, cupNumber: Int): MatchId =
    MatchId(
        when (competition) {
            is League -> "${season.value}_l${competition.journey.twoDigits()}"
            is Cup -> "${season.value}_cup${cupNumber.twoDigits()}"
        }
    )

private fun Int.twoDigits(): String = toString().padStart(2, '0')

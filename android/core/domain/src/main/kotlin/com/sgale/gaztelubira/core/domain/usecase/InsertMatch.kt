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

import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.AlreadyExists
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.Unknown
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Inserted.Document
import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchPlayer
import com.sgale.gaztelubira.core.domain.repository.MatchLocal
import com.sgale.gaztelubira.core.domain.repository.MatchRemote
import com.sgale.gaztelubira.core.domain.repository.PlayerLocal
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

/**
 * Publishes a played match together with everyone who was called up for it, and then stores the
 * same thing locally so the app shows it without waiting for the next sync.
 */
class InsertMatch @Inject constructor(
    private val matchLocal: MatchLocal,
    private val matchRemote: MatchRemote,
    private val playerLocal: PlayerLocal
) {

    /**
     * The id of a match is deterministic, so "already exists" means somebody has published that
     * matchday before: it is reported instead of overwritten.
     *
     * @param players One per person called up, coaches included, each with what they did.
     */
    suspend operator fun invoke(
        match: Match,
        players: List<MatchPlayer>
    ): FirebaseInsertResult =
        try {
            if (matchRemote.matchExists(match.seasonId, match.id)) {
                AlreadyExists
            } else {
                matchRemote.insertMatch(match, players)

                matchLocal.insertMatches(listOf(match))
                matchLocal.insertMatchEvents(match.id, emptyList())
                matchLocal.insertMatchPlayers(match.id, players.map { it.lineup })
                playerLocal.insertMatchStats(match.id, players.map { it.stats })
                Document
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Unknown(e.message.orEmpty())
        }
}

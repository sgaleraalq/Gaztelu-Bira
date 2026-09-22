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

package com.sgale.gaztelubira.core.network.legacy.firebase.implementation.fetch

import com.google.firebase.firestore.FirebaseFirestore
import com.sgale.gaztelubira.core.domain.legacy.model.match.Match
import com.sgale.gaztelubira.core.domain.legacy.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.FirebaseConstants.INFORMATION
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.FirebaseConstants.MATCHES
import com.sgale.gaztelubira.core.network.legacy.firebase.response.match.MatchMapper.asModel
import com.sgale.gaztelubira.core.network.legacy.firebase.response.match.MatchResponse
import javax.inject.Inject

internal class FetchMatches @Inject constructor(
    firestore: FirebaseFirestore,
    gbSettings: IGBPreferences
) : Fetch(firestore, gbSettings) {
    suspend operator fun invoke(): List<Match> =
        fetchList(INFORMATION, MATCHES, MatchResponse::class.java) { it.asModel() }
}

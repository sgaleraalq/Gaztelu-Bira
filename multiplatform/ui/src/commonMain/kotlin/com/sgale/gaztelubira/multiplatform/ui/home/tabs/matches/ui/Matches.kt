/*
 * Designed and developed by 2026 sgaleraalq (Sergio Galera)
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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.matches.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.model.GBMatch

@Composable
internal fun Matches(
    matches: List<GBMatch>,
    onMatchClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = spacedBy(12.dp)
    ) {
        items(
            items = matches,
            key = { match -> match.id }
        ) { match ->
            MatchCard(
                match = match,
                onMatchClicked = onMatchClicked
            )
        }
    }
}

@Composable
private fun MatchCard(
    match: GBMatch,
    onMatchClicked: (String) -> Unit
) {
    Card(
        modifier = Modifier.clickable { onMatchClicked(match.id) },
        colors = CardDefaults.cardColors(
            containerColor = match.result.transColor
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            MatchHeader(match.name, match.type)
            Spacer(Modifier.height(12.dp))
            MatchScore(match)
            MatchInformation(match.date)
        }
    }
}

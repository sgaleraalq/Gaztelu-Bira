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

package com.sgale.gaztelubira.core.network.firebase.mapper

import com.sgale.gaztelubira.core.domain.model.match.MatchId
import com.sgale.gaztelubira.core.domain.model.match.detail.MatchEventType.GOAL
import com.sgale.gaztelubira.core.domain.model.season.SeasonId
import com.sgale.gaztelubira.core.network.firebase.mapper.MatchEventMapper.asModel
import com.sgale.gaztelubira.core.network.firebase.response.MatchEventResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MatchEventMapperTest {
    @Test
    fun `a known type is mapped`() {
        assertEquals(GOAL, event(type = "GOAL").type)
    }

    @Test
    fun `an unknown or missing type maps to no type instead of failing the read`() {
        assertNull(event(type = "OFFSIDE").type)
        assertNull(event(type = "").type)
    }

    private fun event(type: String) =
        MatchEventResponse(type = type).asModel(MatchId("2026_2027_l01"), SeasonId("2026_2027"))
}

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

package com.sgale.gaztelubira.core.notifications

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MatchNotificationPayloadTest {

    @Test
    fun `reads the match out of the data of the push`() {
        val payload = MatchNotificationPayload.fromData(
            mapOf(
                "type" to "match_inserted",
                "seasonId" to "2026_2027",
                "matchId" to "2026_2027_l08"
            )
        )

        assertEquals(MatchNotificationPayload("2026_2027", "2026_2027_l08"), payload)
    }

    @Test
    fun `ignores a push of another type`() {
        val payload = MatchNotificationPayload.fromData(
            mapOf(
                "type" to "something_else",
                "seasonId" to "2026_2027",
                "matchId" to "2026_2027_l08"
            )
        )

        assertNull(payload)
    }

    @Test
    fun `ignores a push without its ids`() {
        assertNull(MatchNotificationPayload.fromData(mapOf("type" to "match_inserted")))
        assertNull(
            MatchNotificationPayload.fromData(
                mapOf("type" to "match_inserted", "seasonId" to "2026_2027", "matchId" to "")
            )
        )
    }

    @Test
    fun `ignores data that is not a push at all`() {
        assertNull(MatchNotificationPayload.fromData(emptyMap()))
    }
}

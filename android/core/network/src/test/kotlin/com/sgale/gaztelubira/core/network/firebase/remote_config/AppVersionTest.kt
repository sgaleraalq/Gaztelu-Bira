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

package com.sgale.gaztelubira.core.network.firebase.remote_config

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppVersionTest {
    @Test
    fun `a later minor is supported even with a lower patch`() {
        assertTrue(isVersionAtLeast(current = "1.5.0", minimum = "1.4.9"))
    }

    @Test
    fun `a later major is supported even with a lower minor and patch`() {
        assertTrue(isVersionAtLeast(current = "2.0.0", minimum = "1.5.3"))
    }

    @Test
    fun `the minimum itself is supported`() {
        assertTrue(isVersionAtLeast(current = "1.4.9", minimum = "1.4.9"))
    }

    @Test
    fun `an older version is not supported`() {
        assertFalse(isVersionAtLeast(current = "1.4.8", minimum = "1.4.9"))
        assertFalse(isVersionAtLeast(current = "1.9.9", minimum = "2.0.0"))
    }

    @Test
    fun `a missing part counts as zero`() {
        assertTrue(isVersionAtLeast(current = "1.5", minimum = "1.5.0"))
        assertFalse(isVersionAtLeast(current = "1.5", minimum = "1.5.1"))
    }

    @Test
    fun `no minimum lets every version through`() {
        assertTrue(isVersionAtLeast(current = "1.0.0", minimum = ""))
    }

    @Test
    fun `a suffix after the number is ignored`() {
        assertTrue(isVersionAtLeast(current = "1.5.0-debug", minimum = "1.5.0"))
    }
}

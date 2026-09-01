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

package com.sgale.gaztelubira.core.domain.utils

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object CameraResults {
    private val mutex = Mutex()
    private val waiters = mutableMapOf<String, CompletableDeferred<CommonImage>>()

    @Suppress("UNCHECKED_CAST")
    suspend fun <T> awaitResult(key: String): T {
        val deferred = CompletableDeferred<CommonImage>()
        mutex.withLock { waiters[key] = deferred }
        return deferred.await() as T
    }

    suspend fun deliver(key: String, value: CommonImage) {
        val waiter = mutex.withLock { waiters.remove(key) }
        waiter?.complete(value)
    }
}

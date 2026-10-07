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

package com.sgale.gaztelubira.core.database.legacy.implementations

import com.sgale.gaztelubira.core.database.legacy.GBDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

abstract class AbstractGBDb(
    protected val db: GBDatabase
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun <Entity, Model, K : Comparable<K>> getFlow(
        source: Flow<List<Entity>>,
        mapper: suspend (Entity) -> Model,
        keySelector: (Model) -> K
    ): Flow<List<Model>> =
        source.mapLatest { list ->
            val mapped = ArrayList<Model>(list.size)
            list.forEach { element -> mapped += mapper(element) }
            mapped.sortedBy(keySelector)
        }
}

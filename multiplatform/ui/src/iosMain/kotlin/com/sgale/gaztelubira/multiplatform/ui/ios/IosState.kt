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

package com.sgale.gaztelubira.multiplatform.ui.ios

import androidx.compose.runtime.mutableStateOf

/**
 * What a Swift view model writes its screen state into. Swift cannot hand Compose a `StateFlow`,
 * so the state crosses the bridge inside a snapshot state instead: assigning [value] from Swift
 * recomposes whatever read it.
 *
 * Writes are expected on the main thread, which is where the Swift view models live.
 */
class IosState<T : Any>(initial: T) {

    private val state = mutableStateOf(initial)

    var value: T
        get() = state.value
        set(value) {
            state.value = value
        }
}

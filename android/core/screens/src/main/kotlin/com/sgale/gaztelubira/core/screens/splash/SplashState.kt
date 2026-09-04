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

package com.sgale.gaztelubira.core.screens.splash

import com.sgale.gaztelubira.core.screens.navigation.Destination
import com.sgale.gaztelubira.core.screens.navigation.Destination.Welcome
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

class SplashState @Inject constructor() : SplashContractor {
    override val completed = MutableStateFlow(false)
    override val avoid = MutableStateFlow(false)
    override val destination = MutableStateFlow<Destination>(Welcome)

    override fun contractCompleted(newDestination: Destination) {
        destination.value = newDestination
        completed.value = true
    }

    override fun avoidSplash(newDestination: Destination) {
        destination.value = newDestination
        avoid.value = true
    }

    override fun reset() {
        completed.value = false
        avoid.value = false
        destination.value = Welcome
    }
}

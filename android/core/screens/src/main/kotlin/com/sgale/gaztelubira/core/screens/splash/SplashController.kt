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


package com.sgale.gaztelubira.core.screens.splash

import com.sgale.gaztelubira.core.screens.navigation.Destination
import kotlinx.coroutines.flow.StateFlow

/**
 * App-scoped hand-off between the boot sequence, which drives it, and the splash screen, which only
 * observes it. Writing is done through the three calls below so no other holder of this singleton
 * can push the splash into a state the boot sequence did not ask for.
 */
interface SplashController {
    val status: StateFlow<SplashStatus>

    /** Boot finished with work worth showing: the bar completes, then the app moves on. */
    fun complete(destination: Destination)

    /** Boot had nothing to wait for: the splash steps aside immediately. */
    fun skip(destination: Destination)

    /** Back to square one, for when the user logs out and the app boots again. */
    fun reset()
}

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

/**
 * How far the app boot has got, as the splash sees it.
 *
 * One value instead of a pair of booleans plus a loose destination: "completed and skipped at the
 * same time" stops being representable, and the destination cannot be read before the decision that
 * produced it has been made.
 */
sealed interface SplashStatus {

    /** Boot work is still running. */
    data object Loading : SplashStatus

    /** Boot work is over and [destination] is where the app goes next. */
    sealed interface Finished : SplashStatus {
        val destination: Destination

        /** The splash was worth showing: let the progress bar finish before leaving. */
        data class Completed(override val destination: Destination) : Finished

        /** There was nothing to wait for: leave the splash straight away. */
        data class Skipped(override val destination: Destination) : Finished
    }
}

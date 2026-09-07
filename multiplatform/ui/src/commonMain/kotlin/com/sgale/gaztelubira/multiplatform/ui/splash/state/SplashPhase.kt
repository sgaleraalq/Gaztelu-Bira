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

package com.sgale.gaztelubira.multiplatform.ui.splash.state

/**
 * The splash is only ever in one of these, never in a combination of them.
 */
enum class SplashPhase {
    /** Boot work is still running; the bar creeps up without ever claiming to be done. */
    LOADING,

    /** Boot work is over; the bar runs to 100% and only then hands over to navigation. */
    COMPLETING,

    /** There was nothing to wait for; the splash steps aside straight away. */
    SKIPPED
}

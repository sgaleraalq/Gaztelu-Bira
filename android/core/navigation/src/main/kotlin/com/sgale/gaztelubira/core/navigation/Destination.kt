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

package com.sgale.gaztelubira.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Every screen of the app, as a Navigation 3 key.
 *
 * Keys only carry what their screen needs to be rebuilt, never the screen itself: the content of
 * each one is registered where the screens live. Being serializable is what lets the back stack
 * survive configuration changes and process death.
 */
@Serializable
sealed interface Destination : NavKey {

    /**
     * HOME
     */
    @Serializable
    data object Home : Destination

    @Serializable
    data object Splash : Destination

    /**
     * Auth screens
     */
    @Serializable
    data object Welcome : Destination

    @Serializable
    data object SignUp : Destination

    @Serializable
    data object Login : Destination

    /**
     * Details screens
     */
    @Serializable
    data class MatchDetail(
        val matchId: String
    ) : Destination

    @Serializable
    data class PlayerDetail(
        val playerId: String,
        val isManager: Boolean
    ) : Destination

    /**
     * Insert screens
     */
    @Serializable
    data object InsertMatch : Destination

    @Serializable
    data object InsertPlayer : Destination

    @Serializable
    data object InsertTeam : Destination

    @Serializable
    data object ReviewPhoto : Destination
}

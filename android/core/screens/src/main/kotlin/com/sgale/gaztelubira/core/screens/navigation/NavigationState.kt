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

package com.sgale.gaztelubira.core.screens.navigation

import androidx.compose.runtime.State
import com.sgale.gaztelubira.core.screens.navigation.Destination.Home
import com.sgale.gaztelubira.core.screens.navigation.Destination.InsertMatch
import com.sgale.gaztelubira.core.screens.navigation.Destination.InsertPlayer
import com.sgale.gaztelubira.core.screens.navigation.Destination.InsertTeam
import com.sgale.gaztelubira.core.screens.navigation.Destination.Login
import com.sgale.gaztelubira.core.screens.navigation.Destination.MatchDetail
import com.sgale.gaztelubira.core.screens.navigation.Destination.PlayerDetail
import com.sgale.gaztelubira.core.screens.navigation.Destination.ReviewPhoto
import com.sgale.gaztelubira.core.screens.navigation.Destination.SignUp
import com.sgale.gaztelubira.core.screens.navigation.Destination.Splash
import com.sgale.gaztelubira.core.screens.navigation.Destination.Welcome
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlin.reflect.KClass

interface NavigationState {
    val currentDestination: State<Destination?>

    fun navigateTo(destination: Destination, clearStack: Boolean = false)
    fun popUpTo(destination: Destination)
    fun navigateBack()
}

sealed interface DestinationConfiguration<T : Destination> {

    val clazz: KClass<T>
    val subclassRegisterer: (PolymorphicModuleBuilder<Destination>) -> Unit

    data class NoParams<T : Destination>(
        val instance: T,
        override val clazz: KClass<T>,
        override val subclassRegisterer: (PolymorphicModuleBuilder<Destination>) -> Unit
    ) : DestinationConfiguration<T>

    data class WithArguments<T : Destination>(
        override val clazz: KClass<T>,
        override val subclassRegisterer: (PolymorphicModuleBuilder<Destination>) -> Unit
    ) : DestinationConfiguration<T>

}

inline fun <reified T : Destination> T.configuration(): DestinationConfiguration.NoParams<T> {
    return DestinationConfiguration.NoParams(
        instance = this,
        clazz = T::class,
        subclassRegisterer = {
            it.subclass(
                subclass = T::class,
                serializer = kotlinx.serialization.serializer()
            )
        }
    )
}

inline fun <reified T : Destination> KClass<T>.configuration(): DestinationConfiguration.WithArguments<T> {
    return DestinationConfiguration.WithArguments(
        clazz = this,
        subclassRegisterer = {
            it.subclass(
                subclass = this@configuration,
                serializer = kotlinx.serialization.serializer()
            )
        }
    )
}

val defaultDestinations: List<DestinationConfiguration<*>> = listOf(
    Splash.configuration(),
    /**
     * Auth
     */
    Welcome.configuration(),
    Login.configuration(),
    SignUp.configuration(),

    /**
     * GBHome
     */
    Home.configuration(),

    /**
     * Insert
     */
    InsertMatch.configuration(),
    InsertPlayer.configuration(),
    InsertTeam.configuration(),

    /**
     * Details
     */
    MatchDetail::class.configuration(),
    PlayerDetail::class.configuration(),
    ReviewPhoto::class.configuration()
)

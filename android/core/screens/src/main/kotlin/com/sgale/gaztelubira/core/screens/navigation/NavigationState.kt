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

package com.sgale.gaztelubira.core.screens.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.key
import androidx.compose.runtime.saveable.SaveableStateHolder
import com.sgale.gaztelubira.core.domain.utils.CameraResults
import com.sgale.gaztelubira.core.domain.utils.CommonImage
import com.sgale.gaztelubira.core.screens.navigation.Destination.Camera
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

suspend inline fun <reified T> NavigationState.navigateForResult(
    destination: Destination,
    resultKey: String
): T {
    navigateTo(destination)
    return CameraResults.awaitResult(resultKey)
}

suspend fun NavigationState.launchAndDeliver(key: String, value: CommonImage) {
    CameraResults.deliver(key, value)
    popUpTo(InsertPlayer)
}

interface MultiplatformNavigationState : NavigationState {
    override val currentDestination: State<Destination>
    val stateHolder: SaveableStateHolder
}

@Composable
fun MultiplatformMainNavigation(
    state: NavigationState,
    enabled: Boolean
) {
    state as MultiplatformNavigationState

    MultiplatformBackHandler(enabled) { state.navigateBack() }

    Crossfade(
        targetState = state.currentDestination.value
    ) { destination ->
        state.stateHolder.SaveableStateProvider(destination.toString()) {
            key(destination) { destination.Content(state) }
        }
    }

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
    Camera::class.configuration(),
    ReviewPhoto::class.configuration()
)

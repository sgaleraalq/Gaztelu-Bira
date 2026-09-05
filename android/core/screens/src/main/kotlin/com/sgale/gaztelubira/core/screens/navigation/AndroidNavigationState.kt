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

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sgale.gaztelubira.core.screens.navigation.Destination.Splash
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlin.reflect.KClass

@Composable
fun rememberNavigationState(): NavigationState {
    val navController = rememberNavController()

    return remember {
        AndroidNavigationState(
            navController,
            defaultDestinations
        )
    }
}

@Composable
fun MainNavigation(state: NavigationState, enabled: Boolean = false) {
    state as AndroidNavigationState

    NavHost(
        navController = state.navHostController,
        startDestination = state.getRoute(state.defaultDestination::class),
        modifier = Modifier.fillMaxSize()
    ) {
        state.applyDestinations(this)
    }
}

class AndroidNavigationState(
    val navHostController: NavHostController,
    val configurations: List<DestinationConfiguration<*>>
) : NavigationState {
    companion object {
        private const val DESTINATION_ARGUMENT_KEY = "arg"

        private val serializedDestinationArgument = navArgument(
            name = DESTINATION_ARGUMENT_KEY,
            builder = { type = NavType.StringType }
        )
    }

    private val json: Json = getJsonWithSerializableDestinationsSupport()
    private val clazzToConfiguration = configurations.associateBy { it.clazz }
    private val routeToConfiguration = configurations.associateBy { getRoute(it.clazz) }

    private val _currentDestination = mutableStateOf<Destination?>(null)
    override val currentDestination: State<Destination?> = _currentDestination

    var defaultDestination: Destination = Splash

    override fun navigateBack() {
        navHostController.navigateUp()
    }

    override fun navigateTo(
        destination: Destination,
        clearStack: Boolean
    ) {
        val route = getActualRoute(destination)
        navHostController.navigate(route) {
            if (clearStack) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    override fun popUpTo(destination: Destination) {
        navHostController.popBackStack(
            route = getRoute(destination::class),
            inclusive = false,
            saveState = false
        )
    }

    fun applyDestinations(builder: NavGraphBuilder) {
        configurations.forEach { builder.registerDestination(it) }
    }

    private fun getJsonWithSerializableDestinationsSupport(): Json {
        return Json {
            serializersModule = SerializersModule {
                polymorphic(Destination::class) {
                    configurations.forEach { it.subclassRegisterer.invoke(this) }
                }
            }
        }
    }

    private fun NavGraphBuilder.registerDestination(configuration: DestinationConfiguration<*>) {
        composable(
            route = getRoute(configuration.clazz),
            arguments = when (configuration) {
                is DestinationConfiguration.NoParams -> emptyList()
                is DestinationConfiguration.WithArguments -> listOf(
                    serializedDestinationArgument
                )
            },
            content = {
                val destination = getDestination(it)
                _currentDestination.value = destination
                Box(modifier = Modifier.fillMaxSize()) {
                    destination.Content(this@AndroidNavigationState)
                }
            }
        )
    }

    fun getRoute(clazz: KClass<out Destination>): String {
        val configuration = clazzToConfiguration.getValue(clazz)
        return when (configuration) {
            is DestinationConfiguration.NoParams -> clazz.simpleName!!
            is DestinationConfiguration.WithArguments -> "${clazz.simpleName}/{$DESTINATION_ARGUMENT_KEY}"
        }
    }

    private fun getActualRoute(destination: Destination): String {
        val configuration = clazzToConfiguration.getValue(destination::class)
        if (configuration is DestinationConfiguration.NoParams)
            return destination::class.simpleName!!

        val uri = Uri.encode(
            json.encodeToString(PolymorphicSerializer(Destination::class), destination)
        )
        return "${destination::class.simpleName}/$uri"
    }

    private fun getDestination(entry: NavBackStackEntry): Destination {
        val route = entry.destination.route ?: getRoute(defaultDestination::class)
        val configuration = routeToConfiguration.getValue(route)

        if (configuration is DestinationConfiguration.NoParams) {
            return configuration.instance
        }

        val serializedDestination = Uri.decode(
            entry.arguments!!.getString(DESTINATION_ARGUMENT_KEY)!!
        )
        return json.decodeFromString<Destination>(serializedDestination)
    }
}

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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.navigation3.ui.NavDisplay
import com.sgale.gaztelubira.core.navigation.Destination.Splash

@Composable
fun rememberNavigationState(startDestination: Destination = Splash): NavigationState {
    val backStack = rememberSerializable(serializer = NavBackStackSerializer<Destination>()) {
        NavBackStack(startDestination)
    }

    return remember(backStack) { AndroidNavigationState(backStack) }
}

/**
 * Shows the screen on top of the back stack. [entries] pairs every [Destination] with its content
 * through Navigation 3's `entry<Destination.X> { ... }` DSL.
 */
@Composable
fun MainNavigation(
    state: NavigationState,
    modifier: Modifier = Modifier,
    entries: EntryProviderScope<Destination>.() -> Unit
) {
    state as AndroidNavigationState

    NavDisplay(
        backStack = state.backStack,
        modifier = modifier.fillMaxSize(),
        onBack = state::navigateBack,
        /**
         * Each entry gets its own saved state and ViewModelStore, so `hiltViewModel()` is scoped
         * to its screen and cleared once that screen leaves the back stack.
         */
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            FullScreenNavEntryDecorator
        ),
        entryProvider = entryProvider(builder = entries)
    )
}

internal class AndroidNavigationState(
    val backStack: NavBackStack<Destination>
) : NavigationState {

    override val currentDestination: State<Destination> = derivedStateOf { backStack.last() }

    override fun navigateTo(
        destination: Destination,
        clearStack: Boolean
    ) {
        when {
            /**
             * Swapped in a single snapshot so NavDisplay never sees an empty back stack.
             */
            clearStack -> Snapshot.withMutableSnapshot {
                backStack.clear()
                backStack.add(destination)
            }

            /**
             * Two entries with the same key would share their state, so navigating again to the
             * screen on top (a double tap, for instance) is ignored.
             */
            backStack.last() != destination -> backStack.add(destination)
        }
    }

    override fun popUpTo(destination: Destination) {
        val index = backStack.lastIndexOf(destination)
        if (index != -1) {
            backStack.subList(index + 1, backStack.size).clear()
        }
    }

    override fun navigateBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }
}

/**
 * Every screen gets the whole display, as it did under the NavHost.
 */
private val FullScreenNavEntryDecorator = NavEntryDecorator<Destination> { entry ->
    Box(modifier = Modifier.fillMaxSize()) {
        entry.Content()
    }
}

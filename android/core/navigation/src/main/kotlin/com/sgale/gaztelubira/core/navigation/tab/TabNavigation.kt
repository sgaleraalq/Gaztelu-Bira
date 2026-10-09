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

package com.sgale.gaztelubira.core.navigation.tab

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.ViewModelStoreProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

private const val TAB_VIEW_MODEL_STORES_KEY = "TabNavigation.viewModelStores"

@Composable
fun <T : Any> rememberTabNavigationState(
    tabs: List<T>,
    initialTab: T
): TabNavigationState<T> {
    require(initialTab in tabs) { "$initialTab is not one of $tabs" }

    /**
     * Saving the index instead of the tab means tabs do not need to be saveable themselves.
     */
    val selectedIndex = rememberSaveable { mutableIntStateOf(tabs.indexOf(initialTab)) }

    return remember(tabs) { AndroidTabNavigationState(tabs, selectedIndex) }
}

/**
 * Shows the selected tab only. A hidden tab keeps its saved state and its ViewModels, like the
 * NavHost did with `saveState` and `restoreState`, until the screen hosting the tabs is popped.
 */
@Composable
fun <T : Any> TabNavigation(
    state: TabNavigationState<T>,
    modifier: Modifier = Modifier,
    content: @Composable (tab: T) -> Unit
) {
    state as AndroidTabNavigationState<T>

    val currentContent by rememberUpdatedState(content)

    /**
     * Every tab stays in the back stack these entries are decorated against, so switching tabs
     * never pops one: the tab being left only goes out of composition.
     */
    val entries = rememberDecoratedNavEntries(
        backStack = state.tabs,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(rememberTabViewModelStoreProvider())
        ),
        entryProvider = { tab -> NavEntry(tab) { currentContent(it) } }
    )

    NavDisplay(
        entries = listOf(entries[state.selectedIndex.intValue]),
        modifier = modifier.fillMaxSize(),
        onBack = { /* A single entry is shown, so back is never handled here */ }
    )
}

/**
 * The provider made by default clears its ViewModelStores as soon as it leaves composition, and
 * the tabs leave it every time a screen is opened on top of them. This one lives in the
 * ViewModelStore of the screen hosting the tabs instead, so it is cleared together with it.
 */
@Composable
private fun rememberTabViewModelStoreProvider(): ViewModelStoreProvider {
    val parent = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    return remember(parent) {
        ViewModelStoreProvider(parentOwner = parent, parentKey = TAB_VIEW_MODEL_STORES_KEY)
    }
}

internal class AndroidTabNavigationState<T : Any>(
    val tabs: List<T>,
    val selectedIndex: MutableIntState
) : TabNavigationState<T> {

    override val selectedTab: State<T> = derivedStateOf { tabs[selectedIndex.intValue] }

    override fun navigate(tab: T) {
        require(tab in tabs) { "$tab is not one of $tabs" }
        selectedIndex.intValue = tabs.indexOf(tab)
    }
}

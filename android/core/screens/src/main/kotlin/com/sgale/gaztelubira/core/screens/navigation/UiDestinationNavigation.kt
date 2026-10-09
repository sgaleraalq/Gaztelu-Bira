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

import com.sgale.gaztelubira.core.navigation.Destination
import com.sgale.gaztelubira.core.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.UiDestination
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.Back
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromGazteluBiraTab
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromLogin
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromMatchesTab
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromTeamTab

/**
 * The shared UI only knows about [UiDestination]s; this maps them onto the app's [Destination]s.
 */
internal fun NavigationState.navigateTo(uiDestination: UiDestination) {
    when (uiDestination) {
        Back -> navigateBack()
        is FromGazteluBiraTab -> navigateTo(uiDestination.toDestination())
        is FromLogin -> navigateTo(uiDestination.toDestination())
        is FromMatchesTab -> navigateTo(uiDestination.toDestination())
        is FromTeamTab -> navigateTo(uiDestination.toDestination())
    }
}

private fun FromGazteluBiraTab.toDestination(): Destination =
    when (this) {
        FromGazteluBiraTab.InsertTeam -> Destination.InsertTeam
    }

private fun FromMatchesTab.toDestination(): Destination =
    when (this) {
        FromMatchesTab.InsertMatch -> Destination.InsertMatch
        is FromMatchesTab.MatchDetail -> Destination.MatchDetail(id)
    }

private fun FromLogin.toDestination(): Destination =
    when (this) {
        FromLogin.SignUp -> Destination.SignUp
        FromLogin.Splash -> Destination.Splash
    }

private fun FromTeamTab.toDestination(): Destination =
    when (this) {
        FromTeamTab.InsertPlayer -> Destination.InsertPlayer
        is FromTeamTab.PlayerDetail -> Destination.PlayerDetail(id, false) // TODO
    }

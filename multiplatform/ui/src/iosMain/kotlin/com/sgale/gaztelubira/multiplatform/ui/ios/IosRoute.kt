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

package com.sgale.gaztelubira.multiplatform.ui.ios

import com.sgale.gaztelubira.multiplatform.ui.UiDestination
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.Back
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromGazteluBiraTab
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromLogin
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromMatchesTab
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromTeamTab

enum class IosRouteKind {
    BACK,
    SIGN_UP,
    SPLASH,
    INSERT_TEAM,
    INSERT_MATCH,
    MATCH_DETAIL,
    INSERT_PLAYER,
    PLAYER_DETAIL
}

/**
 * A [UiDestination] flattened for Swift. Sealed hierarchies reach Swift as loose classes with
 * mangled names and no exhaustive `switch`, so what the shared UI asks for is handed over as a
 * kind plus the id the detail screens need.
 */
class IosRoute internal constructor(
    val kind: IosRouteKind,
    val id: String = ""
)

internal fun UiDestination.toIosRoute(): IosRoute =
    when (this) {
        Back -> IosRoute(IosRouteKind.BACK)
        FromLogin.SignUp -> IosRoute(IosRouteKind.SIGN_UP)
        FromLogin.Splash -> IosRoute(IosRouteKind.SPLASH)
        FromGazteluBiraTab.InsertTeam -> IosRoute(IosRouteKind.INSERT_TEAM)
        FromMatchesTab.InsertMatch -> IosRoute(IosRouteKind.INSERT_MATCH)
        is FromMatchesTab.MatchDetail -> IosRoute(IosRouteKind.MATCH_DETAIL, id)
        FromTeamTab.InsertPlayer -> IosRoute(IosRouteKind.INSERT_PLAYER)
        is FromTeamTab.PlayerDetail -> IosRoute(IosRouteKind.PLAYER_DETAIL, id)
    }

internal fun ((IosRoute) -> Unit).asUiNavigation(): (UiDestination) -> Unit =
    { destination -> this(destination.toIosRoute()) }

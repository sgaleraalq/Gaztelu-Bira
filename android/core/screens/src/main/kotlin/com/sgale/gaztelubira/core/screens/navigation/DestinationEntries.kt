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

import androidx.navigation3.runtime.EntryProviderScope
import com.sgale.gaztelubira.core.domain.model.match.MatchId
import com.sgale.gaztelubira.core.navigation.Destination
import com.sgale.gaztelubira.core.navigation.Destination.Home
import com.sgale.gaztelubira.core.navigation.Destination.InsertMatch
import com.sgale.gaztelubira.core.navigation.Destination.InsertPlayer
import com.sgale.gaztelubira.core.navigation.Destination.InsertTeam
import com.sgale.gaztelubira.core.navigation.Destination.Login
import com.sgale.gaztelubira.core.navigation.Destination.MatchDetail
import com.sgale.gaztelubira.core.navigation.Destination.PlayerDetail
import com.sgale.gaztelubira.core.navigation.Destination.ReviewPhoto
import com.sgale.gaztelubira.core.navigation.Destination.SignUp
import com.sgale.gaztelubira.core.navigation.Destination.Splash
import com.sgale.gaztelubira.core.navigation.Destination.Welcome
import com.sgale.gaztelubira.core.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.auth.login.LoginScreen
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpScreen
import com.sgale.gaztelubira.core.screens.detail.match.MatchDetailScreen
import com.sgale.gaztelubira.core.screens.detail.player.PlayerDetailScreen
import com.sgale.gaztelubira.core.screens.home.HomeScreen
import com.sgale.gaztelubira.core.screens.insert.match.InsertMatchScreen
import com.sgale.gaztelubira.core.screens.insert.player.InsertPlayerScreen
import com.sgale.gaztelubira.core.screens.insert.team.InsertTeamScreen
import com.sgale.gaztelubira.core.screens.splash.SplashScreen
import com.sgale.gaztelubira.multiplatform.ui.auth.welcome.WelcomeView

/**
 * The screen each [Destination] shows. Destinations are plain keys in `:core:navigation`, so their
 * content is wired here, next to the screens.
 */
internal fun EntryProviderScope<Destination>.destinationEntries(state: NavigationState) {
    /**
     * HOME
     */
    entry<Home> { HomeScreen(state) }
    entry<Splash> { SplashScreen(state) }

    /**
     * Auth screens
     */
    entry<Welcome> {
        WelcomeView(
            navigateToLogin = { state.navigateTo(Login) }
        )
    }
    entry<SignUp> { SignUpScreen(state) }
    entry<Login> { LoginScreen(state) }

    /**
     * Details screens
     */
    entry<MatchDetail> { destination ->
        MatchDetailScreen(state, MatchId(destination.matchId))
    }
    entry<PlayerDetail> { destination ->
        PlayerDetailScreen(state, destination.playerId, destination.isManager)
    }

    /**
     * Insert screens
     */
    entry<InsertMatch> { InsertMatchScreen(state) }
    entry<InsertPlayer> { InsertPlayerScreen(state) }
    entry<InsertTeam> { InsertTeamScreen(state) }
    entry<ReviewPhoto> {
//        ReviewImageScreen(
//            commonImage = commonImage,
//            isFrontCamera = commonImage is FromFrontCamera,
//            onRepeat = { state.navigateBack() },
//            onAccept = { /* TODO */ }
//        )
    }
}

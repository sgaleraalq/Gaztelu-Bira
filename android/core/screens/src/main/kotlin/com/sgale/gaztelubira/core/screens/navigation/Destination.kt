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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.utils.CameraManagerCompose
import com.sgale.gaztelubira.core.domain.utils.CommonImage
import com.sgale.gaztelubira.core.domain.utils.CommonImage.FromFrontCamera
import com.sgale.gaztelubira.core.screens.auth.login.LoginScreen
import com.sgale.gaztelubira.core.screens.auth.signup.SignUpScreen
import com.sgale.gaztelubira.core.screens.home.HomeScreen
import com.sgale.gaztelubira.core.screens.insert_match.InsertMatchScreen
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerScreen
import com.sgale.gaztelubira.core.screens.insert_team.InsertTeamScreen
import com.sgale.gaztelubira.core.screens.match_detail.MatchDetailScreen
import com.sgale.gaztelubira.core.screens.player_detail.PlayerDetailScreen
import com.sgale.gaztelubira.core.screens.review_photo.ReviewImageScreen
import com.sgale.gaztelubira.core.screens.splash.SplashScreen
import com.sgale.gaztelubira.multiplatform.ui.UiDestination
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromLogin
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromTeamTab
import com.sgale.gaztelubira.multiplatform.ui.auth.welcome.WelcomeView
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

interface Destination {
    @Composable
    fun Content(state: NavigationState)
    val routeName: String

    /**
     * HOME
     */
    @Serializable
    data object Home : Destination {
        override val routeName = "home"

        @Composable
        override fun Content(state: NavigationState) {
            HomeScreen(state)
        }
    }

    @Serializable
    data object Splash: Destination {
        override val routeName = "splash"

        @Composable
        override fun Content(state: NavigationState) {
            SplashScreen(state)
        }
    }

    /**
     * Auth screens
     */
    @Serializable
    data object Welcome : Destination {
        override val routeName = "welcome"

        @Composable
        override fun Content(state: NavigationState) {
            WelcomeView(
                navigateToLogin = { state.navigateTo(Login) }
            )
        }
    }

    @Serializable
    data object SignUp : Destination {
        override val routeName = "sign_up"

        @Composable
        override fun Content(state: NavigationState) {
            SignUpScreen(state)
        }
    }

    @Serializable
    data object Login : Destination {
        override val routeName = "login"

        @Composable
        override fun Content(state: NavigationState) {
            LoginScreen(state)
        }
    }

    /**
     * Details screens
     */
    @Serializable
    data class MatchDetail(
        val matchId: FirebaseId
    ) : Destination {
        override val routeName = "match_detail"

        @Composable
        override fun Content(state: NavigationState) {
            MatchDetailScreen(state, matchId)
        }
    }

    @Serializable
    data class PlayerDetail(
        val playerId: String,
        val isManager: Boolean
    ) : Destination {
        override val routeName = "player_information"

        @Composable
        override fun Content(state: NavigationState) {
            PlayerDetailScreen(playerId, isManager, state::navigateBack)
        }
    }

    /**
     * Insert screens
     */
    @Serializable
    data object InsertMatch: Destination {
        override val routeName = "insert_match"

        @Composable
        override fun Content(state: NavigationState) {
            InsertMatchScreen(state)
        }
    }

    @Serializable
    data object InsertPlayer : Destination {
        override val routeName = "insert_player"

        @Composable
        override fun Content(state: NavigationState) {
            InsertPlayerScreen(state)
        }
    }

    @Serializable
    data object InsertTeam : Destination {
        override val routeName = "insert_team"

        @Composable
        override fun Content(state: NavigationState) {
            InsertTeamScreen(state)
        }
    }

    /**
     * Camera
     */
    @Serializable
    data class Camera(
        val key: String
    ): Destination {
        override val routeName = "camera"

        @Composable
        override fun Content(state: NavigationState) {
            CameraManagerCompose(
                key = key,
                navigateToReview = { commonImage ->
                    state.navigateTo(ReviewPhoto(key, commonImage))
                },
                navigateBack = { state.navigateBack() }
            )
        }
    }

    @Serializable
    data class ReviewPhoto(
        val key: String,
        val commonImage: CommonImage
    ) : Destination {
        override val routeName = "review_photo"

        @Composable
        override fun Content(state: NavigationState) {
            val scope = rememberCoroutineScope()
            ReviewImageScreen(
                commonImage = commonImage,
                isFrontCamera = commonImage is FromFrontCamera,
                onRepeat = { state.navigateBack() },
                onAccept = {
                    scope.launch {
                        state.launchAndDeliver(key, commonImage)
                    }
                }
            )
        }
    }

    companion object {
        internal fun UiDestination.toDestination(): Destination =
            when (this) {
                is FromLogin -> this.toDestination()
                is FromTeamTab -> this.toDestination()
            }

        private fun FromLogin.toDestination(): Destination =
            when (this) {
                FromLogin.SignUp -> SignUp
                FromLogin.Splash -> Splash
            }

        private fun FromTeamTab.toDestination(): Destination =
            when (this) {
                FromTeamTab.InsertPlayer -> InsertPlayer
                is FromTeamTab.PlayerDetail -> PlayerDetail(id, false) // TODO
            }
    }
}

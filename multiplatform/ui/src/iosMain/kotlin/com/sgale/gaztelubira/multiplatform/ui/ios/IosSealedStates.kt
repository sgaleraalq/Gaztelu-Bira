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

import com.sgale.gaztelubira.multiplatform.model.GBPunctuation
import com.sgale.gaztelubira.multiplatform.ui.auth.AuthState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailLineUp
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState
import com.sgale.gaztelubira.multiplatform.ui.detail.player.PlayerDetailUiState.PlayerWinRate
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsSettings
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsState
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertDataUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog

/**
 * The members of the sealed hierarchies the Swift view models have to produce or tell apart.
 *
 * Swift could name them itself, but a nested Kotlin declaration reaches it under a name the
 * exporter makes up (`AuthState.Loading.Login.Email` becomes `AuthStateLoadingLoginEmail`). Asking
 * for them here keeps those names out of the Swift code.
 */

/**
 * Auth
 */
fun authDefault(): AuthState = AuthState.Default

fun authError(): AuthState = AuthState.Error

fun authLoadingEmail(): AuthState = AuthState.Loading.Login.Email

fun authLoadingGoogle(): AuthState = AuthState.Loading.Login.Google

fun authLoadingSignUp(): AuthState = AuthState.Loading.SignUp

fun isAuthError(state: AuthState): Boolean = state == AuthState.Error

/**
 * Stats tab
 */
fun statsStateDefault(): GBStatsState = GBStatsState.Loaded.Default

fun statsSettingsHidden(): GBStatsSettings = GBStatsSettings.Hidden

fun statsSettingsChangePunctuation(draft: GBPunctuation): GBStatsSettings =
    GBStatsSettings.ChangePunctuation(draft)

/**
 * Match detail
 */
fun matchDetailLineup(lineUp: MatchDetailLineUp?): MatchDetailState =
    MatchDetailState.Lineup(lineUp)

/**
 * Player detail
 */
fun playerWinRate(
    wins: Int,
    draws: Int,
    loses: Int
): PlayerWinRate = PlayerWinRate(wins = wins, draws = draws, loses = loses)

/**
 * Insert screens
 */
fun insertPlayerDialogNone(): InsertPlayerDialog = InsertPlayerDialog.None

fun insertingDefault(): InsertingDataState = InsertingDataState.Default

fun insertingLoading(): InsertingDataState = InsertingDataState.Loading

fun isInsertValid(state: InsertDataUiState): Boolean = state.handler.isValid

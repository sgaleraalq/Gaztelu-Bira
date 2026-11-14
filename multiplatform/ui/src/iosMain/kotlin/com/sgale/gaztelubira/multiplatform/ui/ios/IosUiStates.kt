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

import com.sgale.gaztelubira.multiplatform.model.GBMatchResult
import com.sgale.gaztelubira.multiplatform.model.GBTeam
import com.sgale.gaztelubira.multiplatform.ui.auth.login.LoginUiState
import com.sgale.gaztelubira.multiplatform.ui.auth.signup.SignUpUiState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.MatchDetailUiState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailInformation
import com.sgale.gaztelubira.multiplatform.ui.detail.player.PlayerDetailUiState
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab
import com.sgale.gaztelubira.multiplatform.ui.home.HomeUiState
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.matches.MatchesUiState
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.season.SeasonUiState
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.StatsUiState
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.StatsSeason
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.team.TeamUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamUiState
import com.sgale.gaztelubira.multiplatform.ui.splash.SplashUiState
import com.sgale.gaztelubira.multiplatform.model.GBSeason as SeasonSummary
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBSeason as StatsSeasons

/**
 * Kotlin default arguments do not survive the trip to Swift: every constructor shows up there with
 * all of its parameters required. These hand Swift the same starting state the Android view models
 * get from `XUiState()`, and from there Swift only ever copies.
 */
fun splashUiState(): SplashUiState = SplashUiState()

fun loginUiState(): LoginUiState = LoginUiState()

fun signUpUiState(): SignUpUiState = SignUpUiState()

fun homeUiState(selectedTab: HomeTab): HomeUiState = HomeUiState(selectedTab = selectedTab)

fun seasonUiState(): SeasonUiState = SeasonUiState()

fun teamUiState(): TeamUiState = TeamUiState()

fun matchesUiState(): MatchesUiState = MatchesUiState()

fun statsUiState(): StatsUiState = StatsUiState()

fun matchDetailUiState(team: GBTeam): MatchDetailUiState = MatchDetailUiState(team = team)

fun playerDetailUiState(): PlayerDetailUiState = PlayerDetailUiState()

fun insertPlayerUiState(
    playerId: String,
    positions: List<String>
): InsertPlayerUiState = InsertPlayerUiState(playerId = playerId, positions = positions)

fun insertTeamUiState(teamId: String): InsertTeamUiState = InsertTeamUiState(teamId = teamId)

/**
 * Two classes are called `GBSeason` — the season summary of the home tab and the season picker of
 * the stats tab — and Swift tells them apart by a suffix that depends on export order. Built here,
 * Swift never has to spell either name.
 */
fun seasonSummary(
    points: Int,
    games: Int,
    wins: Int,
    draws: Int,
    loses: Int,
    goalsFor: Int,
    goalsAgainst: Int,
    currentStreak: Int,
    lastGames: List<GBMatchResult>
): SeasonSummary = SeasonSummary(
    points = points,
    games = games,
    wins = wins,
    draws = draws,
    loses = loses,
    goalsFor = goalsFor,
    goalsAgainst = goalsAgainst,
    currentStreak = currentStreak,
    lastGames = lastGames
)

fun statsSeasons(
    selectedSeason: StatsSeason,
    seasons: List<StatsSeason>
): StatsSeasons = StatsSeasons(selectedSeason = selectedSeason, seasons = seasons)

fun emptyStatsSeasons(): StatsSeasons = StatsSeasons.EMPTY_SEASONS

/**
 * `description` collides with `NSObject.description` on the way to Swift, so it is taken here
 * under another name.
 */
fun matchDetailInformation(
    local: GBTeam,
    visitor: GBTeam,
    date: Long,
    text: String?,
    location: String?
): MatchDetailInformation = MatchDetailInformation(
    local = local,
    visitor = visitor,
    date = date,
    description = text,
    location = location
)

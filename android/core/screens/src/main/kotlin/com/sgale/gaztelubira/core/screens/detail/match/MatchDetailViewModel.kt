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

package com.sgale.gaztelubira.core.screens.detail.match

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.common.utils.TAG
import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchDetail
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchLineupPlayer
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchPlayerState
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchPlayerState.BENCH
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchPlayerState.MANAGER
import com.sgale.gaztelubira.core.domain.migration.model.player.Player
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.migration.model.team.TeamFormation
import com.sgale.gaztelubira.core.domain.migration.model.team.TeamFormation.FIVE_THREE_TWO
import com.sgale.gaztelubira.core.domain.migration.model.team.TeamFormation.FOUR_FOUR_TWO
import com.sgale.gaztelubira.core.domain.migration.usecase.GetMatchDetail
import com.sgale.gaztelubira.core.domain.migration.usecase.GetMatchPlayerStats
import com.sgale.gaztelubira.core.domain.migration.usecase.GetPlayers
import com.sgale.gaztelubira.core.domain.migration.usecase.GetTeam
import com.sgale.gaztelubira.core.domain.utils.GazteluBiraUtils.GAZTELU_BIRA
import com.sgale.gaztelubira.core.screens.toGBTeam
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBTeam
import com.sgale.gaztelubira.multiplatform.ui.detail.match.MatchDetailUiState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailInformation
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailLineUp
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Lineup
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class MatchDetailViewModel @Inject constructor(
    private val getPlayers: GetPlayers,
    private val getMatchDetail: GetMatchDetail,
    private val getMatchPlayerStats: GetMatchPlayerStats,
    private val getTeam: GetTeam
) : ViewModel() {
    private val _state = MutableStateFlow(MatchDetailUiState(team = GAZTELU_BIRA.toGBTeam()))
    internal val state: StateFlow<MatchDetailUiState> = _state

    internal fun changeUiState(state: MatchDetailState) {
        _state.update { it.copy(uiState = state) }
    }

    internal fun loadMatch(
        matchId: MatchId,
        onMatchNotFound: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                _state.value = withContext(IO) { loadState(matchId) }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading match: $e")
                onMatchNotFound()
            }
        }
    }

    private suspend fun loadState(matchId: MatchId): MatchDetailUiState {
        val players = getPlayers().associateBy { it.id }
        val matchDetail = getMatchDetail(matchId)
        val match = matchDetail.match.match
        val localTeam = getTeam(match.localTeam).toGBTeam()
        val visitorTeam = getTeam(match.visitorTeam).toGBTeam()

        val information = matchDetail.toMatchDetailInformation(localTeam, visitorTeam)
        val lineUp = matchDetail.toMatchDetailLineUp(players)
        val stats = getMatchPlayerStats(matchId).toMatchDetailStats(players)

        return MatchDetailUiState(
            uiState = Lineup(lineUp),
            team = GAZTELU_BIRA.toGBTeam(),
            localTeam = localTeam,
            localGoals = match.score.local,
            visitorTeam = visitorTeam,
            visitorGoals = match.score.visitor,
            information = information,
            lineUp = lineUp,
            stats = stats
        )
    }

    private fun MatchDetail.toMatchDetailInformation(
        localTeam: GBTeam,
        visitorTeam: GBTeam
    ) = MatchDetailInformation(
        local = localTeam,
        visitor = visitorTeam,
        date = match.information.date,
        description = match.information.description,
        location = match.information.location
    )

    private fun MatchDetail.toMatchDetailLineUp(
        players: Map<PlayerId, Player>
    ) = MatchDetailLineUp(
        benchPlayers = this.players.toGBPlayers(BENCH, players),
        managers = this.players.toGBPlayers(MANAGER, players),
        matchFormation = match.match.formation.toLineUpFormation(),
        players = this.players
            .mapNotNull { p -> p.formationPosition?.let { it - 1 to p.playerId.toGBPlayer(players) } }
            .toMap()
    )

    private fun List<PlayerStats>.toMatchDetailStats(
        players: Map<PlayerId, Player>
    ): MatchDetailStats {
        fun playersWith(stat: (PlayerStats) -> Number) =
            filter { stat(it).toDouble() > 0 }.map { it.playerId.toGBPlayer(players) }

        return MatchDetailStats(
            assists = playersWith { it.assists },
            blueCards = playersWith { it.blueCards },
            cleanSheets = playersWith { it.cleanSheets },
            fails = playersWith { it.fails },
            goals = playersWith { it.goals },
            goalsProvoked = playersWith { it.goalsProvoked },
            penaltiesProvoked = playersWith { it.penaltiesProvoked },
            redCards = playersWith { it.redCards },
            saves = playersWith { it.saves },
            yellowCards = playersWith { it.yellowCards }
        )
    }

    private fun TeamFormation.toLineUpFormation(): LineUpFormation =
        when (this) {
            FOUR_FOUR_TWO -> LineUpFormation.FOUR_FOUR_TWO
            FIVE_THREE_TWO -> LineUpFormation.FIVE_THREE_TWO
        }

    private fun PlayerId.toGBPlayer(
        players: Map<PlayerId, Player>
    ): GBPlayer {
        val player = players[this]
        return GBPlayer(
            id = value,
            name = player?.name.orEmpty(),
            image = player?.faceImage
        )
    }

    private fun List<MatchLineupPlayer>.toGBPlayers(
        filterState: MatchPlayerState,
        players: Map<PlayerId, Player>
    ): List<GBPlayer> = filter { it.state == filterState }.map { it.playerId.toGBPlayer(players) }
}

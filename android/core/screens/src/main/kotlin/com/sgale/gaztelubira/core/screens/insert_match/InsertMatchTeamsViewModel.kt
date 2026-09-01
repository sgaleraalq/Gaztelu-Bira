package com.sgale.gaztelubira.core.screens.insert_match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.match.MatchType.Cup
import com.sgale.gaztelubira.core.domain.model.match.MatchType.League
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.model.team.TeamSide
import com.sgale.gaztelubira.core.domain.usecase.db.GetNumberOfJourneys
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchTeamsInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class InsertMatchTeamsViewModel @Inject constructor(
    private val getNumberOfJourneys: GetNumberOfJourneys
) : ViewModel() {
    private val _matchInformation = MutableStateFlow(InsertMatchTeamsInformation())
    val matchInformation = _matchInformation

    init {
        viewModelScope.launch {
            val numberOfJourneys = withContext(Dispatchers.IO) {
                getNumberOfJourneys()
            }

            _matchInformation.value = _matchInformation.value.copy(
                numberOfJourneys = numberOfJourneys,
            )

            _matchInformation.value = _matchInformation.value.copy(
                matchName = getLeagueJourney(),
            )
        }
    }

    fun changeGoals(side: TeamSide, goals: Int?) {
        val goals = goals ?: -1
        if (goals == -1 || goals in 0..15) {
            _matchInformation.value = _matchInformation.value.copy(
                localGoals = if (side == TeamSide.Local) goals else _matchInformation.value.localGoals,
                visitorGoals = if (side == TeamSide.Local) _matchInformation.value.visitorGoals else goals
            )
        }
    }

    fun changeLocal() {
        val isLocal = _matchInformation.value.appTeamLocal
        val visitorTeam = _matchInformation.value.visitor
        val localTeam = _matchInformation.value.local

        _matchInformation.value = _matchInformation.value.copy(
            appTeamLocal = !isLocal,
            visitor = localTeam,
            local = visitorTeam
        )
    }

    fun updateLocalTeam(
        appTeam: TeamModel?,
        journeyName: String,
        cupName: String
    ) {
        if (_matchInformation.value.local != null) {
            return
        }

        _matchInformation.value = _matchInformation.value.copy(
            journeyName = journeyName,
            cupName = cupName
        )

        _matchInformation.value = _matchInformation.value.copy(
            matchName = getLeagueJourney(),
            local = appTeam
        )
    }

    fun updateMatchType() {
        val matchType = if (_matchInformation.value.matchType == League) {
            Cup
        } else {
            League
        }

        val cupName = _matchInformation.value.cupName

        _matchInformation.value = _matchInformation.value.copy(
            matchType = matchType,
            matchName = when (matchType) {
                League -> getLeagueJourney()
                Cup -> cupName
            }
        )
    }

    fun updateSelectedTeam(team: TeamModel) {
        when (_matchInformation.value.appTeamLocal) {
            true -> visitorSelected(team)
            false -> localSelected(team)
        }
    }

    private fun getLeagueJourney(): String {
        val jName = _matchInformation.value.journeyName
        val nJourneys = _matchInformation.value.numberOfJourneys

        return "$jName ${nJourneys + 1}"
    }

    private fun localSelected(team: TeamModel) {
        _matchInformation.value = _matchInformation.value.copy(
            local = team
        )
    }

    private fun visitorSelected(team: TeamModel) {
        _matchInformation.value = _matchInformation.value.copy(
            visitor = team
        )
    }
}
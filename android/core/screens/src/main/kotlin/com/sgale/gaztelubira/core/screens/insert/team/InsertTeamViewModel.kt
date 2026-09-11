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

package com.sgale.gaztelubira.core.screens.insert.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult.TeamInserted
import com.sgale.gaztelubira.core.domain.usecase.firestore.insert.InsertNewTeam
import com.sgale.gaztelubira.core.domain.utils.CommonImage
import com.sgale.gaztelubira.core.screens.navigation.Destination.Home
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.System.currentTimeMillis
import javax.inject.Inject

@HiltViewModel
internal class InsertTeamViewModel @Inject constructor(
    private val insertNewTeam: InsertNewTeam
): ViewModel() {
    private val initialState = InsertTeamUiState(teamId = currentTimeMillis().toString())
    private val _state = MutableStateFlow(initialState)
    internal val state: StateFlow<InsertTeamUiState> = _state

    private val _validInformation = MutableStateFlow(true)
    val validInformation: StateFlow<Boolean> = _validInformation


    internal fun insertTeam(
        team: TeamModel
    ) {
        if (!validInformation()) {
            _validInformation.value = false
            return
        }

        viewModelScope.launch {
            loading()
            val result = withContext(Dispatchers.IO) {
                insertNewTeam(img, teamName, teamId) {
                    showToast(errorMsg)
                }
            }

            if (result is TeamInserted) {
                state.navigateTo(Home, true)
            } else {
                _loading.value = false
            }
        }
    }

    fun updateName(newName: String) {
        _data.value = _data.value.copy(teamName = newName)
    }

    fun updatePicture(newPicture: CommonImage?) {
        _data.value = _data.value.copy(img = newPicture)
    }

    private fun loading() {
        _state.update { it.copy(state = Loading) }
    }
}

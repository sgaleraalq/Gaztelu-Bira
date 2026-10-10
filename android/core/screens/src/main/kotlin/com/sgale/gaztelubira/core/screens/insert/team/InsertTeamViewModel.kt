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

package com.sgale.gaztelubira.core.screens.insert.team

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.AlreadyExists
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.NoSeasonSelected
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.Unknown
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Inserted.Document
import com.sgale.gaztelubira.core.domain.usecase.InsertTeam
import com.sgale.gaztelubira.core.screens.showToast
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Default
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Loading
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField.TeamImage
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField.TeamName
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.error_inserting_team
import com.sgale.gaztelubira.multiplatform.ui.resources.error_inserting_team_reason
import com.sgale.gaztelubira.multiplatform.ui.resources.error_no_season_selected
import com.sgale.gaztelubira.multiplatform.ui.resources.error_team_already_exists
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import java.lang.System.currentTimeMillis
import javax.inject.Inject

@HiltViewModel
internal class InsertTeamViewModel @Inject constructor(
    private val insertTeam: InsertTeam
) : ViewModel() {
    private val initialState = InsertTeamUiState(teamId = getCurrentTimeId())
    private val _state = MutableStateFlow(initialState)
    internal val state: StateFlow<InsertTeamUiState> = _state.asStateFlow()

    internal fun updateField(field: InsertTeamField) {
        when (field) {
            is TeamName -> onNameChanged(field.name)
            is TeamImage -> onImageChanged(field.image)
        }
    }

    private fun onNameChanged(newName: String) {
        _state.update { it.copy(teamName = newName) }
    }

    private fun onImageChanged(newImage: String?) {
        if (newImage.isNullOrBlank()) {
            _state.update { it.copy(teamImage = "") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(teamImage = newImage) }
        }
    }

    internal fun insertTeam(
        context: Context,
        onSuccess: () -> Unit
    ) {
        val team = _state.value
        if (!team.handler.isValid) return

        viewModelScope.launch {
            _state.update { it.copy(state = Loading) }

            when (val result = insertTeam(team.teamName, team.teamImage.toUri())) {
                is Document -> onSuccess()
                else -> onFailure(context, result.errorMessage())
            }
        }
    }

    private fun getCurrentTimeId(): String =
        currentTimeMillis().toString()

    private suspend fun FirebaseInsertResult.errorMessage(): String =
        when (this) {
            is AlreadyExists -> getString(Res.string.error_team_already_exists)
            is NoSeasonSelected -> getString(Res.string.error_no_season_selected)
            is Unknown -> if (message.isNotBlank()) {
                getString(Res.string.error_inserting_team_reason, message)
            } else {
                getString(Res.string.error_inserting_team)
            }
            else -> getString(Res.string.error_inserting_team)
        }

    /**
     * Keeps the filled fields so the user can fix them and retry.
     */
    private fun onFailure(
        context: Context,
        errorMsg: String
    ) {
        showToast(context, errorMsg)
        _state.update { it.copy(state = Default) }
    }
}

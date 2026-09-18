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

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.team.Team
import com.sgale.gaztelubira.core.domain.repository.firestore.IInsert.FirebaseInsertResult.TeamInserted
import com.sgale.gaztelubira.core.domain.usecase.firestore.insert.InsertNewTeam
import com.sgale.gaztelubira.core.domain.utils.IImageValidator
import com.sgale.gaztelubira.core.screens.showToast
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Loading
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField.TeamImage
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField.TeamName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.System.currentTimeMillis
import javax.inject.Inject

@HiltViewModel
internal class InsertTeamViewModel @Inject constructor(
    private val insertNewTeam: InsertNewTeam,
    private val imageValidator: IImageValidator
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
            val validImage = imageValidator.isValidImage(newImage)
            if (validImage) {
                _state.update { it.copy(teamImage = newImage) }
            }
        }
    }

    internal fun insertTeam(
        context: Context,
        onSuccess: () -> Unit,
        errorMsg: String
    ) {
        val team = _state.value

        if (!team.handler.isValid) return

        viewModelScope.launch {
            _state.update { it.copy(state = Loading) }

            val result = withContext(Dispatchers.IO) {
                insertNewTeam(
                    img = team.teamImage,
                    team = team.toTeamModel(),
                    onFailure = { onFailure(context, errorMsg) }
                )
            }

            if (result is TeamInserted) {
                onSuccess()
            } else {
                onFailure(context, errorMsg)
            }
        }
    }

    private fun getCurrentTimeId(): String =
        currentTimeMillis().toString()

    private fun onFailure(
        context: Context,
        errorMsg: String
    ) {
        showToast(context, errorMsg)
        resetUiState()
    }

    private fun resetUiState() { _state.update { initialState } }

    private fun InsertTeamUiState.toTeamModel() =
        Team(
            id = teamId,
            name = teamName,
            logo = teamImage
        )
}

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
import com.sgale.gaztelubira.core.domain.utils.CommonImage.FromGallery
import com.sgale.gaztelubira.core.screens.navigation.Destination.Home
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField.TeamImage
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField.TeamName
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamState.Default
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamState.InvalidInformation
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamState.Loading
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
    private val insertNewTeam: InsertNewTeam
) : ViewModel() {

    private val _state = MutableStateFlow(InsertTeamUiState(teamId = currentTimeMillis().toString()))
    internal val state: StateFlow<InsertTeamUiState> = _state.asStateFlow()

    /**
     * The shared state only carries the uri the UI draws, so the picked image is kept here with the
     * platform shape the upload needs.
     */
    private var selectedImage: CommonImage? = null

    internal fun updateField(field: InsertTeamField) {
        when (field) {
            is TeamName -> onNameChanged(field.newName)
            is TeamImage -> onImageChanged(field.newImage)
        }
    }

    /**
     * Editing the name clears the last validation failure, but never interrupts an upload in flight.
     */
    private fun onNameChanged(newName: String) {
        _state.update { state ->
            state.copy(
                teamName = newName,
                state = if (state.state == InvalidInformation) Default else state.state
            )
        }
    }

    /**
     * The shared UI only ever clears the image, so anything else reaching here comes from a gallery
     * uri and is rebuilt as such.
     */
    private fun onImageChanged(newImage: String?) {
        onImagePicked(newImage?.takeIf { it.isNotBlank() }?.let { FromGallery(uri = it) })
    }

    internal fun onImagePicked(image: CommonImage?) {
        selectedImage = image
        _state.update { it.copy(teamImage = image?.uri.orEmpty()) }
    }

    internal fun insertTeam(
        navState: NavigationState,
        errorMsg: String
    ) {
        val team = _state.value

        if (team.teamName.isBlank()) {
            _state.update { it.copy(state = InvalidInformation) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(state = Loading) }

            val result = withContext(Dispatchers.IO) {
                insertNewTeam(
                    img = selectedImage,
                    team = team.toTeamModel(),
                    onFailure = {
//                        TODO
                        /* toastManager.showToast(errorMsg) */
                    }
                )
            }

            if (result is TeamInserted) {
                navState.navigateTo(Home, true)
            } else {
                _state.update { it.copy(state = Default) }
            }
        }
    }

    private fun InsertTeamUiState.toTeamModel() =
        TeamModel(
            id = teamId,
            name = teamName,
            logo = teamImage
        )
}

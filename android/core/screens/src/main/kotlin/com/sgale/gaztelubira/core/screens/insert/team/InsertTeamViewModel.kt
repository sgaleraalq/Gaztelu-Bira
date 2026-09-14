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
import com.sgale.gaztelubira.core.domain.utils.IImageValidator
import com.sgale.gaztelubira.core.screens.navigation.Destination.Home
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField.TeamImage
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField.TeamName
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Default
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Loading
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

    private var selectedImage: CommonImage? = null

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
        onImagePicked(
            image = newImage
                ?.takeIf { it.isNotBlank() }
                ?.let { FromGallery(uri = it) }
        )
    }

    internal fun onImagePicked(image: CommonImage?) {
        selectedImage = image

        val uri = image?.uri
        if (uri == null) {
            _state.update { it.copy(teamImage = "", validImage = false) }
            return
        }

        viewModelScope.launch {
            val validImage = imageValidator.isValidImage(uri)
            _state.update { it.copy(teamImage = uri, validImage = validImage) }
        }
    }

    internal fun insertTeam(
        navState: NavigationState
    ) {
        val team = _state.value

        /* The button is disabled while the handler reports a pending option; this only guards
           against an insert reaching here any other way. */
        if (!team.handler.isValid) return

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

    private fun getCurrentTimeId() =
        currentTimeMillis().toString()
}

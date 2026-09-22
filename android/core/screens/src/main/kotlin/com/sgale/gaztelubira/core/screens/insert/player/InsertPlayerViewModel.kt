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

package com.sgale.gaztelubira.core.screens.insert.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.legacy.model.player.Player
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position
import com.sgale.gaztelubira.core.domain.legacy.model.utils.PictureType.BODY
import com.sgale.gaztelubira.core.domain.legacy.model.utils.PictureType.FACE
import com.sgale.gaztelubira.core.domain.legacy.repository.firestore.IInsert.FirebaseInsertResult.PlayerInserted
import com.sgale.gaztelubira.core.domain.legacy.usecase.db.GetAvailableDorsals
import com.sgale.gaztelubira.core.domain.legacy.usecase.firestore.insert.InsertNewPlayer
import com.sgale.gaztelubira.core.screens.insert.manager.gallery.Gallery
import com.sgale.gaztelubira.core.screens.navigation.Destination.Home
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Default
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Loading
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Images
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Images.Body
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Images.Face
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.None
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Dorsal
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Image
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Name
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.System.currentTimeMillis
import javax.inject.Inject
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Position as PositionField

@HiltViewModel
internal class InsertPlayerViewModel @Inject constructor(
    private val insertNewPlayer: InsertNewPlayer,
    private val getAvailableDorsals: GetAvailableDorsals
) : ViewModel() {
    private var selectedPicture = FACE
    private val initialState = InsertPlayerUiState(
        playerId = getCurrentTimeId(),
        positions = Position.entries.map { it.name }
    )
    private val _state = MutableStateFlow(initialState)
    internal val state: StateFlow<InsertPlayerUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val dorsals = withContext(IO) { getAvailableDorsals() }
            _state.update { it.copy(availableDorsals = dorsals) }
        }
    }

    internal fun updateField(field: InsertPlayerField) {
        updateDialogState(None)
        when (field) {
            is Name -> _state.update { it.copy(playerName = field.name) }
            is Dorsal -> _state.update { it.copy(playerDorsal = field.dorsal) }
            is PositionField -> _state.update { it.copy(playerPosition = field.position) }
            is Image -> onImageChanged(newImage = field.image)
        }
    }

    internal fun updateDialogState(newState: InsertPlayerDialog) {
        _state.update { it.copy(dialog = newState) }
    }

    internal fun updateSelectedImage(
        image: Images,
        gallery: Gallery
    ) {
        selectedPicture = when (image) {
            Body -> BODY
            Face -> FACE
        }
        gallery.launch()
    }

    internal fun insertPlayer(
        navState: NavigationState,
        onFailure: () -> Unit
    ) {
        val player = _state.value
        if (!player.handler.isValid) return

        viewModelScope.launch {
            _state.update { it.copy(state = Loading) }

            val result = withContext(IO) {
                insertNewPlayer(player.toPlayerModel())
            }

            if (result is PlayerInserted) {
                navState.navigateTo(Home, true)
            } else {
                onFailure()
                _state.update { it.copy(state = Default) }
            }
        }
    }

    private fun onImageChanged(newImage: String?) {
        when (selectedPicture) {
            FACE -> { _state.update { it.copy(faceImage = newImage.orEmpty()) } }
            BODY -> { _state.update { it.copy(bodyImage = newImage.orEmpty()) } }
        }
    }

    private fun InsertPlayerUiState.toPlayerModel(): Player =
        Player(
            id = playerId,
            name = playerName,
            dorsal = playerDorsal,
            position = Position.valueOf(playerPosition),
            faceImage = faceImage,
            bodyImage = bodyImage
        )

    private fun getCurrentTimeId(): String = currentTimeMillis().toString()
}

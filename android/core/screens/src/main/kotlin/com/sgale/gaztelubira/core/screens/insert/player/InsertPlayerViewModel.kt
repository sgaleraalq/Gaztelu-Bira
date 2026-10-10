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

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.AlreadyExists
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.NoSeasonSelected
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Error.Unknown
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult.Inserted.Document
import com.sgale.gaztelubira.core.domain.model.other.PictureType.BODY
import com.sgale.gaztelubira.core.domain.model.other.PictureType.FACE
import com.sgale.gaztelubira.core.domain.model.player.Position
import com.sgale.gaztelubira.core.domain.usecase.GetAvailableDorsals
import com.sgale.gaztelubira.core.domain.usecase.InsertPlayer
import com.sgale.gaztelubira.core.navigation.Destination.Home
import com.sgale.gaztelubira.core.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.insert.media.gallery.Gallery
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
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Nickname
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.error_inserting_player_reason
import com.sgale.gaztelubira.multiplatform.ui.resources.error_no_season_selected
import com.sgale.gaztelubira.multiplatform.ui.resources.error_player_already_exists
import com.sgale.gaztelubira.multiplatform.ui.resources.upload_error_message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import java.lang.System.currentTimeMillis
import javax.inject.Inject
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField.Position as PositionField

@HiltViewModel
internal class InsertPlayerViewModel @Inject constructor(
    private val insertNewPlayer: InsertPlayer,
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
            val dorsals = getAvailableDorsals()
            _state.update { it.copy(availableDorsals = dorsals) }
        }
    }

    internal fun updateField(field: InsertPlayerField) {
        updateDialogState(None)
        when (field) {
            is Name -> _state.update { it.copy(playerName = field.name) }
            is Nickname -> _state.update { it.copy(playerNickname = field.nickname) }
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

    /**
     * @param onFailure Receives the message to show. The form keeps its fields so the user can retry.
     */
    internal fun insertPlayer(
        navState: NavigationState,
        onFailure: (String) -> Unit
    ) {
        val player = _state.value
        if (!player.handler.isValid) return

        viewModelScope.launch {
            _state.update { it.copy(state = Loading) }

            val result = insertNewPlayer(
                name = player.playerName,
                nickname = player.playerNickname,
                dorsal = player.playerDorsal,
                position = Position.mapPosition(player.playerPosition),
                faceImage = player.faceImage.ifBlank { null }?.toUri(),
                bodyImage = player.bodyImage.ifBlank { null }?.toUri()
            )

            if (result is Document) {
                navState.navigateTo(Home, true)
            } else {
                onFailure(result.errorMessage())
                _state.update { it.copy(state = Default) }
            }
        }
    }

    private suspend fun FirebaseInsertResult.errorMessage(): String =
        when (this) {
            is AlreadyExists -> getString(Res.string.error_player_already_exists)
            is NoSeasonSelected -> getString(Res.string.error_no_season_selected)
            is Unknown -> if (message.isNotBlank()) {
                getString(Res.string.error_inserting_player_reason, message)
            } else {
                getString(Res.string.upload_error_message)
            }
            else -> getString(Res.string.upload_error_message)
        }

    private fun onImageChanged(newImage: String?) {
        when (selectedPicture) {
            FACE -> { _state.update { it.copy(faceImage = newImage.orEmpty()) } }
            BODY -> { _state.update { it.copy(bodyImage = newImage.orEmpty()) } }
        }
    }

    private fun getCurrentTimeId(): String = currentTimeMillis().toString()
}

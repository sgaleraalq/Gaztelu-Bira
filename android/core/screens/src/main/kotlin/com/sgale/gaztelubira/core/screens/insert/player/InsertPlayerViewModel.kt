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

package com.sgale.gaztelubira.core.screens.insert.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.model.player.Position
import com.sgale.gaztelubira.core.domain.model.utils.PictureType.BODY
import com.sgale.gaztelubira.core.domain.model.utils.PictureType.FACE
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult.PlayerInserted
import com.sgale.gaztelubira.core.domain.usecase.db.GetAvailableDorsals
import com.sgale.gaztelubira.core.domain.usecase.firestore.insert.InsertNewPlayer
import com.sgale.gaztelubira.core.domain.utils.CommonImage
import com.sgale.gaztelubira.core.screens.navigation.Destination.Home
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Default
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Loading
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog
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
    private val initialState = InsertPlayerUiState(playerId = getCurrentTimeId())
    private val _state = MutableStateFlow(initialState)
    internal val state: StateFlow<InsertPlayerUiState> = _state.asStateFlow()

    /**
     * The picked images are kept aside rather than in the state: the state only carries the uri the
     * form needs to draw, while the upload needs the mime type that came with it.
     */
    private var faceImage: CommonImage? = null
    private var bodyImage: CommonImage? = null

    init {
        viewModelScope.launch {
            val dorsals = withContext(IO) { getAvailableDorsals() }
            _state.update { it.copy(availableDorsals = dorsals) }
        }
    }

    internal fun updateField(field: InsertPlayerField) {
        when (field) {
            is Name -> _state.update { it.copy(playerName = field.name) }
            is Dorsal -> _state.update { it.copy(playerDorsal = field.dorsal) }
            is PositionField -> _state.update { it.copy(playerPosition = field.position) }
            is Image -> onImageChanged(field.image)
        }
    }

    internal fun showDialog(dialog: InsertPlayerDialog) {
        _state.update { it.copy(dialog = dialog) }
    }

    /** Drops the picture into whichever box the user tapped before opening the source dialog. */
    internal fun onImagePicked(image: CommonImage?) {
        setImage(image)
    }

    internal fun insertPlayer(
        navState: NavigationState,
        onFailure: () -> Unit
    ) {
        val player = _state.value

        /* The button reports the missing field instead of inserting, so this only guards against
           an insert reaching here any other way. */
        if (!player.handler.isValid) return

        viewModelScope.launch {
            _state.update { it.copy(state = Loading) }

            val result = withContext(IO) {
                insertNewPlayer(
                    player = player.toPlayerModel(),
                    faceImg = faceImage,
                    bodyImg = bodyImage
                )
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
        setImage(
            image = newImage
                ?.takeIf { it.isNotBlank() }
                ?.let { CommonImage.FromGallery(uri = it) }
        )
    }

    private fun setImage(image: CommonImage?) {
        val uri = image?.uri.orEmpty()
        when (selectedPicture) {
            FACE -> {
                faceImage = image
                _state.update { it.copy(faceImage = uri) }
            }

            BODY -> {
                bodyImage = image
                _state.update { it.copy(bodyImage = uri) }
            }
        }
    }

    private fun InsertPlayerUiState.toPlayerModel(): Player =
        Player(
            id = playerId,
            name = playerName,
            dorsal = playerDorsal,
            position = Position.valueOf(playerPosition),
            faceImage = "",
            bodyImage = ""
        )

    private fun getCurrentTimeId(): String = currentTimeMillis().toString()
}

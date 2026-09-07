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


package com.sgale.gaztelubira.core.screens.review_photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.utils.CommonImage
import com.sgale.gaztelubira.core.domain.utils.SharedImagesBridge
import com.sgale.gaztelubira.multiplatform.ui.review_image.ReviewImageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * The preview is downscaled rather than shown at capture resolution: it only has to survive a
 * glance before the user accepts or repeats the shot.
 */
private const val MAX_PREVIEW_SIZE_PX = 1080
private const val PREVIEW_QUALITY = 85

@HiltViewModel
internal class ReviewImageViewModel @Inject constructor(
    private val imageLoader: SharedImagesBridge
) : ViewModel() {

    private val _state = MutableStateFlow(ReviewImageUiState())
    internal val state: StateFlow<ReviewImageUiState> = _state.asStateFlow()

    internal fun loadImage(commonImage: CommonImage, isFrontCamera: Boolean) {
        viewModelScope.launch {
            val image = withContext(Dispatchers.IO) {
                imageLoader.loadImage(
                    uri = commonImage.uri,
                    maxWidth = MAX_PREVIEW_SIZE_PX,
                    maxHeight = MAX_PREVIEW_SIZE_PX,
                    quality = PREVIEW_QUALITY,
                    isFrontCamera = isFrontCamera
                )
            }
            _state.update { it.copy(image = image) }
        }
    }
}

/*
 * Designed and developed by 2025 sgaleraalq (Sergio Galera)
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

package com.sgale.gaztelubira.core.domain.utils.camera

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.sgale.gaztelubira.core.domain.utils.takePhoto

const val CHANGE_CAMERA = "Change camera"
const val TAKE_PHOTO = "Take photo"

@Composable
fun GBCamera(
    modifier: Modifier = Modifier,
    controller: LifecycleCameraController,
    changeCamera: () -> CameraSelector,
    navigateBack: () -> Unit,
    navigateToReview: (Uri) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        GBCameraPreview(
            controller = controller,
            modifier = Modifier
                .fillMaxSize()
        )

        IconButton(
            modifier = Modifier.align(BottomEnd).padding(32.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = White
            ),
            onClick = {
                controller.cameraSelector = changeCamera()
            }
        ) {
            Icon(
                imageVector = Default.Cameraswitch,
                contentDescription = CHANGE_CAMERA
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(BottomCenter)
                .padding(32.dp),
            contentAlignment = Center
        ) {
            IconButton(
                modifier = Modifier.size(72.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = White
                ),
                onClick = {
                    takePhoto(
                        controller = controller,
                        onPhotoTaken = { navigateToReview(it) },
                        context = context,
                        closeCamera = { navigateBack() }
                    )
                }
            ) {
                Icon(
                    modifier = Modifier.size(56.dp),
                    imageVector = Default.Camera,
                    contentDescription = TAKE_PHOTO
                )
            }
        }
    }
}

@Composable
fun GBCameraPreview(
    modifier: Modifier = Modifier,
    controller: LifecycleCameraController
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier,
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        }
    )
}

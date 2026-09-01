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

package com.sgale.gaztelubira.core.domain.utils

import android.content.Context
import android.net.Uri
import android.net.Uri.fromFile
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileOptions.Builder
import androidx.camera.core.ImageCapture.OutputFileResults
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController.IMAGE_CAPTURE
import androidx.camera.view.LifecycleCameraController
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getMainExecutor
import com.sgale.gaztelubira.core.domain.utils.CommonImage.FromBackCamera
import com.sgale.gaztelubira.core.domain.utils.CommonImage.FromFrontCamera
import com.sgale.gaztelubira.core.domain.utils.camera.GBCamera
import java.io.File

// CameraManager.android.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraManagerCompose(
    key: String,
    navigateToReview: (CommonImage) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    val controller = remember {
        LifecycleCameraController(context).apply { setEnabledUseCases(IMAGE_CAPTURE) }
    }

    fun isBackCamera(): Boolean {
        return controller.cameraSelector == DEFAULT_BACK_CAMERA
    }

    fun changeCamera(): CameraSelector {
        return if (isBackCamera()) {
            DEFAULT_FRONT_CAMERA
        } else {
            DEFAULT_BACK_CAMERA
        }
    }

    BackHandler { navigateBack() }

    GBCamera(
        controller = controller,
        changeCamera = { changeCamera() },
        navigateBack = { navigateBack },
        navigateToReview = { uri ->
            val image = if (isBackCamera()) {
                FromBackCamera(uri.toString())
            } else {
                FromFrontCamera(uri.toString())
            }
            navigateToReview(image)
        }
    )
}

internal fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Uri) -> Unit,
    closeCamera: () -> Unit
) {
    val photoFile = File(
        context.cacheDir,
        "photo_${System.currentTimeMillis()}.jpg"
    )

    val output = Builder(photoFile).build()

    controller.takePicture(
        output,
        getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: OutputFileResults) {
                Log.i("Camera", "Photo capture succeeded: ${outputFileResults.savedUri}")
                onPhotoTaken(fromFile(photoFile))
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("Camera", "Couldn't save photo: ", exception)
                closeCamera()
            }
        }
    )
}

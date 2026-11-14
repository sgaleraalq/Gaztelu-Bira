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

package com.sgale.gaztelubira.multiplatform.ui.ios

import androidx.compose.ui.window.ComposeUIViewController
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerActions
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerView
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Images.Body
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog.Images.Face
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamActions
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamView
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField
import platform.UIKit.UIViewController

/**
 * The iOS counterpart of Android's `InsertPlayerScreen`.
 *
 * `updateField` and `openGallery` take sealed types on the shared side; here each of their cases
 * is a callback of its own, so the Swift view model receives plain values.
 *
 * @param onImageChanged Null when the picture is removed.
 */
fun insertPlayerViewController(
    state: IosState<InsertPlayerUiState>,
    onNameChanged: (String) -> Unit,
    onNicknameChanged: (String) -> Unit,
    onDorsalChanged: (Int) -> Unit,
    onPositionChanged: (String) -> Unit,
    onImageChanged: (String?) -> Unit,
    updateDialogState: (InsertPlayerDialog) -> Unit,
    openFaceGallery: () -> Unit,
    openBodyGallery: () -> Unit,
    insertPlayer: () -> Unit,
    onMissingField: (String) -> Unit
): UIViewController {
    val actions = InsertPlayerActions(
        updateField = { field ->
            when (field) {
                is InsertPlayerField.Name -> onNameChanged(field.name)
                is InsertPlayerField.Nickname -> onNicknameChanged(field.nickname)
                is InsertPlayerField.Dorsal -> onDorsalChanged(field.dorsal)
                is InsertPlayerField.Position -> onPositionChanged(field.position)
                is InsertPlayerField.Image -> onImageChanged(field.image)
            }
        },
        updateDialogState = updateDialogState,
        openGallery = { image ->
            when (image) {
                Face -> openFaceGallery()
                Body -> openBodyGallery()
            }
        },
        insertPlayer = insertPlayer,
        onMissingField = onMissingField
    )
    return ComposeUIViewController {
        GBTheme {
            InsertPlayerView(state.value, actions)
        }
    }
}

/**
 * The iOS counterpart of Android's `InsertTeamScreen`.
 *
 * @param onImageChanged Null when the logo is removed.
 */
fun insertTeamViewController(
    state: IosState<InsertTeamUiState>,
    onNameChanged: (String) -> Unit,
    onImageChanged: (String?) -> Unit,
    openGallery: () -> Unit,
    insertTeam: () -> Unit,
    onMissingField: (String) -> Unit
): UIViewController {
    val actions = InsertTeamActions(
        updateField = { field ->
            when (field) {
                is InsertTeamField.TeamName -> onNameChanged(field.name)
                is InsertTeamField.TeamImage -> onImageChanged(field.image)
            }
        },
        openGallery = openGallery,
        insertTeam = insertTeam,
        onMissingField = onMissingField
    )
    return ComposeUIViewController {
        GBTheme {
            InsertTeamView(state.value, actions)
        }
    }
}

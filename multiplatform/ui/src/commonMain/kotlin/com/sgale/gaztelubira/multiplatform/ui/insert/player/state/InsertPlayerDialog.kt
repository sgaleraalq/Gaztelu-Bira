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

package com.sgale.gaztelubira.multiplatform.ui.insert.player.state

/**
 * Which of the form's pickers is on screen. Kept apart from
 * [com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState] so that "a dialog is open"
 * and "the player is being uploaded" cannot overwrite one another.
 */
sealed interface InsertPlayerDialog {
    data object None : InsertPlayerDialog
    data object Capture : InsertPlayerDialog
    data object Dorsals : InsertPlayerDialog
    data object Positions : InsertPlayerDialog
}

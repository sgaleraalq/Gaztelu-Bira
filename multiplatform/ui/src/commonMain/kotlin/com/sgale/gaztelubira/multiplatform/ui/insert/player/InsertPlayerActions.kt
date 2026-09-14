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

package com.sgale.gaztelubira.multiplatform.ui.insert.player

import com.sgale.gaztelubira.multiplatform.ui.insert.InsertDataActions
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.player.state.InsertPlayerField

data class InsertPlayerActions(
    val updateField: (InsertPlayerField) -> Unit,
    val showDialog: (InsertPlayerDialog) -> Unit,
    val pickImage: () -> Unit,
    val takePicture: () -> Unit,
    val insertPlayer: () -> Unit,
    override val onMissingField: (String) -> Unit
) : InsertDataActions

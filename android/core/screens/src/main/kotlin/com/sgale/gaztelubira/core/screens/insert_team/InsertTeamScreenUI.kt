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

package com.sgale.gaztelubira.core.screens.insert_team

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.domain.utils.CommonImage
import com.sgale.gaztelubira.core.screens.insert_team.ui.InsertTeamButton
import com.sgale.gaztelubira.core.screens.insert_team.ui.InsertTeamImage
import com.sgale.gaztelubira.core.screens.insert_team.ui.InsertTeamName

@Composable
internal fun InsertTeamScreenUI(
    modifier: Modifier,
    data: InsertTeamData,
    loading: Boolean,
    validInformation: Boolean,
    updateName: (String) -> Unit,
    updatePicture: (CommonImage?) -> Unit,
    removeImage: () -> Unit,
    onInsert: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(16.dp)
    ) {
        InsertTeamName(data.teamName, loading, validInformation) { updateName(it) }
        InsertTeamImage(
            img = data.img,
            loading = loading,
            updatePicture = { updatePicture(it) },
            removeImage = { removeImage() }
        )
        Spacer(Modifier.weight(1f))
        InsertTeamButton(loading){ onInsert() }
    }
}

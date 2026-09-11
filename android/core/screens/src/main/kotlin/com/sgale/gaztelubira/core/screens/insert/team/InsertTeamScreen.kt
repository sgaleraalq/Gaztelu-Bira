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

package com.sgale.gaztelubira.core.screens.insert.team

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.screens.LocalMainViewModel
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamActions
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamView
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamState.Companion.isNotLoading

@Composable
internal fun InsertTeamScreen(
    navState: NavigationState,
    viewModel: InsertTeamViewModel = hiltViewModel<InsertTeamViewModel>()
) {
    val mainViewModel = LocalMainViewModel.current
    val user by mainViewModel.userSession.collectAsState()
    val validInformation by viewModel.validInformation.collectAsState()

    val errorMsg = stringResource(R.string.upload_error_message)

    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler(state.state.isNotLoading()) {
        navState.navigateBack()
    }

    val actions = remember(
        navState,
        viewModel
    ) {
        InsertTeamActions(
            updateField = {},
            insertTeam = {}
        )
    }

    InsertTeamView(
        state = state,
        actions = actions
    )

//    GBScaffold(
//        showTopAppBar = true,
//        title = stringResource(R.string.insert_new_team)
//    ) { modifier ->
//        InsertTeamScreenUI(
//            modifier = modifier,
//            data = data,
//            loading = loading,
//            validInformation = validInformation,
//            updateName = { viewModel.updateName(it) },
//            updatePicture = { viewModel.updatePicture(it) },
//            removeImage = { viewModel.updatePicture(null) },
//            onInsert = { viewModel.insertTeam(state, data.img, data.teamName, data.id, errorMsg) }
//        )
//    }
}

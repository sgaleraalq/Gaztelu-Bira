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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult.TeamInserted
import com.sgale.gaztelubira.core.domain.usecase.firestore.insert.InsertNewTeam
import com.sgale.gaztelubira.core.domain.utils.CommonImage
import com.sgale.gaztelubira.core.domain.utils.IToastManager
import com.sgale.gaztelubira.core.screens.navigation.Destination.Home
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class InsertTeamViewModel @Inject constructor(
    private val insertNewTeam: InsertNewTeam,
    private val toastManager: IToastManager
): ViewModel(), InsertTeamContractor.ViewModel {

    private val _data = MutableStateFlow(InsertTeamData())
    val data: StateFlow<InsertTeamData> = _data

    private val _loading = MutableStateFlow(false)
    val loading = _loading

    private val _validInformation = MutableStateFlow(true)
    val validInformation: StateFlow<Boolean> = _validInformation


    override fun insertTeam(
        state: NavigationState,
        img: CommonImage?,
        teamName: String,
        teamId: String,
        errorMsg: String
    ) {
        if (!validInformation()) {
            _validInformation.value = false
            return
        }

        viewModelScope.launch {
            _loading.value = true
            val result = withContext(Dispatchers.IO) {
                insertNewTeam(img, teamName, teamId) {
                    showToast(errorMsg)
                }
            }

            if (result is TeamInserted) {
                state.navigateTo(Home, true)
            } else {
                _loading.value = false
            }
        }
    }

    fun updateName(newName: String) {
        _data.value = _data.value.copy(teamName = newName)
    }

    fun updatePicture(newPicture: CommonImage?) {
        _data.value = _data.value.copy(img = newPicture)
    }

    private fun validInformation(): Boolean =
        _data.value.teamName.isNotBlank()

    private fun showToast(msg: String) {
        toastManager.showToast(msg)
    }
}

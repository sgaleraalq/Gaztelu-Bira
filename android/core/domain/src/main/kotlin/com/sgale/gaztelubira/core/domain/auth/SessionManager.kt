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

package com.sgale.gaztelubira.core.domain.auth

import com.sgale.gaztelubira.core.domain.auth.repository.AuthRepository
import com.sgale.gaztelubira.core.domain.model.user.UserId
import com.sgale.gaztelubira.core.domain.repository.UsersRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single source of truth for who is using the app. Any view model that cares about the user (or
 * only about whether they are an admin) injects this instead of having a screen hand it down.
 */
@Singleton
class SessionManager @Inject constructor(
    private val auth: AuthRepository,
    private val users: UsersRemote
) {
    private val _session = MutableStateFlow<UserSession?>(null)
    val session: StateFlow<UserSession?> = _session.asStateFlow()

    val isAdmin: Flow<Boolean> = _session
        .map { it?.isAdmin() == true }
        .distinctUntilChanged()

    fun isAuthenticated(): Boolean = auth.getUserSession() != null

    /**
     * Fetches the profile of whoever Firebase has signed in. Without one the session is cleared.
     */
    suspend fun load() {
        _session.value = auth.getUserSession()?.let { uid -> users.getUser(UserId(uid)) }
    }

    fun logout() {
        auth.logout()
        _session.value = null
    }
}

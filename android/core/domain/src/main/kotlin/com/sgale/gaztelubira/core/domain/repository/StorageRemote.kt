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

package com.sgale.gaztelubira.core.domain.repository

import android.net.Uri
import com.sgale.gaztelubira.core.domain.model.firebase.FirebaseInsertResult
import com.sgale.gaztelubira.core.domain.model.team.TeamId

interface StorageRemote {
    /**
     * @param id TeamId used as the logo's file name.
     * @param uri Uri of the image to be uploaded.
     */
    suspend fun insertTeamLogo(id: TeamId, uri: Uri): FirebaseInsertResult
}

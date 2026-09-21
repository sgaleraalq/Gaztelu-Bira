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

package com.sgale.gaztelubira.core.domain.model.team

import androidx.compose.runtime.Immutable
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId

@Immutable
data class Team(
    val id: FirebaseId,
    val name: String,
    val logo: String?
) {
    companion object {
        val ERROR_TEAM = Team(
            id = "error_team",
            name = "Error Team",
            logo = "https://firebasestorage.googleapis.com/v0/b/gbmultiplatform.firebasestorage.app/o/error_team.png?alt=media&token=8890839e-cc50-41db-a648-1502145e37d4"
        )
    }
}

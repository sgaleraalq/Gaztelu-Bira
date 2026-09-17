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

package com.sgale.gaztelubira.core.network.firebase.response.user

import com.sgale.gaztelubira.core.domain.model.user.UserModel
import com.sgale.gaztelubira.core.domain.model.user.UserRole.Companion.userRoleOf
import com.sgale.gaztelubira.core.network.NetworkMapper

internal object UserMapper: NetworkMapper<UserModel, UserResponse> {
    override fun UserModel.asResponse() =
        UserResponse(
            id = uid,
            name = name.orEmpty(),
            email = email.orEmpty(),
            role = role.name
        )

    override fun UserResponse.asModel() =
        UserModel(
            uid = id,
            name = name,
            email = email,
            img = img,
            role = userRoleOf(role)
        )
}

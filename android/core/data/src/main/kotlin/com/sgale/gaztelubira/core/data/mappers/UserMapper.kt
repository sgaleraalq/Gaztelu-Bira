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

package com.sgale.gaztelubira.core.data.mappers

import com.sgale.gaztelubira.core.data.db.entities.UserEntity
import com.sgale.gaztelubira.core.data.network.response.UserResponse
import com.sgale.gaztelubira.core.domain.model.user.UserModel
import com.sgale.gaztelubira.core.domain.model.user.UserRole.Companion.asRole

object UserMapper: Mapper<UserResponse, UserModel, UserEntity> {
    override fun asResponse(domain: UserModel) =
        UserResponse(
            id = domain.uid,
            name = domain.name ?: "",
            email = domain.email ?: "",
            role = domain.role.name
        )

    override fun asEntity(domain: UserModel) =
        UserEntity(
            id = domain.uid,
            name = domain.name ?: "",
            email = domain.email ?: "",
            img = domain.img,
            role = domain.role
        )

    override fun entityAsDomain(entity: UserEntity) =
        UserModel(
            uid = entity.id,
            name = entity.name,
            email = entity.email,
            img = entity.img,
            role = entity.role
        )

    override fun responseAsModel(response: UserResponse) =
        UserModel(
            uid = response.id,
            name = response.name,
            email = response.email,
            img = response.img,
            role = asRole(response.role)
        )
}

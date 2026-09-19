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

package com.sgale.gaztelubira.core.database.mappers

import com.sgale.gaztelubira.core.database.db.entities.UserEntity
import com.sgale.gaztelubira.core.domain.model.user.UserModel

object UserMapper: DatabaseMapper<UserModel, UserEntity> {
    override fun UserModel.asEntity() =
        UserEntity(
            id = uid,
            name = name.orEmpty(),
            email = email.orEmpty(),
            img = img,
            role = role,
        )

    override fun UserEntity.asModel() =
        UserModel(
            uid = id,
            name = name,
            email = email,
            img = img,
            role = role
        )
}

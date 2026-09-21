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

package com.sgale.gaztelubira.core.network.migration.firebase

import com.sgale.gaztelubira.core.domain.migration.model.FirebaseId

/**
 * Maps every object to each layer of the application
 * @param Model Domain layer representation
 * @param Response Data Response layer representation
 * @param Id The id of that model, so each mapper takes its own and no one has
 * to turn a general [FirebaseId] into the one it actually needs
 */
internal interface NetworkMapper <Model, Response, Id: FirebaseId> {
    fun Model.asResponse(): Response
    fun Response.asModel(id: Id): Model
}

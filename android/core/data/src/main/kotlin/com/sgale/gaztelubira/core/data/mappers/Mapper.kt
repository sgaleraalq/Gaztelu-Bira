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

/**
 * Maps every object to each layer of the application
 * @param Response Data layer representation
 * @param Domain Domain layer representation
 * @param Entity Cache layer representation
 */
interface Mapper <Response, Domain, Entity> {
    fun asResponse(domain: Domain): Response
    fun asEntity(domain: Domain): Entity?
    fun entityAsDomain(entity: Entity): Domain?
    fun responseAsModel(response: Response): Domain?
}

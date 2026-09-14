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

package com.sgale.gaztelubira.multiplatform.ui.insert.player.state

sealed interface InsertPlayerField {
    data class Name(
        val newName: String
    ) : InsertPlayerField

    data class Dorsal(
        val newDorsal: Int
    ) : InsertPlayerField

    data class Position(
        val newPosition: PlayerPosition
    ) : InsertPlayerField

    data class Image(
        val type: PictureType,
        val newImage: String?
    ) : InsertPlayerField

    /** Which box the next picked picture lands in. */
    data class SelectedPicture(
        val type: PictureType
    ) : InsertPlayerField

    companion object {
        internal fun emptyImage(type: PictureType) = Image(type, null)
    }
}

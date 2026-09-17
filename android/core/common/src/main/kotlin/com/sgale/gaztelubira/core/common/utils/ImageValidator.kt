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

package com.sgale.gaztelubira.core.common.utils

import android.content.Context
import androidx.core.net.toUri
import com.sgale.gaztelubira.core.domain.utils.IImageValidator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageValidator @Inject constructor(
    @param:ApplicationContext private val context: Context
) : IImageValidator {
    override suspend fun isValidImage(uri: String): Boolean =
        withContext(IO) {
            runCatching {
                context.contentResolver.openInputStream(uri.toUri())?.use { true } == true
            }.getOrDefault(false)
        }
}

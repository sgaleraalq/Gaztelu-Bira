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


package com.sgale.gaztelubira.multiplatform.ui.review_image

/**
 * [image] holds the decoded bytes, and stays null until they arrive. Equality on a [ByteArray] is
 * by identity, which is what we want here: the bytes are produced once and never rewritten, so a
 * new array always means a new image.
 */
data class ReviewImageUiState(
    val image: ByteArray? = null
)

/*
 * Designed and developed by 2025 sgaleraalq (Sergio Galera)
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

package com.sgale.gaztelubira.multiplatform.ui.auth.common

/**
 * iOS counterpart of the Android `Patterns.EMAIL_ADDRESS` check. There is no system-provided
 * equivalent worth the Foundation interop here, so the pattern is spelled out — deliberately
 * permissive, the same tradeoff Android's makes: it rejects obvious typos, and the only real
 * proof that an address exists is the confirmation email.
 */
private val EMAIL_PATTERN = Regex(
    "[a-zA-Z0-9+._%\\-]{1,256}@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
)

actual fun validEmail(email: String): Boolean =
    EMAIL_PATTERN.matches(email)

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

//import android.util.Patterns.EMAIL_ADDRESS

typealias Email = String
typealias Password = String

fun validEmail(email: String): Boolean =
    true
//    EMAIL_ADDRESS.matcher(email).matches() TODO

fun Email.valid() = isNotBlank() && validEmail(this)

fun Password.blank() = isBlank()
fun Password.short() = length < 8
fun Password.noDigits() = !this.any { it.isDigit() }
fun Password.mismatch(repeatPassword: String) = this != repeatPassword

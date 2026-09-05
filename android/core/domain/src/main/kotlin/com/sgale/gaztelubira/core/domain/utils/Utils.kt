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

package com.sgale.gaztelubira.core.domain.utils

import android.os.Build.VERSION_CODES.O
import androidx.annotation.RequiresApi
import java.text.DecimalFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

const val DATE_FORMAT = "dd/MM/yyyy"

typealias Email = String
typealias Password = String

fun formatDecimal(value: Double?, pattern: String = "#.##"): String =
    DecimalFormat(pattern).format(value)

fun generateRandomUUID(): String =
    UUID.randomUUID().toString()

fun getActualTimeAsLong(): Long = System.currentTimeMillis()

@RequiresApi(O)
fun getDateFromLong(format: String, date: Long): String {
    val formatter = DateTimeFormatter.ofPattern(format)
    return Instant.ofEpochMilli(date)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(formatter)
}

@RequiresApi(O)
fun Long.toDate() = getDateFromLong(DATE_FORMAT, this)

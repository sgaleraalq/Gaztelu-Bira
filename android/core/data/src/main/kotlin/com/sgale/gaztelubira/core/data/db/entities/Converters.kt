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

package com.sgale.gaztelubira.core.data.db.entities

import androidx.room.TypeConverter
import com.sgale.gaztelubira.core.domain.model.player.Stats
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @TypeConverter
    fun fromPlayersMap(value: Map<Int, FirebaseId?>?): String? {
        if (value == null) return null
        val serializer = MapSerializer(Int.serializer(), String.serializer().nullable)
        return json.encodeToString(serializer, value)
    }

    @TypeConverter
    fun toPlayersMap(value: String?): Map<Int, FirebaseId?>? {
        if (value == null) return null
        val serializer = MapSerializer(Int.serializer(), String.serializer().nullable)
        return json.decodeFromString(serializer, value)
    }

    @TypeConverter
    fun fromPlayersList(value: List<FirebaseId>) =
        json.encodeToString(ListSerializer(String.serializer()), value)

    @TypeConverter
    fun toPlayersList(value: String) =
        json.decodeFromString(ListSerializer(String.serializer()), value)

    @TypeConverter
    fun fromStatsMatchEntity(stats: StatsMatchEntity?) =
        stats?.let { json.encodeToString(it) }

    @TypeConverter
    fun toStatsMatch(value: String?): StatsMatchEntity? =
        value?.let { json.decodeFromString<StatsMatchEntity>(it) }

    @TypeConverter
    fun fromStatsMap(stats: Map<FirebaseId, Stats>?): String? {
        if (stats == null) return null
        val serializer = MapSerializer(String.serializer(), Stats.serializer())
        return json.encodeToString(serializer, stats)
    }

    @TypeConverter
    fun toStatsMap(value: String?): Map<FirebaseId, Stats>? {
        if (value == null) return null
        val serializer = MapSerializer(String.serializer(), Stats.serializer())
        return json.decodeFromString(serializer, value)
    }
}

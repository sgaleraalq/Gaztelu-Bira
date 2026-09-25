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

package com.sgale.gaztelubira.core.database.migration.preferences

import android.content.SharedPreferences
import android.content.SharedPreferences.*
import androidx.core.content.edit
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.migration.repository.preferences.Preferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val SELECTED_SEASON = "gbmultiplatform.selectedSeason"

internal class PreferencesImpl @Inject constructor(
    private val settings: SharedPreferences
) : Preferences {

    override val selectedSeason: Flow<SeasonId?> = callbackFlow {
        send(settings.readSelectedSeason())

        val listener = OnSharedPreferenceChangeListener { preferences, key ->
            if (key == SELECTED_SEASON) trySend(preferences.readSelectedSeason())
        }

        settings.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { settings.unregisterOnSharedPreferenceChangeListener(listener) }
    }.distinctUntilChanged()

    override suspend fun selectSeason(season: SeasonId?) {
        settings.edit {
            if (season == null) remove(SELECTED_SEASON) else putString(SELECTED_SEASON, season.value)
        }
    }

    private fun SharedPreferences.readSelectedSeason(): SeasonId? =
        getString(SELECTED_SEASON, null)?.let(::SeasonId)
}

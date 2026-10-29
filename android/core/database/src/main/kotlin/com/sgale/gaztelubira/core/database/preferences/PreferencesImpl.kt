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

package com.sgale.gaztelubira.core.database.preferences

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.core.content.edit
import com.sgale.gaztelubira.core.domain.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.repository.Preferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val SP_LAST_SYNC = "gbmultiplatform.lastSync"
private const val SP_SELECTED_SEASON = "gbmultiplatform.selectedSeason"

internal class PreferencesImpl @Inject constructor(
    private val settings: SharedPreferences
) : Preferences {
    override val selectedSeason: Flow<SeasonId?> = observe(SP_SELECTED_SEASON) { readSelectedSeason() }

    override val lastSync: Flow<Long?> = observe(SP_LAST_SYNC) { readLastSync() }

    override suspend fun resolveSelectedSeason(): SeasonId? = selectedSeason.first()

    override suspend fun selectSeason(season: SeasonId?) {
        settings.edit {
            if (season == null) remove(SP_SELECTED_SEASON) else putString(SP_SELECTED_SEASON, season.value)
        }
    }

    override suspend fun setLastSync(time: Long) {
        settings.edit { putLong(SP_LAST_SYNC, time) }
    }

    /**
     * What [read] gives now, and again every time [key] changes.
     */
    private fun <T> observe(key: String, read: SharedPreferences.() -> T): Flow<T> =
        callbackFlow {
            send(settings.read())

            val listener = OnSharedPreferenceChangeListener { preferences, changed ->
                if (changed == key) trySend(preferences.read())
            }

            settings.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { settings.unregisterOnSharedPreferenceChangeListener(listener) }
        }.distinctUntilChanged()

    private fun SharedPreferences.readLastSync(): Long? =
        if (contains(SP_LAST_SYNC)) getLong(SP_LAST_SYNC, 0L) else null

    private fun SharedPreferences.readSelectedSeason(): SeasonId? =
        getString(SP_SELECTED_SEASON, null)?.let(::SeasonId)
}

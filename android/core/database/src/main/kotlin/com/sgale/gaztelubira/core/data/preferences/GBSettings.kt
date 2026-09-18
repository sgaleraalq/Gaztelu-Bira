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

package com.sgale.gaztelubira.core.data.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseCollection
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseTimestamp
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences.Companion.IS_FIRST_TIME
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences.Companion.SEASON
import javax.inject.Inject

class GBSettings @Inject constructor(
    private val settings: SharedPreferences
) : IGBPreferences {
    override fun getSeason(): String = settings.getString(SEASON, "").orEmpty()

    override fun getTimestamp(collection: FirebaseCollection) =
        settings.getLong(collection, 0L)

    override fun isFirstTime() = settings.getBoolean(IS_FIRST_TIME, true)

    override fun setFirstTime(value: Boolean) {
        settings.edit { putBoolean(IS_FIRST_TIME, value) }
    }

    override fun setTimestamp(timestamp: FirebaseTimestamp, collection: FirebaseCollection) {
        settings.edit { putLong(collection, timestamp) }
    }

    override fun setSeason(season: String) {
        settings.edit { putString(SEASON, season) }
    }

    override fun setRules() {
        // TODO
    }
}

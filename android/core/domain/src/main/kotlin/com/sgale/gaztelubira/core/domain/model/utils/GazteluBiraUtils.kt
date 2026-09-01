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

package com.sgale.gaztelubira.core.domain.model.utils

import com.sgale.gaztelubira.core.domain.model.team.TeamModel

object GazteluBiraUtils {
    const val TESTING = false
    const val GAZTELU_BIRA_ID = "gaztelu_bira"
    const val GAZTELU_BIRA_NAME = "Gaztelu Bira"
    const val GAZTELU_BIRA_LOGO =
        "https://firebasestorage.googleapis.com/v0/b/gbmultiplatform.firebasestorage.app/o/" +
            "img_gaztelu_bira.webp?alt=media&token=3708f1c5-f9d7-4353-8829-967b21df75ed"

    val GAZTELU_BIRA = TeamModel(
        GAZTELU_BIRA_ID, GAZTELU_BIRA_NAME, GAZTELU_BIRA_LOGO
    )
}

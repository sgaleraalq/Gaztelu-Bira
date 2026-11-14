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

package com.sgale.gaztelubira.multiplatform.ui.ios

import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_error_no_season_selected
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_player_error_already_exists
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_player_error_inserting_reason
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_player_upload_error_message
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_team_error_already_exists
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_team_error_inserting
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_team_error_inserting_reason
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

/**
 * The messages the Android view models read with `getString(Res.string...)`. The strings live in
 * the shared `composeResources`, which Swift cannot reach, so the iOS view models ask for them by
 * name.
 */
enum class IosMessage(
    internal val resource: StringResource
) {
    INSERT_NO_SEASON_SELECTED(Res.string.insert_error_no_season_selected),
    INSERT_PLAYER_ALREADY_EXISTS(Res.string.insert_player_error_already_exists),
    INSERT_PLAYER_ERROR_REASON(Res.string.insert_player_error_inserting_reason),
    INSERT_PLAYER_UPLOAD_ERROR(Res.string.insert_player_upload_error_message),
    INSERT_TEAM_ALREADY_EXISTS(Res.string.insert_team_error_already_exists),
    INSERT_TEAM_ERROR(Res.string.insert_team_error_inserting),
    INSERT_TEAM_ERROR_REASON(Res.string.insert_team_error_inserting_reason)
}

private val messagesScope = MainScope()

/**
 * Resolves [message] in the device language and hands it to [onResolved] on the main thread.
 *
 * A callback rather than a `suspend fun`: it keeps the Swift side free of the rules Kotlin puts on
 * suspend functions called from another language.
 *
 * @param argument Fills the `%1$s` of the messages that carry a reason; ignored by the rest.
 */
fun resolveMessage(
    message: IosMessage,
    argument: String?,
    onResolved: (String) -> Unit
) {
    messagesScope.launch {
        val text = if (argument == null) {
            getString(message.resource)
        } else {
            getString(message.resource, argument)
        }
        onResolved(text)
    }
}

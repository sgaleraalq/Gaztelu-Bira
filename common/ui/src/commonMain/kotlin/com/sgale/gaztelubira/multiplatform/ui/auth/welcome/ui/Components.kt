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

package com.sgale.gaztelubira.multiplatform.ui.auth.welcome.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTitle
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import `gaztelu bira`.common.ui.generated.resources.Res
import gaztelu.app_name
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WelcomeScreenTitle() {
    val welcomeString = stringResource(Res.string.welcome_to)
    val appName = stringResource(Res.string.app_name)

    GBTitle(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        title = "$welcomeString \n$appName"
    )
}

@Composable
fun WelcomeScreenSubtitle() {
    GBText(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        text = stringResource(Res.string.gaztelu_bira_welcome_text),
        alignment = Center,
        textColor = White,
        style = gBTypography().bodyMedium,
        maxLines = 4
    )
}

@Composable
fun WelcomeScreenImage(
    modifier: Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(Res.drawable.img_welcome_image),
            contentDescription = stringResource(Res.string.app_name),
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun WelcomeScreenButton(
    navigate: () -> Unit,
) {
    GBElevatedButton(
        modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp).fillMaxWidth(),
        text = stringResource(Res.string.join_gaztelu_bira),
        onClick = { navigate() },
        roundness = 32
    )
}

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

package com.sgale.gaztelubira.core.screens.auth.welcome.ui

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
import com.sgale.gaztelubira.core.designsystem.R as DesignSystemR
import com.sgale.gaztelubira.core.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.core.designsystem.components.GBText
import com.sgale.gaztelubira.core.designsystem.components.GBTitle
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sgale.gaztelubira.core.screens.R

@Composable
fun WelcomeScreenTitle() {
    GBTitle(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        title = "${stringResource(R.string.welcome_to)} \n${stringResource(DesignSystemR.string.app_name)}"
    )
}

@Composable
fun WelcomeScreenSubtitle() {
    GBText(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        text = stringResource(R.string.gaztelu_bira_welcome_text),
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
            painter = painterResource(R.drawable.img_welcome_image),
            contentDescription = stringResource(DesignSystemR.string.app_name),
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
        text = stringResource(R.string.join_gaztelu_bira),
        onClick = { navigate() },
        roundness = 32
    )
}

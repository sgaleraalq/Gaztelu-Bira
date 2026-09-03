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

package com.sgale.gaztelubira.multiplatform.ui

import androidx.compose.ui.window.ComposeUIViewController
import com.sgale.gaztelubira.multiplatform.ui.auth.welcome.WelcomeScreen
import platform.UIKit.UIViewController

/**
 * Entry point for the iOS app: a `@Composable` cannot cross into Objective-C, so the screen is
 * handed over wrapped in a `UIViewController` that SwiftUI hosts through
 * `UIViewControllerRepresentable`.
 *
 * Navigation stays with the caller — the same contract the Android side uses — so Swift decides
 * what "go to login" means.
 */
fun welcomeViewController(
    navigateToLogin: () -> Unit
): UIViewController = ComposeUIViewController {
    WelcomeScreen(navigateToLogin = navigateToLogin)
}

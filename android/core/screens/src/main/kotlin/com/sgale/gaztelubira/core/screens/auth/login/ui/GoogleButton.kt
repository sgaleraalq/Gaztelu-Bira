package com.sgale.gaztelubira.core.screens.auth.login.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import androidx.compose.ui.res.painterResource

@Composable
fun GoogleLogin(
    loginGoogle: () -> Unit
) {
    GBElevatedButton(
        text = stringResource(R.string.login_with_google),
        onClick = { loginGoogle() },
//        icon = painterResource(R.drawable.ic_google) TODO
    )
}

@Composable
fun ContinueAsGuest(
    onClick: () -> Unit
) {
    GBElevatedButton(
        text = stringResource(R.string.continue_as_guest),
        onClick = { onClick() }
    )
}

package com.sgale.gaztelubira.core.screens.auth.login.ui

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.auth.login.LoginUser
import com.sgale.gaztelubira.core.screens.auth.common.AuthTextField
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource

@Composable
fun LoginTextFields(
    user: LoginUser,
    changeEmail: (String) -> Unit,
    changePassword: (String) -> Unit,
    changePasswordVisibility: () -> Unit
) {
    Column(
        verticalArrangement = spacedBy(4.dp)
    ) {
        AuthTextField(
            text = user.email,
            label = stringResource(R.string.email_id),
            icon = R.drawable.ic_at_sign,
            onTextChanged = { changeEmail(it) }
        )
        AuthTextField(
            text = user.password,
            label = stringResource(R.string.password),
            icon = R.drawable.ic_padlock,
            isPassword = true,
            isPasswordVisible = user.isPasswordVisible,
            onTextChanged = { changePassword(it) },
            showPassword = { changePasswordVisibility() }
        )
    }
}

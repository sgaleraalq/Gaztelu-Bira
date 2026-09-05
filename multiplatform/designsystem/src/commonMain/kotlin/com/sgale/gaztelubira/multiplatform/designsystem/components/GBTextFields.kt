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

package com.sgale.gaztelubira.multiplatform.designsystem.components

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions.Companion.Default
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.ImeAction.Companion.Unspecified
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Sentences
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import androidx.compose.ui.text.input.KeyboardType.Companion.Number
import androidx.compose.ui.text.input.KeyboardType.Companion.Password
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation.Companion.None
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.primaryRed
import com.sgale.gaztelubira.multiplatform.designsystem.style.white_in_gray_box

@Composable
fun GBTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    style: TextStyle = gBTypography().bodyMedium,
    label: String = "",
    enabled: Boolean = true,
    trailingIcon: @Composable () -> Unit = {},
    isEmail: Boolean = false,
    isPassword: Boolean = false,
    error: Boolean = false,
    firstCap: Boolean = false,
    isNumeric: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        modifier = modifier,
        value = text,
        onValueChange = { onTextChanged(it) },
        enabled = enabled,
        textStyle = style,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Transparent,
            unfocusedIndicatorColor = white_in_gray_box,
            focusedContainerColor = Transparent,
            focusedIndicatorColor = white_in_gray_box,
            focusedTextColor = White,
            unfocusedTextColor = White,
            disabledContainerColor = Transparent,
            disabledIndicatorColor = Green,
            disabledTextColor = White,
            errorIndicatorColor = primaryRed,
            errorContainerColor = Transparent,
            errorTextColor = primaryRed
        ),
        label = {
            GBText(
                text = label,
                textColor = white_in_gray_box.copy(alpha = 0.5f),
                style = gBTypography().bodyMedium
            )
        },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = Default.copy(
            imeAction = Next,
            keyboardType = when {
                isPassword -> Password
                isEmail -> Email
                isNumeric -> Number
                else -> Text
            },
            capitalization = if (firstCap && !isPassword) Sentences else KeyboardCapitalization.None
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else None,
        keyboardActions = KeyboardActions(
            onNext = {
                keyboardController?.hide()
            }
        ),
        trailingIcon = trailingIcon,
        isError = error
    )
}


@Composable
fun GBBasicTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    style: TextStyle,
    isEmail: Boolean = false,
    isNumeric: Boolean = false,
    isPassword: Boolean = false,
    singleLine: Boolean = false,
    showDoneButton: Boolean = false,
    showNextLine: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChanged(it) },
        textStyle = style,
        keyboardOptions = Default.copy(
            imeAction = if (showDoneButton) Done else if (showNextLine) Unspecified else Next,
            keyboardType = when {
                isPassword -> Password
                isEmail -> Email
                isNumeric -> Number
                else -> Text
            },
            capitalization = Sentences
        ),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        singleLine = singleLine
    )
}

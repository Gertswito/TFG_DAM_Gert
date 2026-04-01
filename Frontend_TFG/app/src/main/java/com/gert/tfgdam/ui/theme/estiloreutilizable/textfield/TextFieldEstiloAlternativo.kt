package com.gert.tfgdam.ui.theme.estiloreutilizable.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun TextFieldEstiloAlternativo(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isEnabled: Boolean = true,
) {
    if (isPassword) {
        TextField(
            value = value,
            onValueChange = {
                if (it.length <= 150) onValueChange(it)
            },
            label = { Text(label) },
            modifier = modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            enabled = isEnabled,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                errorTextColor = MaterialTheme.colorScheme.onError,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                errorCursorColor = MaterialTheme.colorScheme.onError,
                focusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                errorIndicatorColor = MaterialTheme.colorScheme.onError,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                errorPlaceholderColor = MaterialTheme.colorScheme.onError
            )
        )
    } else {
        TextField(
            value = value,
            onValueChange = {
                if (it.length <= 150) onValueChange(it)
            },
            label = { Text(label) },
            modifier = modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            enabled = isEnabled,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                errorTextColor = MaterialTheme.colorScheme.onError,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                errorCursorColor = MaterialTheme.colorScheme.onError,
                focusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                errorIndicatorColor = MaterialTheme.colorScheme.onError,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                errorPlaceholderColor = MaterialTheme.colorScheme.onError
            )
        )
    }
}
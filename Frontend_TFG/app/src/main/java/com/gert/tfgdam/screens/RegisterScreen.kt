package com.gert.tfgdam.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gert.tfgdam.routes.Routes


@Composable
fun RegisterScreen( navController: NavController ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Crear cuenta",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.height(15.dp))

                var user by remember { mutableStateOf("") }
                TextFieldRegisterYLogin(
                    value = user,
                    onValueChange = { user = it },
                    label = "Nombre de usuario",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    var nombre by remember { mutableStateOf("") }
                    TextFieldRegisterYLogin(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = "Nombre",
                        modifier = Modifier.weight(1f)
                    )

                    var apellidos by remember { mutableStateOf("") }
                    TextFieldRegisterYLogin(
                        value = apellidos,
                        onValueChange = { apellidos = it },
                        label = "Apellidos",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                var correo by remember { mutableStateOf("") }
                TextFieldRegisterYLogin(
                    value = correo,
                    onValueChange = { correo = it },
                    label = "Correo electrónico",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                var contrasenha by remember { mutableStateOf("") }
                TextFieldRegisterYLogin(
                    value = contrasenha,
                    onValueChange = { contrasenha = it },
                    label = "Contraseña",
                    isPassword = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { navController.navigate(Routes.HOME) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                ) {
                    Text("Crear cuenta")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "¿Tiene una cuenta? Inicie sesión",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.clickable { navController.navigate(Routes.LOGIN) }
                )
            }
        }
    }
}

@Composable
fun TextFieldRegisterYLogin(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
) {
    if (isPassword) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = modifier,
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                errorTextColor = MaterialTheme.colorScheme.onError,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                errorCursorColor = MaterialTheme.colorScheme.onError,
                focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                errorIndicatorColor = MaterialTheme.colorScheme.onError,
                focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                errorPlaceholderColor = MaterialTheme.colorScheme.onError
            )
        )
    } else {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = modifier,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                errorTextColor = MaterialTheme.colorScheme.onError,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                errorCursorColor = MaterialTheme.colorScheme.onError,
                focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                errorIndicatorColor = MaterialTheme.colorScheme.onError,
                focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                errorPlaceholderColor = MaterialTheme.colorScheme.onError
            )
        )
    }
}
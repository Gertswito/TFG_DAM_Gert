package com.gert.tfgdam.feature.user.usersettings.contrasenha.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gert.tfgdam.feature.admin.cliente.model.Cliente
import com.gert.tfgdam.feature.user.usersettings.contrasenha.viewmodel.UserContrasenhaViewModel
import com.gert.tfgdam.ui.theme.estiloreutilizable.textfield.TextFieldEstiloAlternativo

@Composable
fun UserContrasenhaScreen(
    usuario: Cliente,
    modifier: Modifier = Modifier,
    viewModel: UserContrasenhaViewModel = viewModel()
) {
    var expandedContrasenha by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { expandedContrasenha = !expandedContrasenha },
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
                text = "Contraseña",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 40.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            )

            AnimatedVisibility(
                visible = expandedContrasenha,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(15.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp, horizontal = 8.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.medium
                            ),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(20.dp))

                            TextFieldEstiloAlternativo(
                                value = viewModel.contrasenha,
                                onValueChange = { viewModel.contrasenha = it },
                                isPassword = true,
                                label = "Nueva contraseña",
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            TextFieldEstiloAlternativo(
                                value = viewModel.contrasenhaRepetida,
                                onValueChange = {
                                    viewModel.contrasenhaRepetida = it
                                },
                                isPassword = true,
                                label = "Repetir contraseña",
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )

                            Spacer(modifier = Modifier.height(25.dp))

                            Button(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                enabled = !viewModel.isLoading,
                                onClick = { viewModel.cambiarContrasenha(usuario) }
                            ) {
                                if (viewModel.isLoading) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Text(
                                        text = "CAMBIAR CONTRASEÑA",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            if (viewModel.errorMessage !== "") {
                                Text(
                                    text = viewModel.errorMessage,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            if (viewModel.successMessage !== "") {
                                Text(
                                    text = viewModel.successMessage,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                }
            }
        }
    }
}
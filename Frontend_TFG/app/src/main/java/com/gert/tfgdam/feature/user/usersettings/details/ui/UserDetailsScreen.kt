package com.gert.tfgdam.feature.user.usersettings.details.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.gert.tfgdam.feature.user.usersettings.viewmodel.UserSettingsViewModel
import com.gert.tfgdam.ui.theme.estiloreutilizable.textfield.TextFieldEstiloAlternativo

@Composable
fun UserDetailsScreen (
    usuario: Cliente,
    modifier: Modifier = Modifier,
    viewModel: UserSettingsViewModel = viewModel()
) {
    var expandedInformacion by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { expandedInformacion = !expandedInformacion },
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
                text = "Información",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(
                visible = expandedInformacion,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(25.dp))

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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = ("Nombre de usuario: "),
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                            )

                            Text(
                                text = (usuario.usuario ?: "N/A"),
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                            )

                            if (!viewModel.isEditarClicked) {
                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    text = ("Email: "),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                )

                                Text(
                                    text = (usuario.email ?: "N/A"),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 14.sp,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    text = ("Nombre: "),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                )

                                Text(
                                    text = (usuario.nombre ?: "N/A"),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    text = ("Apellidos: "),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                )

                                Text(
                                    text = (usuario.apellidos ?: "N/A"),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                )

                                Spacer(modifier = Modifier.height(25.dp))

                                Button(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    onClick = { viewModel.clickEditarOCancelar(usuario) }
                                ) {
                                    Text(
                                        text = "EDITAR DATOS",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            } else {
                                Text(
                                    text = "El nombre de usuario no se puede cambiar",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(
                                        horizontal = 15.dp
                                    )
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                TextFieldEstiloAlternativo(
                                    value = viewModel.emailCambiado,
                                    onValueChange = {
                                        viewModel.emailCambiado = it
                                    },
                                    label = "Email",
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                TextFieldEstiloAlternativo(
                                    value = viewModel.nombreCambiado,
                                    onValueChange = {
                                        viewModel.nombreCambiado = it
                                    },
                                    label = "Nombre",
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                TextFieldEstiloAlternativo(
                                    value = viewModel.apellidosCambiados,
                                    onValueChange = {
                                        viewModel.apellidosCambiados = it
                                    },
                                    label = "Apellidos",
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                )

                                Spacer(modifier = Modifier.height(25.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Button(
                                        modifier = modifier
                                            .weight(1f)
                                            .padding(start = 20.dp, end = 5.dp),
                                        enabled = !viewModel.isLoadingEditar,
                                        onClick = { viewModel.editarFormulario(usuario) }
                                    ) {
                                        if (viewModel.isLoadingEditar) {
                                            CircularProgressIndicator(
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        } else {
                                            Text(
                                                text = "GUARDAR",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    Button(
                                        modifier = modifier
                                            .weight(1f)
                                            .padding(end = 20.dp, start = 5.dp),
                                        enabled = !viewModel.isLoadingEditar,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.secondary
                                        ),
                                        onClick = { viewModel.clickEditarOCancelar(usuario) }
                                    ) {
                                        Text(
                                            text = "SALIR",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                }

                                if (viewModel.errorMessageEditar !== "") {
                                    Text(
                                        text = viewModel.errorMessageEditar,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }

                                if (viewModel.successMessageEditar !== "") {
                                    Text(
                                        text = viewModel.successMessageEditar,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                }
            }
        }
    }
}
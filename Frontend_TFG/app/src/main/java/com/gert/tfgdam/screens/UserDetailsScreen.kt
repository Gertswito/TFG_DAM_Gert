package com.gert.tfgdam.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import com.gert.tfgdam.model.Direccion
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.viewmodel.UserDetailsViewModel

@Composable
fun UserDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: UserDetailsViewModel = viewModel()
) {
    val context = LocalContext.current
    val usuario = viewModel.usuarioSesionEntero
    val isLoading = viewModel.isLoading

    LaunchedEffect(Unit) {
        viewModel.cargarUsuarioSesion(context)
    }

    when {
        isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        usuario == null -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se ha encontrado el usuario",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn {
                    item {
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
                                                    text = (usuario?.usuario ?: "N/A"),
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
                                                        text = (usuario?.email ?: "N/A"),
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
                                                        text = (usuario?.nombre ?: "N/A"),
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
                                                        text = (usuario?.apellidos ?: "N/A"),
                                                        color = MaterialTheme.colorScheme.onBackground,
                                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                                    )

                                                    Spacer(modifier = Modifier.height(25.dp))

                                                    Button(
                                                        modifier = modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 20.dp),
                                                        onClick = {
                                                            viewModel.clickEditarOCancelar(
                                                                usuario
                                                            )
                                                        }
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

                                                    TextFieldRegisterYLogin(
                                                        value = viewModel.emailCambiado,
                                                        onValueChange = {
                                                            viewModel.emailCambiado = it
                                                        },
                                                        label = "Email",
                                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                                    )

                                                    Spacer(modifier = Modifier.height(20.dp))

                                                    TextFieldRegisterYLogin(
                                                        value = viewModel.nombreCambiado,
                                                        onValueChange = {
                                                            viewModel.nombreCambiado = it
                                                        },
                                                        label = "Nombre",
                                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                                    )

                                                    Spacer(modifier = Modifier.height(20.dp))

                                                    TextFieldRegisterYLogin(
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
                                                            onClick = {
                                                                viewModel.editarFormulario(
                                                                    usuario
                                                                )
                                                            }
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
                                                            onClick = {
                                                                viewModel.clickEditarOCancelar(
                                                                    usuario
                                                                )
                                                            }
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

                    item {
                        Spacer(modifier = Modifier.height(15.dp))

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 20.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
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

                                                TextFieldRegisterYLogin(
                                                    value = viewModel.contrasenha,
                                                    onValueChange = { viewModel.contrasenha = it },
                                                    isPassword = true,
                                                    label = "Nueva contraseña",
                                                    modifier = Modifier.padding(horizontal = 20.dp)
                                                )

                                                Spacer(modifier = Modifier.height(20.dp))

                                                TextFieldRegisterYLogin(
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
                                                    enabled = !viewModel.isLoadingContrasenha,
                                                    onClick = { viewModel.cambiarContrasenha(usuario) }
                                                ) {
                                                    if (viewModel.isLoadingContrasenha) {
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

                                                if (viewModel.errorMessageContrasenha !== "") {
                                                    Text(
                                                        text = viewModel.errorMessageContrasenha,
                                                        color = MaterialTheme.colorScheme.error,
                                                        modifier = Modifier.padding(top = 8.dp)
                                                    )
                                                }

                                                if (viewModel.successMessageContrasenha !== "") {
                                                    Text(
                                                        text = viewModel.successMessageContrasenha,
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

                    item {
                        Spacer(modifier = Modifier.height(15.dp))

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 20.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        var expandedDireccion by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { expandedDireccion = !expandedDireccion },
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
                                    text = "Direcciones",
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
                                    visible = expandedDireccion,
                                    enter = expandVertically() + fadeIn(),
                                    exit = shrinkVertically() + fadeOut()
                                ) {
                                    Column (
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Spacer(modifier = Modifier.height(20.dp))

                                        if (usuario.direcciones.isEmpty()) {
                                            Text (
                                                text = "No se han encontrado direcciones",
                                                color = MaterialTheme.colorScheme.onBackground,
                                            )
                                        } else {
                                            usuario.direcciones.forEach { direccion ->
                                                DireccionItem(direccion)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(20.dp))

                                        Button(
                                            modifier = modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 20.dp),
                                            onClick = { /* TODO */ }
                                        ) {
                                            Text(
                                                text = "AÑADIR DIRECCIÓN",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DireccionItem(
    direccion: Direccion,
    isEditarDireccion: Boolean = false
) {
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
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(2f)) {
                    Text(
                        text = "Calle",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = direccion.calle ?: "N/A",
                        fontSize = 15.sp
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Nº / Piso",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${direccion.numero ?: "-"} · ${direccion.piso ?: "-"}",
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ciudad",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = direccion.ciudad ?: "N/A",
                        fontSize = 15.sp
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Provincia",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = direccion.provincia ?: "N/A",
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Código postal",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = direccion.codigoPostal ?: "N/A",
                        fontSize = 15.sp
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Button(
                        modifier = Modifier.width(65.dp),
                        onClick = { /* TODO */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}
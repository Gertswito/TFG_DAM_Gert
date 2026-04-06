package com.gert.tfgdam.feature.user.usersettings.direcciones.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.gert.tfgdam.feature.admin.autor.viewmodel.AutorAdminViewModel
import com.gert.tfgdam.feature.admin.cliente.model.Cliente
import com.gert.tfgdam.feature.admin.direccion.model.Direccion
import com.gert.tfgdam.feature.user.usersettings.direcciones.viewmodel.UserDireccionesViewModel
import com.gert.tfgdam.ui.theme.estiloreutilizable.textfield.TextFieldEstilo

@Composable
fun UserDireccionesScreen(
    usuarioScreenAnterior: Cliente,
    modifier: Modifier = Modifier,
    viewModel: UserDireccionesViewModel = viewModel()
) {
    LaunchedEffect(usuarioScreenAnterior.id) {
        viewModel.setDireccionesIniciales(usuarioScreenAnterior, usuarioScreenAnterior.direcciones)
    }

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

                    if (viewModel.direcciones.isEmpty() || viewModel.direcciones.all { it.activo == false }) {
                        Text(
                            text = "No se han encontrado direcciones",
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    } else {
                        viewModel.direcciones.forEach { direccion ->
                            DireccionItem(direccion, false)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        onClick = { viewModel.clickAbrirModalOCerrar() }
                    ) {
                        Text(
                            text = "AÑADIR DIRECCIÓN",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (viewModel.abrirModalEditarDireccion) {
                        EditarDireccionModal(
                            showDialog = viewModel.abrirModalEditarDireccion,
                            direccionEditar = null,
                            viewModel = viewModel,
                            onDismiss = { viewModel.abrirModalEditarDireccion = false },
                            onSave = { direccionActualizada ->
                                viewModel.actualizarDireccion(direccionActualizada)
                                viewModel.abrirModalEditarDireccion = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DireccionItem(
    direccion: Direccion,
    isPago: Boolean = false,
    viewModel: UserDireccionesViewModel = viewModel()
) {
    var abrirModalEditarDireccion by remember { mutableStateOf(false) }
    var direccionSeleccionadaDelete by remember { mutableStateOf<Direccion?>(null) }
    val estaSeleccionada = viewModel.direccionSeleccionada?.id == direccion.id
    val context = LocalContext.current

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
                        text = (direccion.numero.toString()) + " · " + (direccion.piso ?: "-"),
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
                    if(!isPago) {
                        Row(
                            modifier = Modifier.width(100.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { abrirModalEditarDireccion = true },
                                modifier = Modifier.width(50.dp).padding(end = 2.dp),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar"
                                )
                            }

                            IconButton(
                                onClick = { direccionSeleccionadaDelete = direccion },
                                modifier = Modifier.width(50.dp).padding(start = 4.dp),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.onError,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar"
                                )
                            }
                        }

                        if (abrirModalEditarDireccion) {
                            EditarDireccionModal(
                                showDialog = abrirModalEditarDireccion,
                                direccionEditar = direccion,
                                viewModel = viewModel,
                                onDismiss = { abrirModalEditarDireccion = false },
                                onSave = { direccionActualizada ->
                                    viewModel.actualizarDireccion(direccionActualizada)
                                    abrirModalEditarDireccion = false
                                }
                            )
                        }

                        if (direccionSeleccionadaDelete != null) {
                            EliminarDireccion(
                                showDialog = true,
                                onDismiss = {
                                    direccionSeleccionadaDelete = null
                                },
                                direccion = direccionSeleccionadaDelete!!,
                                viewModel = viewModel,
                                onSave = { direccionEliminada ->
                                    viewModel.actualizarDireccion(direccionEliminada)
                                    direccionSeleccionadaDelete = null
                                }
                            )
                        }
                    } else {
                        if (!estaSeleccionada) {
                            Button(
                                onClick = {
                                    viewModel.vibrar(context)
                                    viewModel.seleccionarDireccion(direccion)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Seleccionar",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    viewModel.vibrar(context)
                                    viewModel.deseleccionarDireccion()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Seleccionar",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditarDireccionModal(
    showDialog: Boolean,
    direccionEditar: Direccion? = null,
    viewModel: UserDireccionesViewModel = viewModel(),
    onDismiss: () -> Unit,
    onSave: (Direccion) -> Unit
) {
    if (showDialog) {
        LaunchedEffect(Unit) {
            viewModel.restaurarCamposDireccion(direccionEditar)
            viewModel.errorMessage = ""
            viewModel.successMessage = ""
        }

        Dialog(
            onDismissRequest = {
                onDismiss()
                viewModel.restaurarCamposDireccion(null)
            }
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(top = 30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (direccionEditar != null) {
                            Text(
                                text = "Editar",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 40.sp,
                                lineHeight = 40.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp)
                            )
                        } else {
                            Text(
                                text = "Crear",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 40.sp,
                                lineHeight = 40.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp, horizontal = 8.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = MaterialTheme.shapes.medium
                                ),
                            elevation = CardDefaults.cardElevation(0.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, bottom = 16.dp, start = 10.dp, end = 10.dp)
                            ) {
                                TextFieldEstilo(
                                    value = viewModel.calleEditar,
                                    onValueChange = {
                                        viewModel.calleEditar = it
                                    },
                                    label = "Calle",
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextFieldEstilo(
                                        value = viewModel.numeroEditar,
                                        onValueChange = {
                                            viewModel.numeroEditar = it
                                        },
                                        label = "Número",
                                        modifier = Modifier.padding(start = 10.dp, end = 5.dp)
                                            .weight(1f)
                                    )

                                    TextFieldEstilo(
                                        value = viewModel.pisoEditar,
                                        onValueChange = {
                                            viewModel.pisoEditar = it
                                        },
                                        label = "Piso",
                                        modifier = Modifier.padding(start = 5.dp, end = 10.dp)
                                            .weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                TextFieldEstilo(
                                    value = viewModel.ciudadEditar,
                                    onValueChange = {
                                        viewModel.ciudadEditar = it
                                    },
                                    label = "Ciudad",
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                TextFieldEstilo(
                                    value = viewModel.provinciaEditar,
                                    onValueChange = {
                                        viewModel.provinciaEditar = it
                                    },
                                    label = "Provincia",
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                TextFieldEstilo(
                                    value = viewModel.codigoPostalEditar,
                                    onValueChange = {
                                        viewModel.codigoPostalEditar = it
                                    },
                                    label = "Código postal",
                                    modifier = Modifier.padding(
                                        start = 10.dp,
                                        end = 10.dp,
                                        bottom = 10.dp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        if (viewModel.errorMessage !== "") {
                            Text(
                                text = viewModel.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                            )
                        }

                        if (viewModel.successMessage !== "") {
                            Text(
                                text = viewModel.successMessage,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }

                        if (direccionEditar != null) {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                onClick = { viewModel.usuario?.let { user ->
                                    viewModel.editarDireccion(user) { nuevaDireccion ->
                                        onSave(nuevaDireccion)
                                    }
                                }},
                                enabled = !viewModel.isLoading
                            ) {
                                if (viewModel.isLoading) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Text(
                                        text = "EDITAR",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        } else {
                            Button(
                                onClick = { viewModel.usuario?.let { user ->
                                    viewModel.crearDireccion(user) { nuevaDireccion ->
                                        viewModel.agregarDireccion(nuevaDireccion)
                                        onSave(nuevaDireccion)
                                    }
                                }},
                                enabled = !viewModel.isLoading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            ) {
                                if (viewModel.isLoading) {
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
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    IconButton(
                        onClick = {
                            onDismiss()
                            viewModel.restaurarCamposDireccion(null)
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EliminarDireccion(
    direccion: Direccion,
    viewModel: UserDireccionesViewModel = viewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: (Direccion) -> Unit
) {
    if (showDialog) {
        LaunchedEffect(Unit) {
            viewModel.errorMessage = ""
            viewModel.successMessage = ""
        }

        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 35.dp, bottom = 20.dp, start = 8.dp, end = 8.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = "Eliminar",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 40.sp,
                            lineHeight = 40.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = ("¿Seguro que quieres borrar la dirección?"),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (viewModel.errorMessage !== "") {
                            Text(
                                text = viewModel.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            onClick = {
                                viewModel.eliminarDireccion(direccion) { direccionEliminada ->
                                    viewModel.quitarDireccion(direccionEliminada)
                                    onSave(direccionEliminada)
                                }
                            },
                            enabled = !viewModel.isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onError,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            if (viewModel.isLoading) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "ELIMINAR",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = {
                            onDismiss()
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                }
            }
        }
    }
}
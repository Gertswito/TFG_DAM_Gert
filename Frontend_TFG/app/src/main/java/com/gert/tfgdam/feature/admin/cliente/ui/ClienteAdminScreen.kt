package com.gert.tfgdam.feature.admin.cliente.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.feature.admin.cliente.model.Cliente
import com.gert.tfgdam.feature.admin.cliente.viewmodel.ClienteAdminViewModel
import com.gert.tfgdam.ui.theme.estiloreutilizable.textfield.TextFieldDropdownEstiloAlternativo
import com.gert.tfgdam.ui.theme.estiloreutilizable.textfield.TextFieldEstiloAlternativo
import kotlinx.coroutines.delay

@Composable
fun ClienteAdminScreen(
    viewModel: ClienteAdminViewModel = viewModel(),
) {
    val clientes = viewModel.clientes
    var showEmpty by remember { mutableStateOf(false) }
    val horizontalScrollState = rememberScrollState()
    var abrirModal by remember { mutableStateOf(false) }
    var clienteSeleccionado by remember { mutableStateOf<Cliente?>(null) }

    LaunchedEffect(clientes) {
        if (clientes.isEmpty()) {
            delay(200)
            showEmpty = true
        } else {
            showEmpty = false
        }
    }

    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Clientes",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )

                Button (
                    onClick = { abrirModal = true },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                if (abrirModal) {
                    CrearEditarCliente(
                        viewModel = viewModel,
                        showDialog = abrirModal,
                        onDismiss = {
                            viewModel.restaurarCamposCliente(null)
                            abrirModal = false
                        },
                        onSave = {
                            abrirModal = false
                        }
                    )
                }
            }

            if(clientes.isEmpty() && showEmpty) {
                Text(
                    text = "No hay clientes disponibles",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 10.dp)
                        .background(MaterialTheme.colorScheme.primary)
                        .horizontalScroll(horizontalScrollState)
                ) {
                    Text(
                        text = "Id",
                        modifier = Modifier
                            .width(viewModel.cambiarDeCharacteresADp(viewModel.idWidth))
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Usuario",
                        modifier = Modifier
                            .width(viewModel.cambiarDeCharacteresADp(viewModel.usuarioWidth))
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Rol",
                        modifier = Modifier
                            .width(100.dp)
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Nombre",
                        modifier = Modifier
                            .width(viewModel.cambiarDeCharacteresADp(viewModel.nombreWidth))
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Apellidos",
                        modifier = Modifier
                            .width(viewModel.cambiarDeCharacteresADp(viewModel.apellidosWidth))
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Email",
                        modifier = Modifier
                            .width(viewModel.cambiarDeCharacteresADp(viewModel.emailWidth))
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "",
                        modifier = Modifier.width(70.dp)
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 15.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    clientes.forEach { cliente ->
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(horizontalScrollState),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = cliente.id.toString(),
                                    modifier = Modifier
                                        .width(viewModel.cambiarDeCharacteresADp(viewModel.idWidth))
                                        .padding(8.dp),
                                )

                                Text(
                                    text = cliente.usuario ?: "",
                                    modifier = Modifier
                                        .width(viewModel.cambiarDeCharacteresADp(viewModel.usuarioWidth))
                                        .padding(8.dp),
                                )

                                Text(
                                    text = cliente.rol.toString() ?: "",
                                    modifier = Modifier
                                        .width(100.dp)
                                        .padding(8.dp),
                                )

                                Text(
                                    text = cliente.nombre ?: "",
                                    modifier = Modifier
                                        .width(viewModel.cambiarDeCharacteresADp(viewModel.nombreWidth))
                                        .padding(8.dp),
                                )

                                Text(
                                    text = cliente.apellidos ?: "",
                                    modifier = Modifier
                                        .width(viewModel.cambiarDeCharacteresADp(viewModel.apellidosWidth))
                                        .padding(8.dp),
                                )

                                Text(
                                    text = cliente.email ?: "",
                                    modifier = Modifier
                                        .width(viewModel.cambiarDeCharacteresADp(viewModel.emailWidth))
                                        .padding(8.dp),
                                )

                                Box(
                                    modifier = Modifier
                                        .width(70.dp)
                                        .padding(horizontal = 8.dp),
                                ) {
                                    IconButton(
                                        onClick = { clienteSeleccionado = cliente },
                                        modifier = Modifier.width(50.dp),
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
                                }
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                if (clienteSeleccionado != null) {
                    CrearEditarCliente(
                        viewModel = viewModel,
                        cliente = clienteSeleccionado,
                        showDialog = true,
                        onDismiss = {
                            viewModel.restaurarCamposCliente(null)
                            clienteSeleccionado = null
                        },
                        onSave = {
                            clienteSeleccionado = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CrearEditarCliente(
    cliente: Cliente? = null,
    viewModel: ClienteAdminViewModel = viewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    if (showDialog) {
        LaunchedEffect(Unit) {
            if (cliente != null) {
                viewModel.restaurarCamposCliente(cliente)
            }
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
                        if (cliente != null) {
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

                            Spacer(modifier = Modifier.height(10.dp))

                            TextFieldEstiloAlternativo(
                                value = viewModel.idCliente,
                                onValueChange = { viewModel.idCliente = it },
                                label = "Id",
                                isPassword = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                isEnabled = false
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

                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        TextFieldEstiloAlternativo(
                            value = viewModel.usuarioCliente,
                            onValueChange = { viewModel.usuarioCliente = it },
                            label = "Usuario",
                            isPassword = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            TextFieldEstiloAlternativo(
                                value = viewModel.nombreCliente,
                                onValueChange = { viewModel.nombreCliente = it },
                                label = "Nombre",
                                modifier = Modifier.weight(1f)
                            )

                            TextFieldEstiloAlternativo(
                                value = viewModel.apellidoCliente,
                                onValueChange = { viewModel.apellidoCliente = it },
                                label = "Apellidos",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        TextFieldEstiloAlternativo(
                            value = viewModel.emailCliente,
                            onValueChange = { viewModel.emailCliente = it },
                            label = "Email",
                            isPassword = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        if (cliente == null) {
                            TextFieldEstiloAlternativo(
                                value = viewModel.contrasenhaCliente,
                                onValueChange = { viewModel.contrasenhaCliente = it },
                                label = "Contraseña",
                                isPassword = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        TextFieldDropdownEstiloAlternativo(
                            selectedItem = viewModel.rolCliente,
                            items = viewModel.listaRoles,
                            onItemSelected = { viewModel.rolCliente = it },
                            label = "Rol",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        if (viewModel.errorMessage !== "") {
                            Text(
                                text = viewModel.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }

                        if (cliente != null) {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                onClick = {
                                    viewModel.editarCliente {
                                        onSave()
                                    }
                                },
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                onClick = {
                                    viewModel.crearCliente {
                                        onSave()
                                    }
                                },
                                enabled = !viewModel.isLoading
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
                    }

                    IconButton(
                        onClick = {
                            viewModel.restaurarCamposCliente(null)
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
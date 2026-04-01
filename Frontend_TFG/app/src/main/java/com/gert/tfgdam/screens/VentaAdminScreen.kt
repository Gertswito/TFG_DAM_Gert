package com.gert.tfgdam.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.model.Autor
import com.gert.tfgdam.model.LineaVenta
import com.gert.tfgdam.model.Venta
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.viewmodel.AutorAdminViewModel
import com.gert.tfgdam.viewmodel.VentaAdminViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@Composable
fun VentaAdminScreen(
    viewModel: VentaAdminViewModel = viewModel(),
    navController: NavController
) {
    val ventas = viewModel.ventas
    var showEmpty by remember { mutableStateOf(false) }
    var verLineasVenta by remember { mutableStateOf(false) }

    val horizontalScrollState = rememberScrollState()
    val horizontalScrollStateLineasVentas = rememberScrollState()

    var abrirModalVenta by remember { mutableStateOf(false) }
    var abrirModalLineaVenta by remember { mutableStateOf(false) }

    LaunchedEffect(ventas) {
        if (ventas.isEmpty()) {
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
                    text = "Ventas",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )

                Button (
                    onClick = { abrirModalVenta = true },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                if (abrirModalVenta) {
                    CrearEditarVenta(
                        viewModel = viewModel,
                        showDialog = abrirModalVenta,
                        onDismiss = { abrirModalVenta = false },
                        onSave = {
                            abrirModalVenta = false
                        }
                    )
                }
            }

            if(ventas.isEmpty() && showEmpty) {
                Text(
                    text = "No hay ventas disponibles",
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
                        text = "",
                        modifier = Modifier
                            .width(50.dp)
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

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
                        text = "Dirección",
                        modifier = Modifier
                            .width(viewModel.cambiarDeCharacteresADp(viewModel.direccionWidth))
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Fecha",
                        modifier = Modifier
                            .width(150.dp)
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Hora",
                        modifier = Modifier
                            .width(100.dp)
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Precio",
                        modifier = Modifier
                            .width(120.dp)
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 15.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    ventas.forEach { venta ->
                        item {
                            val lineas = viewModel.lineasPorVenta[venta.id] ?: emptyList()

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(horizontalScrollState),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box (
                                    modifier = Modifier
                                        .width(50.dp)
                                        .padding(8.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            verLineasVenta = !verLineasVenta

                                            if (verLineasVenta) {
                                                venta.id?.let { viewModel.cargarLineasVentasPorVenta(it) }
                                            } else {
                                                venta.id?.let { viewModel.limpiarLineasVenta(it) }
                                            }
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Desplegar"
                                    )
                                }

                                Text(
                                    text = venta.id.toString(),
                                    modifier = Modifier
                                        .width(viewModel.cambiarDeCharacteresADp(viewModel.idWidth))
                                        .padding(8.dp),
                                )

                                Text(
                                    text = venta.cliente?.usuario ?: "",
                                    modifier = Modifier
                                        .width(viewModel.cambiarDeCharacteresADp(viewModel.usuarioWidth))
                                        .padding(8.dp),
                                )

                                Column (
                                    modifier = Modifier.width(viewModel.cambiarDeCharacteresADp(viewModel.direccionWidth))
                                ) {
                                    Text(
                                        text = (venta.direccion?.calle + " " + venta.direccion?.numero.toString() + ", " + venta.direccion?.piso) ?: "",
                                        modifier = Modifier
                                            .width(viewModel.cambiarDeCharacteresADp(viewModel.direccionWidth))
                                            .padding(top = 8.dp, end = 8.dp, start = 8.dp),
                                    )

                                    Text(
                                        text = (venta.direccion?.ciudad + " " + venta.direccion?.provincia) ?: "",
                                        modifier = Modifier
                                            .width(viewModel.cambiarDeCharacteresADp(viewModel.direccionWidth))
                                            .padding(end = 8.dp, start = 8.dp),
                                    )

                                    Text(
                                        text = (venta.direccion?.codigoPostal) ?: "",
                                        modifier = Modifier
                                            .width(viewModel.cambiarDeCharacteresADp(viewModel.direccionWidth))
                                            .padding(bottom = 8.dp, end = 8.dp, start = 8.dp),
                                    )
                                }

                                Text(
                                    text = venta.fecha ?: "",
                                    modifier = Modifier
                                        .width(150.dp)
                                        .padding(8.dp),
                                )

                                Text(
                                    text = venta.hora ?: "",
                                    modifier = Modifier
                                        .width(100.dp)
                                        .padding(8.dp),
                                )

                                val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()
                                val formatoDinero = NumberFormat.getCurrencyInstance(locale)
                                Text(
                                    text = formatoDinero.format(venta.precioFinal ?: 0.0),
                                    modifier = Modifier
                                        .width(120.dp)
                                        .padding(8.dp),
                                )
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.primary)

                            if (lineas.isNotEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                                        .horizontalScroll(horizontalScrollStateLineasVentas)
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
                                        text = "Libro",
                                        modifier = Modifier
                                            .width(250.dp)
                                            .padding(8.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )

                                    Text(
                                        text = "Cantidad",
                                        modifier = Modifier
                                            .width(100.dp)
                                            .padding(8.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )

                                    Text(
                                        text = "P. Unit.",
                                        modifier = Modifier
                                            .width(100.dp)
                                            .padding(8.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )

                                    Text(
                                        text = "P. Total",
                                        modifier = Modifier
                                            .width(100.dp)
                                            .padding(8.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                                        .background(MaterialTheme.colorScheme.background)
                                ) {
                                    lineas.forEachIndexed { index, linea ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .horizontalScroll(horizontalScrollStateLineasVentas),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = linea.id.toString(),
                                                modifier = Modifier
                                                    .width(viewModel.cambiarDeCharacteresADp(viewModel.idWidth))
                                                    .padding(8.dp),
                                            )

                                            Text(
                                                text = linea.libro?.titulo ?: "",
                                                modifier = Modifier
                                                    .width(250.dp)
                                                    .padding(8.dp),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Text(
                                                text = (linea.cantidad.toString() + " uds") ?: "",
                                                modifier = Modifier
                                                    .width(100.dp)
                                                    .padding(8.dp),
                                            )

                                            val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()
                                            val formatoDinero = NumberFormat.getCurrencyInstance(locale)
                                            Text(
                                                text = formatoDinero.format(linea.precioParcial ?: 0.0),
                                                modifier = Modifier
                                                    .width(100.dp)
                                                    .padding(8.dp),
                                            )

                                            Text(
                                                text = formatoDinero.format(linea.precioTotal ?: 0.0),
                                                modifier = Modifier
                                                    .width(100.dp)
                                                    .padding(8.dp),
                                            )
                                        }

                                        HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                                    }

                                    Button (
                                        onClick = { abrirModalLineaVenta = true },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        )

                                    ) {
                                        Row (
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Añadir línea",
                                                modifier = Modifier.padding(start = 8.dp),
                                                fontSize = 16.sp
                                            )

                                            Spacer(modifier = Modifier.width(8.dp))

                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Añadir",
                                                tint = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                    }

                                    if (abrirModalLineaVenta) {
                                        CrearEditarLineaVenta(
                                            viewModel = viewModel,
                                            venta = venta,
                                            showDialog = abrirModalLineaVenta,
                                            onDismiss = { abrirModalLineaVenta = false },
                                            onSave = {
                                                abrirModalLineaVenta = false
                                            }
                                        )
                                    }

                                    HorizontalDivider(color = MaterialTheme.colorScheme.primary)
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
fun CrearEditarVenta(
    venta: Venta? = null,
    viewModel: VentaAdminViewModel = viewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    if (showDialog) {
        viewModel.cargarListasEditarYCrearVenta()
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
                        if (venta != null) {
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
                                value = viewModel.idVenta,
                                onValueChange = { viewModel.idVenta = it },
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

                        TextFieldDropdownEstiloAlternativo(
                            selectedItem = viewModel.clienteVenta,
                            items = viewModel.listaClientes,
                            onItemSelected = { viewModel.clienteVenta = it },
                            label = "Cliente",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            itemToString = { cliente -> cliente?.usuario ?: "N/A" }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        if (viewModel.clienteVenta != null) {
                            val cliente = viewModel.clienteVenta
                            viewModel.cargarListaDireccionesPorCliente(cliente?.id ?: 0)

                            TextFieldDropdownEstiloAlternativo(
                                selectedItem = viewModel.direccionVenta,
                                items = viewModel.listaDirecciones,
                                onItemSelected = { viewModel.direccionVenta = it },
                                label = "Dirección de envío",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                itemToString = { direccion ->
                                    if (direccion?.calle != null) {
                                        "${direccion.calle} ${direccion.numero}, ${direccion.piso}"
                                    } else {
                                        "N/A"
                                    }                                }
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        TextFieldDatePickerEstiloAlternativo(
                            date = viewModel.fechaVenta,
                            onDateSelected = { viewModel.fechaVenta = it },
                            label = "Fecha",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        TextFieldTimePickerEstiloAlternativo(
                            time = viewModel.horaVenta,
                            onTimeSelected = { viewModel.horaVenta = it },
                            label = "Hora",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
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

                        if (venta != null) {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                onClick = {
                                    viewModel.editarVenta {
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
                                    viewModel.crearVenta {
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
                        onClick = { onDismiss() },
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

@Composable
fun CrearEditarLineaVenta(
    lineaVenta: LineaVenta? = null,
    venta: Venta,
    viewModel: VentaAdminViewModel = viewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    if (showDialog) {
        viewModel.cargarListasEditarYCrearLineaVenta()
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
                        if (lineaVenta != null) {
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
                                value = viewModel.idVenta,
                                onValueChange = { viewModel.idVenta = it },
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

                        TextFieldDropdownEstiloAlternativo(
                            selectedItem = viewModel.libroLineaVenta,
                            items = viewModel.listaLibros,
                            onItemSelected = { viewModel.libroLineaVenta = it },
                            label = "Libro",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            itemToString = { libro -> libro?.titulo ?: "N/A" }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        TextFieldEstiloAlternativo(
                            value = viewModel.cantidadLineaVenta,
                            onValueChange = { viewModel.cantidadLineaVenta = it },
                            label = "Cantidad",
                            isPassword = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        if (viewModel.libroLineaVenta != null && viewModel.cantidadLineaVenta != "") {
                            val libro = viewModel.libroLineaVenta
                            viewModel.calcularPreciosLineaVenta(libro)

                            if (viewModel.precioParcialLineaVenta != "" && viewModel.precioTotalLineaVenta != "") {
                                TextFieldEstiloAlternativo(
                                    value = viewModel.precioParcialLineaVenta,
                                    onValueChange = { viewModel.precioParcialLineaVenta = it },
                                    label = "Precio Unitario",
                                    isPassword = false,
                                    isEnabled = false,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                TextFieldEstiloAlternativo(
                                    value = viewModel.precioTotalLineaVenta,
                                    onValueChange = { viewModel.precioTotalLineaVenta = it },
                                    label = "Precio Total",
                                    isPassword = false,
                                    isEnabled = false,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }

                        if (viewModel.errorMessage !== "") {
                            Text(
                                text = viewModel.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }

                        if (lineaVenta != null) {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                onClick = {
                                    viewModel.editarLineaVenta(venta) {
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
                                    viewModel.crearLineaVenta(venta) {
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
                        onClick = { onDismiss() },
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
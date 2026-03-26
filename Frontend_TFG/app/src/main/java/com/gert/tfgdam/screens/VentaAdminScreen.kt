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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.routes.Routes
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
                    onClick = { navController.navigate(Routes.AUTOR_ADMIN) },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir",
                        tint = MaterialTheme.colorScheme.onPrimary
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

                            if(!lineas.isEmpty()) {
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
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
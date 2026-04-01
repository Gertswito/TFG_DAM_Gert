package com.gert.tfgdam.feature.admin.homeadmin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.core.navigation.routes.Routes
import com.gert.tfgdam.feature.admin.homeadmin.viewmodel.HomeAdminViewModel
import com.gert.tfgdam.ui.theme.estiloreutilizable.textfield.TextFieldEstiloAlternativo
import kotlinx.coroutines.delay

@Composable
fun HomeAdminScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeAdminViewModel = viewModel(),
) {
    val libros = viewModel.libros
    var showEmpty by remember { mutableStateOf(false) }

    LaunchedEffect(libros) {
        if (libros.isEmpty()) {
            delay(200)
            showEmpty = true
        } else {
            showEmpty = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text (
            text = "Entidades",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp),
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Card (
            modifier = Modifier
                .padding(end = 8.dp, start = 8.dp)
                .height(60.dp)
                .fillMaxWidth(),
            onClick = { navController.navigate(Routes.LIBRO_ADMIN) },
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ){
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Libros",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Card (
                modifier = Modifier
                    .padding(end = 4.dp, start = 8.dp)
                    .height(60.dp)
                    .weight(1f),
                onClick = { navController.navigate(Routes.GENERO_ADMIN) },
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Géneros",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Card (
                modifier = Modifier
                    .padding(end = 8.dp, start = 4.dp)
                    .height(60.dp)
                    .weight(1f),
                onClick = { navController.navigate(Routes.TIPO_LIBRO_ADMIN) },
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Tipos de libro",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Card (
                modifier = Modifier
                    .padding(end = 4.dp, start = 8.dp)
                    .height(60.dp)
                    .weight(1f),
                onClick = { navController.navigate(Routes.AUTOR_ADMIN) },
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Autores",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Card (
                modifier = Modifier
                    .padding(end = 8.dp, start = 4.dp)
                    .height(60.dp)
                    .weight(1f),
                onClick = { navController.navigate(Routes.EDITORIAL_ADMIN) },
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Editoriales",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Card (
                modifier = Modifier
                    .padding(end = 4.dp, start = 8.dp)
                    .height(60.dp)
                    .weight(1f),
                onClick = { navController.navigate(Routes.CLIENTE_ADMIN) },
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Clientes",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Card (
                modifier = Modifier
                    .padding(end = 8.dp, start = 4.dp)
                    .height(60.dp)
                    .weight(1f),
                onClick = { navController.navigate(Routes.VENTA_ADMIN) },
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Ventas",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        if (libros.isEmpty() && showEmpty) {
            Column(
                modifier = modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No hay libros disponibles para manejar su stock",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )
            }
        } else {
            Column(
                modifier = modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text (
                    text = "Haga click en cada libro para alterar su stock",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Título",
                        modifier = Modifier
                            .weight(3f)
                            .padding(start = 8.dp, top = 8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Stock",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp, top = 8.dp),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 275.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                ) {
                    items(libros.size) {
                        LibroItemTablaStock(libro = libros[it], viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun LibroItemTablaStock(
    libro: Libro,
    viewModel: HomeAdminViewModel = viewModel()
) {
    var abrirEditarStockLibroModal by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable{ abrirEditarStockLibroModal = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
        ) {
            Text(
                text = (libro.titulo ?: "N/A"),
                modifier = Modifier.padding(start = 8.dp),
            )

            Text(
                text = (libro.autor?.nombre ?: "N/A") + " - " + (libro.editorial?.nombre ?: "N/A"),
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.inverseSurface
            )
        }

        Text(
            text = libro.stock.toString() + " uds",
            textAlign = TextAlign.End,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        if (abrirEditarStockLibroModal) {
            EditarStockLibroModal(
                showDialog = abrirEditarStockLibroModal,
                libro = libro,
                viewModel = viewModel,
                onDismiss = { abrirEditarStockLibroModal = false },
                onSave = { abrirEditarStockLibroModal = false }
            )
        }
    }

    HorizontalDivider(color = MaterialTheme.colorScheme.primary)
}

@Composable
fun EditarStockLibroModal(
    libro: Libro,
    viewModel: HomeAdminViewModel = viewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    if (showDialog) {
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
                            text = libro.titulo ?: "N/A",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Text(
                            text = (libro.autor?.nombre ?: "N/A") + " - " + (libro.editorial?.nombre ?: "N/A"),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.inverseSurface,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        TextFieldEstiloAlternativo(
                            value = viewModel.addStock,
                            onValueChange = { viewModel.addStock = it },
                            label = "Stock a añadir",
                            isPassword = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "El stock introducido se sumará al stock actual !",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        if (viewModel.errorMessage !== "") {
                            Text(
                                text = viewModel.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.actualizarStock(libro) {
                                    onSave()
                                }
                            }
                        ) {
                            Text(
                                text = "ACTUALIZAR STOCK",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
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
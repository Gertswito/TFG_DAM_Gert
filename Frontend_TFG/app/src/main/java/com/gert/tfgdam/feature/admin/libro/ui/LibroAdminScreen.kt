package com.gert.tfgdam.feature.admin.libro.ui

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gert.tfgdam.R
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.libro.viewmodel.LibroAdminViewModel
import com.gert.tfgdam.ui.theme.estiloreutilizable.textfield.TextFieldDatePickerEstiloAlternativo
import com.gert.tfgdam.ui.theme.estiloreutilizable.textfield.TextFieldDropdownEstiloAlternativo
import com.gert.tfgdam.ui.theme.estiloreutilizable.textfield.TextFieldEstiloAlternativo
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LibroAdminScreen (
    viewModel: LibroAdminViewModel = viewModel(),
) {
    val libros = viewModel.libros
    var showEmpty by remember { mutableStateOf(false) }
    val horizontalScrollState = rememberScrollState()
    var abrirModal by remember { mutableStateOf(false) }

    LaunchedEffect(libros) {
        if (libros.isEmpty()) {
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
                    text = "Libros",
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
                    CrearEditarLibro(
                        viewModel = viewModel,
                        showDialog = abrirModal,
                        onDismiss = { abrirModal = false },
                        onSave = {
                            abrirModal = false
                        }
                    )
                }
            }

            if(libros.isEmpty() && showEmpty) {
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
                        text = "Portada",
                        modifier = Modifier
                            .width(100.dp)
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Título",
                        modifier = Modifier
                            .width(viewModel.cambiarDeCharacteresADp(viewModel.tituloWidth))
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Autor",
                        modifier = Modifier
                            .width(viewModel.cambiarDeCharacteresADp(viewModel.autorWidth))
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Editorial",
                        modifier = Modifier
                            .width(viewModel.cambiarDeCharacteresADp(viewModel.editorialWidth))
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "ISBN",
                        modifier = Modifier
                            .width(viewModel.cambiarDeCharacteresADp(viewModel.isbnWidth))
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Fecha de salida",
                        modifier = Modifier
                            .width(150.dp)
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Stock",
                        modifier = Modifier
                            .width(100.dp)
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Precio",
                        modifier = Modifier
                            .width(100.dp)
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
                    libros.forEach { libro ->
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(horizontalScrollState),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = libro.id.toString(),
                                    modifier = Modifier
                                        .width(viewModel.cambiarDeCharacteresADp(viewModel.idWidth))
                                        .padding(8.dp),
                                )

                                if (libro.portada != "") {
                                    AsyncImage(
                                        model = libro.portada,
                                        contentDescription = libro.titulo,
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(160.dp)
                                            .padding(5.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.libro_no_encontrado),
                                        contentDescription = libro.titulo,
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(160.dp)
                                            .padding(8.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }

                                Text(
                                    text = libro.titulo ?: "",
                                    modifier = Modifier
                                        .width(viewModel.cambiarDeCharacteresADp(viewModel.tituloWidth))
                                        .padding(8.dp),
                                )

                                Text(
                                    text = libro.autor?.nombre ?: "",
                                    modifier = Modifier
                                        .width(viewModel.cambiarDeCharacteresADp(viewModel.autorWidth))
                                        .padding(8.dp),
                                )

                                Text(
                                    text = libro.editorial?.nombre ?: "",
                                    modifier = Modifier
                                        .width(viewModel.cambiarDeCharacteresADp(viewModel.editorialWidth))
                                        .padding(8.dp),
                                )

                                Text(
                                    text = libro.isbn ?: "",
                                    modifier = Modifier
                                        .width(viewModel.cambiarDeCharacteresADp(viewModel.isbnWidth))
                                        .padding(8.dp),
                                )

                                Text(
                                    text = libro.fechaSalida ?: "",
                                    modifier = Modifier
                                        .width(150.dp)
                                        .padding(8.dp),
                                )

                                Text(
                                    text = (libro.stock.toString() + " uds") ?: "",
                                    modifier = Modifier
                                        .width(100.dp)
                                        .padding(8.dp),
                                )

                                val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()
                                val formatoDinero = NumberFormat.getCurrencyInstance(locale)
                                Text(
                                    text = formatoDinero.format(libro.precio ?: 0.00),
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


@Composable
fun CrearEditarLibro(
    libro: Libro? = null,
    viewModel: LibroAdminViewModel = viewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    if (showDialog) {
        viewModel.cargarListasEditarYCrear()
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 35.dp, bottom = 20.dp, start = 8.dp, end = 8.dp)
                            .fillMaxWidth(),
                    ) {
                        if (libro != null) {
                            item {
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
                                    value = viewModel.idLibro,
                                    onValueChange = { viewModel.idLibro = it },
                                    label = "Id",
                                    isPassword = false,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                    isEnabled = false
                                )
                            }
                        } else {
                            item {
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
                        }

                        item {
                            Column (
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (viewModel.portadaLibro != "") {
                                    AsyncImage(
                                        model = viewModel.portadaLibro,
                                        contentDescription = viewModel.tituloLibro,
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(160.dp)
                                            .padding(5.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.libro_no_encontrado),
                                        contentDescription = viewModel.tituloLibro,
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(160.dp)
                                            .padding(8.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                TextFieldEstiloAlternativo(
                                    value = viewModel.portadaLibro,
                                    onValueChange = { viewModel.portadaLibro = it },
                                    label = "Portada",
                                    isPassword = false,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            TextFieldEstiloAlternativo(
                                value = viewModel.tituloLibro,
                                onValueChange = { viewModel.tituloLibro = it },
                                label = "Nombre",
                                isPassword = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            TextFieldDropdownEstiloAlternativo(
                                selectedItem = viewModel.autorLibro,
                                items = viewModel.listaAutores,
                                onItemSelected = { viewModel.autorLibro = it },
                                label = "Autor",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                itemToString = { autor -> autor?.nombre ?: "N/A" }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            TextFieldDropdownEstiloAlternativo(
                                selectedItem = viewModel.editorialLibro,
                                items = viewModel.listaEditoriales,
                                onItemSelected = { viewModel.editorialLibro = it },
                                label = "Editorial",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                itemToString = { editorial -> editorial?.nombre ?: "N/A" }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            TextFieldDropdownEstiloAlternativo(
                                selectedItem = viewModel.tipoLibroLibro,
                                items = viewModel.listaTipoLibros,
                                onItemSelected = { viewModel.tipoLibroLibro = it },
                                label = "Tipo de libro",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                itemToString = { tipoLibro -> tipoLibro?.nombre ?: "N/A" }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            TextFieldEstiloAlternativo(
                                value = viewModel.isbnLibro,
                                onValueChange = { viewModel.isbnLibro = it },
                                label = "ISBN",
                                isPassword = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            TextFieldDatePickerEstiloAlternativo(
                                date = viewModel.fechaSalidaLibro,
                                onDateSelected = { viewModel.fechaSalidaLibro = it },
                                label = "Fecha salida",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            TextField(
                                value = viewModel.descripcionLibro,
                                onValueChange = {
                                    if (it.length <= 500) viewModel.descripcionLibro = it
                                },
                                label = { Text("Descripción") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .padding(horizontal = 8.dp),
                                singleLine = false,
                                maxLines = 5,
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

                            Spacer(modifier = Modifier.height(20.dp))

                            TextFieldEstiloAlternativo(
                                value = viewModel.stockLibro,
                                onValueChange = { viewModel.stockLibro = it },
                                label = "Stock",
                                isPassword = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            TextFieldEstiloAlternativo(
                                value = viewModel.precioLibro,
                                onValueChange = { viewModel.precioLibro = it },
                                label = "Precio",
                                isPassword = false,
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
                        }

                        if (libro != null) {
                            item {
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                    onClick = {
                                        viewModel.editarLibro {
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
                            }
                        } else {
                            item {
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                    onClick = {
                                        viewModel.crearLibro {
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
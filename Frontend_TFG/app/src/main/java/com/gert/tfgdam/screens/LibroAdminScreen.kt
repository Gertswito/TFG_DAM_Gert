package com.gert.tfgdam.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gert.tfgdam.R
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.viewmodel.LibroAdminViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LibroAdminScreen (
    viewModel: LibroAdminViewModel = viewModel(),
    navController: NavController
) {
    val libros = viewModel.libros
    var showEmpty by remember { mutableStateOf(false) }
    val horizontalScrollState = rememberScrollState()

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
                    onClick = { navController.navigate(Routes.AUTOR_ADMIN) },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir",
                        tint = MaterialTheme.colorScheme.onPrimary
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
                                            .padding(8.dp),
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
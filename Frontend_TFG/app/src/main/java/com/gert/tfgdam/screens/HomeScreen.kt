package com.gert.tfgdam.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.model.TipoLibro
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.util.JwtManager
import com.gert.tfgdam.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    navController: NavController
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

    if (libros.isEmpty() && showEmpty) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay libros disponibles",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    } else {
        val librosAgrupados: Map<TipoLibro?, List<Libro>> = libros
            .groupBy { it.tipoLibro }
            .toList()
            .sortedBy { it.first?.id }
            .toMap()

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            librosAgrupados.forEach { (tipo, librosDelTipo) ->

                item {
                    Text(
                        text = tipo?.nombre ?: "Sin nombre",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable { navController.navigate(Routes.TIPO_LIBRO_GENEROS.replace("{tipoLibro}", tipo?.nombre ?: "Sin nombre")) }
                    )
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {

                        val librosLimitados = librosDelTipo.take(5)

                        items(librosLimitados) { libro ->
                            LibroItem(libro, navController)
                        }

                        if (librosDelTipo.size > 5) {
                            item {
                                Card(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .width(150.dp)
                                        .height(270.dp)
                                        .clickable { navController.navigate(Routes.TIPO_LIBRO_GENEROS.replace("{tipoLibro}", tipo?.nombre ?: "Sin nombre")) },
                                    elevation = CardDefaults.cardElevation(4.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Ver todos",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun LibroItem(
    libro: Libro,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(160.dp)
            .height(270.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = { navController.navigate(Routes.LIBRO_DETAILS.replace("{libroId}", libro.id.toString())) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize() // Permite superposición
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = libro.portada,
                    contentDescription = libro.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = libro.titulo ?: "Sin título",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()
                    val formatoDinero = NumberFormat.getCurrencyInstance(locale)
                    Text(
                        text = formatoDinero.format(libro.precio ?: 0.00),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    BotonAñadirCarrito(libro)
                }
            }

            IconButton(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Añadir a la lista de deseados",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun BotonAñadirCarrito(
    libroEspecifico: Libro,
    isLibroDetails: Boolean = false,
    modifier: Modifier = Modifier
) {
    val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()
    val formatoDinero = NumberFormat.getCurrencyInstance(locale)
    val context = LocalContext.current
    val userInfo by JwtManager.getUserInfoFlow(context).collectAsState(initial = null)
    val esUser = userInfo?.rol == "USER"
    if (isLibroDetails) {
        Button(
            modifier = modifier.fillMaxWidth(),
            enabled = esUser,
            onClick = { ClickearBoton(context) },
        ) {
            Text(
                text = ("AÑADIR AL CARRITO - ") + (libroEspecifico?.precio?.let {
                    formatoDinero.format(
                        it
                    )
                } ?: "N/A"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    } else {
        Button(
            modifier = modifier
                .width(60.dp)
                .height(30.dp),
            enabled = esUser,
            onClick = { ClickearBoton(context) }
        ) {


            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Añadir",
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )

        }
    }
}

fun ClickearBoton(context: Context) {
    Toast.makeText(
        context,
        "WIP: Añadido al carrito",
        Toast.LENGTH_SHORT
    ).show()
}
package com.gert.tfgdam.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gert.tfgdam.model.CarritoItem
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.util.JwtManager
import com.gert.tfgdam.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CarritoScreen(
    viewModel: CarritoViewModel = viewModel(),
    navController: NavController
) {
    val carritoItems by viewModel.carritoItems.collectAsState()
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Card (
                modifier = Modifier
                    .padding(8.dp)
                    .height(60.dp)
                    .weight(1f),
                onClick = { navController.navigate(Routes.LISTA_DESEADOS) },
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
                        text = "Lista de deseados",
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
                    .padding(8.dp)
                    .height(60.dp)
                    .weight(1f),
                onClick = { navController.navigate(Routes.HISTORIAL_COMPRA) },
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
                        text = "Historial de compra",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Card (
            modifier = Modifier
                .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 18.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ){
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(top = 20.dp, bottom = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Carrito",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(5.dp))

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 10.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(5.dp))

                if (carritoItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "El carrito está vacío :(",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        items(items = carritoItems, key = { it.libro.id!! }) { item ->
                            LibroItemCarrito(item)
                        }
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp, horizontal = 10.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val total = viewModel.calcularTotal()
                        val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()
                        val formatoDinero = NumberFormat.getCurrencyInstance(locale)
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)) {
                                    append("Total: ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = 18.sp, color = MaterialTheme.colorScheme.secondary)) {
                                    append(formatoDinero.format(total))
                                }
                            }
                        )

                        Button(
                            onClick = { navController.navigate(Routes.PAGO) }
                        ) {
                            Text(
                                text = "COMPRAR",
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

@Composable
fun LibroItemCarrito(
    item: CarritoItem,
){
    val viewModel: CarritoViewModel = viewModel()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.libro.portada,
                contentDescription = item.libro.titulo,
                modifier = Modifier
                    .height(100.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.fillMaxWidth(1f),
            ) {
                Text(
                    text = item.libro.titulo ?: "Sin título",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()
                    val formatoDinero = NumberFormat.getCurrencyInstance(locale)
                    Text(
                        text = formatoDinero.format((item.libro.precio ?: 0.00) * item.cantidad),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            if (item.cantidad > 1) {
                                viewModel.quitarUnoDelCarrito(item.libro.id!!)
                            } else {
                                viewModel.deleteDelCarrito(item.libro.id!!)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Disminuir"
                            )
                        }

                        Text(
                            text = item.cantidad.toString(),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(20.dp),
                            textAlign = TextAlign.Center
                        )

                        IconButton(
                            onClick = { viewModel.addAlCarrito(item.libro) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Aumentar"
                            )
                        }
                    }

                    IconButton(onClick = { viewModel.deleteDelCarrito(item.libro.id!!) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BotonAddCarrito(
    libroEspecifico: Libro,
    isLibroDetails: Boolean = false,
) {
    val carritoViewModel: CarritoViewModel = viewModel()
    val context = LocalContext.current
    val userInfo by JwtManager.getUserInfoFlow(context).collectAsState(initial = null)
    val esUser = userInfo?.rol == "USER"

    if (!isLibroDetails) {
        Button(
            modifier = Modifier
                .width(60.dp)
                .height(30.dp),
            enabled = esUser,
            onClick = { carritoViewModel.addAlCarrito(libroEspecifico) }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Añadir",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    } else {
        val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()
        val formatoDinero = NumberFormat.getCurrencyInstance(locale)
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = esUser,
            onClick = { carritoViewModel.addAlCarrito(libroEspecifico) },
        ) {
            Text(
                text = ("AÑADIR AL CARRITO - ") + (libroEspecifico.precio?.let { formatoDinero.format(it) } ?: "N/A"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
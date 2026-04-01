package com.gert.tfgdam.ui.theme.estiloreutilizable.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.gert.tfgdam.R
import com.gert.tfgdam.feature.user.carrito.model.CarritoItem
import com.gert.tfgdam.feature.user.carrito.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LibroItemCarrito(
    item: CarritoItem,
){
    val viewModel: CarritoViewModel = viewModel()
    val context = LocalContext.current
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
            if (item.libro.portada != "") {
                AsyncImage(
                    model = item.libro.portada,
                    contentDescription = item.libro.titulo,
                    modifier = Modifier
                        .height(100.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.libro_no_encontrado),
                    contentDescription = item.libro.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )
            }

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
                            viewModel.vibrar(context)
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
                            onClick = {
                                viewModel.vibrar(context)
                                viewModel.addAlCarrito(item.libro)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Aumentar"
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            viewModel.vibrar(context)
                            viewModel.deleteDelCarrito(item.libro.id!!)
                        }
                    ) {
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
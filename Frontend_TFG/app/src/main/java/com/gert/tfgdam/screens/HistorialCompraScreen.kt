package com.gert.tfgdam.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.gert.tfgdam.model.LineaVenta
import com.gert.tfgdam.model.Venta
import com.gert.tfgdam.util.JwtManager
import com.gert.tfgdam.viewmodel.HistorialCompraViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HistorialCompraScreen(
    viewModel: HistorialCompraViewModel = viewModel()
) {
    val listaHistorial = viewModel.listaHistorial
    val context = LocalContext.current
    val userInfo by JwtManager.getUserInfoFlow(context).collectAsState(initial = null)

    LaunchedEffect(userInfo?.sub) {
        userInfo?.sub?.let { usuario ->
            viewModel.cargarHistorial(usuario)
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Historial de compras",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
        }

        if (listaHistorial.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "No se ha terminado ninguna compra aún",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            val ventasAgrupadas = listaHistorial.groupBy { it.venta?.id }.toSortedMap(compareBy(nullsLast()) { it })
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top
            ) {
                ventasAgrupadas.forEach { (ventaId, lineas) ->

                    item {
                        VentaExpandableCard(
                            venta = lineas.first().venta,
                            lineas = lineas.sortedBy { it.id }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VentaExpandableCard(
    venta: Venta?,
    lineas: List<LineaVenta>
) {
    var verLineasVenta by remember { mutableStateOf(false) }
    val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()
    val formatoDinero = NumberFormat.getCurrencyInstance(locale)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { verLineasVenta = !verLineasVenta },
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = ("Venta #") + (venta?.id),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = buildAnnotatedString {
                        append("Total: ")
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                            append(formatoDinero.format(venta?.precioFinal ?: 0.00))
                        }
                    },
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) { append("Fecha: ") }

                    withStyle(
                        style = SpanStyle(
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    ) { append((venta?.fecha) + " " + (venta?.hora)) }
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = ("Dirección de envío: "),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    fontWeight = FontWeight.Bold
                )

                Column (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = (venta?.direccion?.calle) + " " + (venta?.direccion?.numero) + " " + (venta?.direccion?.piso + ", "),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp),
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.inverseSurface
                    )

                    Text(
                        text = ((venta?.direccion?.ciudad) + ", " + (venta?.direccion?.provincia) + " | " + (venta?.direccion?.codigoPostal)),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp),
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.inverseSurface
                    )
                }
            }

            AnimatedVisibility(
                visible = verLineasVenta,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 4.dp, end = 4.dp, bottom = 0.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
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
                                .padding(8.dp)
                        ) {
                            lineas.forEachIndexed { index, linea ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Column {
                                        Text(
                                            text = linea.libro?.titulo?.let {
                                                if (it.length > 30) it.take(30) + "..." else it
                                            } ?: "Libro",
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = ("- ") + (linea.libro?.autor?.nombre?.let {
                                                if (it.length > 30) it.take(30) + "..." else it
                                            } ?: "Autor"),
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "Cantidad: " + (linea.cantidad),
                                            fontWeight = FontWeight.Thin,
                                            fontSize = 10.sp
                                        )
                                    }

                                    Text(
                                        text = "+" + formatoDinero.format(linea.precioTotal),
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }

                                if (index != lineas.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
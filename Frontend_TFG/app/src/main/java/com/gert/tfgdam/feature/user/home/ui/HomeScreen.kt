package com.gert.tfgdam.feature.user.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.tipolibro.model.TipoLibro
import com.gert.tfgdam.core.navigation.routes.Routes
import com.gert.tfgdam.feature.user.home.viewmodel.HomeViewModel
import com.gert.tfgdam.ui.theme.estiloreutilizable.item.LibroItem
import kotlinx.coroutines.delay

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
            item {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(0.7f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(top = 25.dp, bottom = 25.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Bienvenido",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "a Librerías Gert",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(top = 20.dp, bottom = 19.dp)
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { navController.navigate(Routes.LIBROS_NOVEDADES) },
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "Novedades del mes",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
            }

            librosAgrupados.forEach { (tipo, librosDelTipo) ->

                item {
                    Text(
                        text = tipo?.nombre ?: "Sin nombre",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { navController.navigate(Routes.TIPO_LIBRO_GENEROS.replace("{tipoLibro}", tipo?.nombre ?: "Sin nombre")) }
                    )
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {

                        items(librosDelTipo) { libro ->
                            LibroItem(libro, false, navController)
                        }

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

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
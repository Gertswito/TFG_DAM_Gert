package com.gert.tfgdam.feature.user.libro.portipo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.core.navigation.routes.Routes
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.user.libro.portipo.viewmodel.TipoLibroGenerosViewModel
import com.gert.tfgdam.ui.theme.estiloreutilizable.item.LibroItem
import com.gert.tfgdam.ui.theme.estiloreutilizable.textfield.TextFieldBuscador
import kotlin.collections.component1

@Composable
fun TipoLibroGenerosScreen(
    modifier: Modifier,
    tipoLibroString: String,
    viewModel: TipoLibroGenerosViewModel = viewModel(),
    navController: NavController
) {
    val librosPorTipo = viewModel.librosPorTipo
    val librosFiltradosPorTipo = viewModel.librosFiltradosPorTipo
    val showEmpty by remember(librosPorTipo) { mutableStateOf(librosPorTipo.isEmpty()) }

    LaunchedEffect(tipoLibroString) {
        viewModel.cargarLibrosPorTipo(tipoLibroString)
    }

    Column (modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = tipoLibroString,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        TextFieldBuscador(
            value = viewModel.buscador,
            onValueChange = { viewModel.onBuscadorChange(it) },
            placeholder = "Buscar libros",
            modifier = Modifier.padding(horizontal = 15.dp)
        )

        if (viewModel.isLoadingBusqueda) {
            Text(
                text = "Buscando...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        } else if (viewModel.buscador.isNotEmpty() && librosFiltradosPorTipo.isEmpty() && !viewModel.isLoadingBusqueda) {
            Text(
                text = "No hay resultados",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                textAlign = TextAlign.Center
            )
        } else if (librosPorTipo.isEmpty() && showEmpty) {
            Text(
                text = "No hay libros disponibles, vuelve a intentarlo más tarde",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                textAlign = TextAlign.Center
            )
        } else {
            val librosPorGenero: Map<String, List<Libro>>

            if (viewModel.buscador.isEmpty()) {
                librosPorGenero = librosPorTipo
                    .flatMap { libro ->
                        libro.generos.map { genero ->
                            (genero.nombre ?: "Sin género") to libro
                        }
                    }
                    .groupBy(
                        keySelector = { it.first },
                        valueTransform = { it.second }
                    )
                    .toSortedMap()

                librosPorGenero.mapValues { (_, libros) ->
                    libros.take(5)
                }
            } else {
                librosPorGenero = librosFiltradosPorTipo
                    .flatMap { libro ->
                        libro.generos.map { genero ->
                            (genero.nombre ?: "Sin género") to libro
                        }
                    }
                    .groupBy(
                        keySelector = { it.first },
                        valueTransform = { it.second }
                    )
                    .toSortedMap()
            }

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                librosPorGenero.forEach { (genero, librosPorGenero) ->
                    item {
                        Text(
                            text = buildAnnotatedString {
                                append("- ")
                                withStyle(
                                    style = SpanStyle(textDecoration = TextDecoration.Underline)
                                ) { append(genero ?: "Sin género") }
                            },
                            fontSize = 24.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { navController.navigate(Routes.TIPO_LIBRO_GENERO_SELECTED.replace("{tipoLibro}", tipoLibroString).replace("{genero}", genero ?: "null")) }
                        )
                    }

                    item {
                        LazyRow {
                            items(librosPorGenero) { libro ->
                                LibroItem(libro, false, navController)
                            }

                            item {
                                Card(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .width(150.dp)
                                        .height(270.dp)
                                        .clickable{ navController.navigate(Routes.TIPO_LIBRO_GENERO_SELECTED.replace("{tipoLibro}", tipoLibroString).replace("{genero}", genero ?: "null")) },
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
}
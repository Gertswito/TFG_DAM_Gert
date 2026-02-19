package com.gert.tfgdam.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.viewmodel.TipoLibroGenerosViewModel
import kotlin.collections.component1

@Composable
fun TipoLibroGenerosScreen(
    modifier: Modifier,
    tipoLibroString: String,
    viewModel: TipoLibroGenerosViewModel = viewModel(),
    navController: NavController
) {
    val librosPorTipo = viewModel.librosPorTipo
    val showEmpty by remember(librosPorTipo) { mutableStateOf(librosPorTipo.isEmpty()) }

    LaunchedEffect(tipoLibroString) {
        viewModel.cargarLibrosPorTipo(tipoLibroString)
    }

    if (librosPorTipo.isEmpty() && showEmpty) {
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
        val librosPorGenero = librosPorTipo
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

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = tipoLibroString,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            librosPorGenero.forEach { (genero) ->

                item {
                    Text(
                        text = ("- ") + (genero ?: "Sin género"),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable{ navController.navigate(Routes.TIPO_LIBRO_GENERO_SELECTED.replace("{tipoLibro}", tipoLibroString).replace("{genero}", genero ?: "null")) }
                    )
                }

                item {
                    LazyRow {
                        items(librosPorTipo) { libro ->
                            LibroItem(libro, navController)
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
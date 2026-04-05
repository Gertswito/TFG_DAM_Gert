package com.gert.tfgdam.feature.user.libro.portipoygenero.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.feature.user.libro.portipoygenero.viewmodel.TipoLibroGeneroSelectedViewModel
import com.gert.tfgdam.ui.theme.estiloreutilizable.item.LibroItem
import com.gert.tfgdam.ui.theme.estiloreutilizable.textfield.TextFieldBuscador

@Composable
fun TipoLibroGeneroSelectedScreen (
    tipoLibroString: String,
    generoString: String,
    viewModel: TipoLibroGeneroSelectedViewModel = viewModel(),
    navController: NavController
){
    val librosPorTipoGenero = viewModel.librosPorTipoGenero
    val librosFiltradosPorTipoGenero = viewModel.librosFiltradosPorTipoGenero
    var showEmpty by remember (librosPorTipoGenero) { mutableStateOf(librosPorTipoGenero.isEmpty()) }

    LaunchedEffect(tipoLibroString) {
        viewModel.cargarLibrosPorTipoGenero(tipoLibroString, generoString)
    }

    Column (modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)) {
                    append(tipoLibroString)
                }
                append(" - ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = 24.sp)) {
                    append(generoString)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 15.dp, end = 15.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

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
        } else if (viewModel.buscador.isNotEmpty() && librosFiltradosPorTipoGenero.isEmpty() && !viewModel.isLoadingBusqueda) {
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
        } else if (librosPorTipoGenero.isEmpty() && showEmpty) {
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
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (viewModel.buscador.isEmpty()) {
                    items(librosPorTipoGenero) { libro ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            LibroItem(libro, false, navController)
                        }
                    }
                } else {
                    items(librosFiltradosPorTipoGenero) { libro ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            LibroItem(libro, false, navController)
                        }
                    }
                }
            }
        }
    }
}
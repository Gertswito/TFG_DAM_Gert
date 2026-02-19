package com.gert.tfgdam.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.viewmodel.TipoLibroGeneroSelectedViewModel
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.grid.items

@Composable
fun TipoLibroGeneroSelectedScreen (
    tipoLibroString: String,
    generoString: String,
    modifier: Modifier = Modifier,
    viewModel: TipoLibroGeneroSelectedViewModel = viewModel(),
    navController: NavController
){
    val librosPorTipoGenero = viewModel.librosPorTipoGenero
    var showEmpty by remember { mutableStateOf(false) }

    LaunchedEffect(tipoLibroString) {
        viewModel.cargarLibrosPorTipoGenero(tipoLibroString, generoString)
    }

    LaunchedEffect(tipoLibroString) {
        if (librosPorTipoGenero.isEmpty()) {
            delay(200)
            showEmpty = true
        } else {
            showEmpty = false
        }
    }

    if (librosPorTipoGenero.isEmpty() && showEmpty) {
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
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 140.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
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
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(librosPorTipoGenero) { libro ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LibroItem(libro)
                }
            }
        }
    }
}
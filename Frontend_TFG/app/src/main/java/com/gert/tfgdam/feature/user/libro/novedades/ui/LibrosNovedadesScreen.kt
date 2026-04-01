package com.gert.tfgdam.feature.user.libro.novedades.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.feature.user.libro.novedades.viewmodel.LibrosNovedadesViewModel
import com.gert.tfgdam.ui.theme.estiloreutilizable.item.LibroItem
import kotlinx.coroutines.delay

@Composable
fun LibrosNovedadesScreen(
    viewModel: LibrosNovedadesViewModel = viewModel(),
    navController: NavController
){
    val librosNovedadesPorMes = viewModel.librosNovedadesPorMes
    val librosNovedadesPorUltimaAdicion = viewModel.librosNovedadesPorUltimaAdicion
    val mesActual = viewModel.mesActual
    var showEmptyPorMes by remember { mutableStateOf(false) }
    var showEmptyPorUltimaAdicion by remember { mutableStateOf(false) }

    LaunchedEffect(librosNovedadesPorMes, librosNovedadesPorUltimaAdicion) {
        if (librosNovedadesPorMes.isEmpty()) {
            delay(200)
            showEmptyPorMes = true
        } else {
            showEmptyPorMes = false
        }

        if (librosNovedadesPorUltimaAdicion.isEmpty()) {
            delay(200)
            showEmptyPorUltimaAdicion = true
        } else {
            showEmptyPorUltimaAdicion = false
        }
    }

    if (librosNovedadesPorMes.isEmpty() && showEmptyPorMes && librosNovedadesPorUltimaAdicion.isEmpty() && showEmptyPorUltimaAdicion) {
        Box(
            modifier = Modifier
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (mesActual != "NINGUNO" && !showEmptyPorMes) {
                item {
                    Text(
                        text = ("Novedades de ") + (mesActual),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {

                        items(librosNovedadesPorMes) { libro ->
                            LibroItem(libro, false, navController)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (!showEmptyPorUltimaAdicion) {
                item {
                    Text(
                        text = "Recientemente añadidos",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {

                        items(librosNovedadesPorUltimaAdicion) { libro ->
                            LibroItem(libro, false, navController)
                        }
                    }
                }
            }
        }
    }
}
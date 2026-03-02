package com.gert.tfgdam.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.util.JwtManager
import com.gert.tfgdam.viewmodel.LibroDetailsViewModel
import com.gert.tfgdam.viewmodel.ListaDeseadosViewModel

@Composable
fun LibroDetailsScreen(
    libroId: Long,
    viewModel: LibroDetailsViewModel = viewModel(),
    listaDeseadosViewModel: ListaDeseadosViewModel = viewModel()
) {
    val libroEspecifico = viewModel.libroEspecifico
    var showEmpty by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userInfo by JwtManager.getUserInfoFlow(context).collectAsState(initial = null)

    LaunchedEffect(libroId, userInfo) {
        viewModel.cargarLibrosDetail(libroId)
        userInfo?.sub?.let { usuario ->
            listaDeseadosViewModel.buscarLibroEnListaDeseados(libroId, usuario)
        }
    }

    if (libroEspecifico == null && showEmpty) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No se ha seleccionado ningún libro",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = libroEspecifico?.titulo ?: "Sin título",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 34.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = libroEspecifico?.portada ?: "",
                        contentDescription = libroEspecifico?.titulo,
                        modifier = Modifier
                            .width(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            item {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 0.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = ("Autor: ") + (libroEspecifico?.autor?.nombre ?: "N/A"),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = ("Editorial: ") + (libroEspecifico?.editorial?.nombre ?: "N/A"),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = ("Fecha de salida: ") + (libroEspecifico?.fechaSalida ?: "01/01/2026"),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            item {
                if(libroEspecifico !== null) {
                    Column (
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BotonAddListaDeseados(libroEspecifico, listaDeseadosViewModel.isLibroYaDeseado)
                        BotonAddCarrito(libroEspecifico, true)
                    }
                }
            }

            if (libroEspecifico?.descripcion != null && libroEspecifico.descripcion != "") {
                item {
                    Text(
                        text = libroEspecifico.descripcion ?: "SIN DESCRIPCIÓN",
                        fontSize = 16.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            item {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 0.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                if (libroEspecifico?.generos?.isNotEmpty() ?: true) {
                    Text(
                        text = "Géneros:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = Int.MAX_VALUE
                    ) {
                        libroEspecifico?.generos?.forEach { genero ->
                            Box(
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = genero.nombre ?: "Sin genero",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
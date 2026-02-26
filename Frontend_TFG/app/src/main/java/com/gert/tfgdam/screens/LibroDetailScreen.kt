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
import com.gert.tfgdam.model.JwtPayload
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.util.JwtManager
import com.gert.tfgdam.viewmodel.LibroDetailsViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LibroDetailsScreen(
    libroId: Long,
    modifier: Modifier = Modifier,
    viewModel: LibroDetailsViewModel = viewModel()
) {
    val libroEspecifico = viewModel.libroEspecifico
    var showEmpty by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userInfo by JwtManager.getUserInfoFlow(context).collectAsState(initial = null)

    LaunchedEffect(libroId, userInfo) {
        viewModel.cargarLibrosDetail(libroId)
        userInfo?.sub?.let { usuario ->
            viewModel.buscarLibroEnListaDeseados(libroId, usuario)
        }
    }

    if (libroEspecifico == null && showEmpty) {
        Box(
            modifier = modifier
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
            modifier = modifier
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
                    modifier = Modifier.fillMaxWidth()
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
                        .padding(vertical = 16.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    libroEspecifico?.autor?.let {
                        Text("Autor: ${it.nombre}", fontWeight = FontWeight.Medium)
                    }
                    libroEspecifico?.editorial?.let {
                        Text("Editorial: ${it.nombre}", fontWeight = FontWeight.Medium)
                    }
                    libroEspecifico?.fechaSalida?.let {
                        Text("Fecha de salida: $it", fontWeight = FontWeight.Medium)
                    }
                }
            }

            item {
                if(libroEspecifico !== null) {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BotonAddListaDeseados(viewModel, userInfo, libroEspecifico, viewModel.isLibroYaDeseado)
                        BotonAddCarritoDesdeDetails(libroEspecifico, userInfo)
                    }
                }
            }

            item {
                libroEspecifico?.descripcion?.let { desc ->
                    Text(
                        text = desc,
                        fontSize = 16.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            item {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
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
                        modifier = Modifier.padding(vertical = 4.dp)
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

@Composable
fun BotonAddCarritoDesdeDetails(
    libroEspecifico: Libro,
    userInfo: JwtPayload? = null,
) {
    val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()
    val formatoDinero = NumberFormat.getCurrencyInstance(locale)
    val esUser = userInfo?.rol == "USER"

    Button(
        modifier = Modifier.fillMaxWidth(),
        enabled = esUser,
        onClick = { /*TODO*/ },
    ) {
        Text(
            text = ("AÑADIR AL CARRITO - ") + (libroEspecifico.precio?.let {
                formatoDinero.format(
                    it
                )
            } ?: "N/A"),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun BotonAddListaDeseados(
    viewModel: LibroDetailsViewModel = viewModel(),
    userInfo: JwtPayload? = null,
    libroEspecifico: Libro,
    isLibroYaDeseado: Boolean = false,
) {
    val esUser = userInfo?.rol == "USER"

    if (!isLibroYaDeseado) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = esUser && !viewModel.isLoadingDeseado,
            onClick = { viewModel.addLibroListaDeseados(libroEspecifico, userInfo?.sub ?: "") },
        ) {
            if (viewModel.isLoadingDeseado) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = ("AÑADIR A LISTA DE DESEOS"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    } else {
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = esUser && !viewModel.isLoadingDeseado,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            onClick = { viewModel.deleteLibroListaDeseados(libroEspecifico, userInfo?.sub ?: "") },
        ) {
            if (viewModel.isLoadingDeseado) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = ("QUITAR DE LISTA DE DESEOS"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    if (viewModel.errorMessageDeseado !== "") {
        Text(
            text = viewModel.errorMessageDeseado,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
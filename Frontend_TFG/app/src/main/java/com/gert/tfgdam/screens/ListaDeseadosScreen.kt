package com.gert.tfgdam.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gert.tfgdam.model.JwtPayload
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.util.JwtManager
import com.gert.tfgdam.viewmodel.ListaDeseadosViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ListaDeseadosScreen(
    viewModel: ListaDeseadosViewModel = viewModel(),
    navController: NavController
) {
    val librosDeseados = viewModel.libros
    val context = LocalContext.current
    val userInfo by JwtManager.getUserInfoFlow(context).collectAsState(initial = null)

    LaunchedEffect(userInfo?.sub) {
        userInfo?.sub?.let { usuario ->
            viewModel.cargarLibrosListaDeseados(usuario)
        }
    }

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Lista de deseados",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 24.dp)
            )
        }

        if (librosDeseados.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 16.dp),
                contentAlignment = Alignment.TopCenter
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(librosDeseados.sortedBy { it.id }) { libro ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        LibroItem(libro, true, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun BotonAddListaDeseados(
    libroEspecifico: Libro,
    isLibroYaDeseado: Boolean = false,
) {
    val listaDeseadosViewModel: ListaDeseadosViewModel = viewModel()
    val context = LocalContext.current
    val userInfo by JwtManager.getUserInfoFlow(context).collectAsState(initial = null)
    val esUser = userInfo?.rol == "USER"

    if (!isLibroYaDeseado) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = esUser && !listaDeseadosViewModel.isLoading,
            onClick = { listaDeseadosViewModel.addLibroListaDeseados(libroEspecifico, userInfo?.sub ?: "") },
        ) {
            if (listaDeseadosViewModel.isLoading) {
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
            enabled = esUser && !listaDeseadosViewModel.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            onClick = { listaDeseadosViewModel.deleteLibroListaDeseados(libroEspecifico, userInfo?.sub ?: "", true)},
        ) {
            if (listaDeseadosViewModel.isLoading) {
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

    if (listaDeseadosViewModel.errorMessage !== "") {
        Text(
            text = listaDeseadosViewModel.errorMessage,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
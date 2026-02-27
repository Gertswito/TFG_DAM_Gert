package com.gert.tfgdam.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
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
import kotlinx.coroutines.delay
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
                contentPadding = PaddingValues(8.dp)
            ) {
                items(librosDeseados.sortedBy { it.id }) { libro ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        LibroItemEnListaDeseados(libro, userInfo, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun LibroItemEnListaDeseados(
    libro: Libro,
    userInfo: JwtPayload? = null,
    navController: NavController,
    viewModel: ListaDeseadosViewModel = viewModel()
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(160.dp)
            .height(270.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = { navController.navigate(Routes.LIBRO_DETAILS.replace("{libroId}", libro.id.toString())) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = libro.portada,
                    contentDescription = libro.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = libro.titulo ?: "Sin título",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()
                    val formatoDinero = NumberFormat.getCurrencyInstance(locale)
                    Text(
                        text = formatoDinero.format(libro.precio ?: 0.00),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    BotonAñadirCarrito(libro)
                }
            }

            IconButton(
                onClick = { viewModel.deleteLibroListaDeseados(libro, userInfo?.sub ?: "") },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Quitar de lista de deseados",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
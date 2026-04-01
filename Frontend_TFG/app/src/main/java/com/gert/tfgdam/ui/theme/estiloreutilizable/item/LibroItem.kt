package com.gert.tfgdam.ui.theme.estiloreutilizable.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gert.tfgdam.R
import com.gert.tfgdam.core.navigation.routes.Routes
import com.gert.tfgdam.core.util.Jwt.JwtManager
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.user.listadeseados.viewmodel.ListaDeseadosViewModel
import com.gert.tfgdam.ui.theme.estiloreutilizable.button.BotonAddCarrito
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LibroItem(
    libro: Libro,
    isListaDeseados: Boolean = false,
    navController: NavController
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
                if (libro.portada != "") {
                    AsyncImage(
                        model = libro.portada,
                        contentDescription = libro.titulo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.libro_no_encontrado),
                        contentDescription = libro.titulo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentScale = ContentScale.Fit
                    )
                }

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
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Medium,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    BotonAddCarrito(libro)
                }
            }

            if(isListaDeseados) {
                val listaDeseadosViewModel: ListaDeseadosViewModel = viewModel()
                val context = LocalContext.current
                val userInfo by JwtManager.getUserInfoFlow(context).collectAsState(initial = null)

                IconButton(
                    onClick = {
                        listaDeseadosViewModel.vibrar(context)
                        listaDeseadosViewModel.deleteLibroListaDeseados(libro, userInfo?.sub ?: "", false)
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(0.dp)
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
}
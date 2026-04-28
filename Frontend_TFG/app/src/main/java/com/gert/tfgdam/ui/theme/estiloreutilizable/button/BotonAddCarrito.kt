package com.gert.tfgdam.ui.theme.estiloreutilizable.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gert.tfgdam.core.util.Jwt.JwtManager
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.user.carrito.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale

@Composable
fun BotonAddCarrito(
    libroEspecifico: Libro,
    isLibroDetails: Boolean = false,
) {
    val carritoViewModel: CarritoViewModel = viewModel()
    val context = LocalContext.current
    val userInfo by JwtManager.getUserInfoFlow(context).collectAsState(initial = null)
    val esUser = userInfo?.rol == "USER"

    val hoy = LocalDate.now()
    val disponible = (libroEspecifico.stock ?: 0) > 0 &&
            (libroEspecifico.fechaSalida?.let { fecha ->
                val fechaParsed = LocalDate.parse(fecha)
                !fechaParsed.isAfter(hoy)
            } ?: true)

    if (!isLibroDetails) {
        Button(
            modifier = Modifier
                .width(60.dp)
                .height(30.dp),
            enabled = esUser && disponible,
            onClick = {
                carritoViewModel.vibrar(context)
                carritoViewModel.addAlCarrito(libroEspecifico)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Añadir",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    } else {
        val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()
        val formatoDinero = NumberFormat.getCurrencyInstance(locale)
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = esUser && disponible,
            onClick = {
                carritoViewModel.vibrar(context)
                carritoViewModel.addAlCarrito(libroEspecifico)
            },
        ) {
            Text(
                text = ("AÑADIR AL CARRITO - ") + (libroEspecifico.precio?.let { formatoDinero.format(it) } ?: "N/A"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
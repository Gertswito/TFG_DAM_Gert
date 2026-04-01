package com.gert.tfgdam.ui.theme.estiloreutilizable.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.gert.tfgdam.feature.user.listadeseados.viewmodel.ListaDeseadosViewModel

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
            onClick = {
                listaDeseadosViewModel.vibrar(context)
                listaDeseadosViewModel.addLibroListaDeseados(libroEspecifico, userInfo?.sub ?: "")
            },
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
            onClick = {
                listaDeseadosViewModel.vibrar(context)
                listaDeseadosViewModel.deleteLibroListaDeseados(libroEspecifico, userInfo?.sub ?: "", true)
            },
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
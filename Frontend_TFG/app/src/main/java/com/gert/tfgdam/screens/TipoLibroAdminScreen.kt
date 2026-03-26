package com.gert.tfgdam.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.viewmodel.TipoLibroAdminViewModel
import kotlinx.coroutines.delay

@Composable
fun TipoLibroAdminScreen(
    viewModel: TipoLibroAdminViewModel = viewModel(),
    navController: NavController
) {
    val tiposlibros = viewModel.tiposlibros
    var showEmpty by remember { mutableStateOf(false) }

    LaunchedEffect(tiposlibros) {
        if (tiposlibros.isEmpty()) {
            delay(200)
            showEmpty = true
        } else {
            showEmpty = false
        }
    }

    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tipos de libros",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )

                Button (
                    onClick = { navController.navigate(Routes.AUTOR_ADMIN) },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

            }

            if(tiposlibros.isEmpty() && showEmpty) {
                Text(
                    text = "No hay tipos de libros disponibles",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 10.dp)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Id",
                        modifier = Modifier
                            .weight(3f)
                            .padding(start = 8.dp, top = 8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Nombre",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp, top = 8.dp),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 15.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                ) {
                    tiposlibros.forEach { tipolibro ->
                        item{
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tipolibro.id.toString() ?: "",
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp)
                                )

                                Text(
                                    text = tipolibro.nombre ?: "",
                                    textAlign = TextAlign.End,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp)
                                )
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}
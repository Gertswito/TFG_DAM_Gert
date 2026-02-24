package com.gert.tfgdam.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gert.tfgdam.viewmodel.UserSettingsViewModel

@Composable
fun UserSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: UserSettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    val usuario = viewModel.usuarioSesionEntero
    val isLoading = viewModel.isLoading

    LaunchedEffect(Unit) {
        viewModel.cargarUsuarioSesion(context)
    }

    when {
        isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        usuario == null -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se ha encontrado el usuario",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn {
                    item {
                        UserDetailsScreen(usuario, modifier)
                    }

                    item {
                        Spacer(modifier = Modifier.height(15.dp))

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 20.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        UserContrasenhaScreen(usuario, modifier)
                    }

                    item {
                        Spacer(modifier = Modifier.height(15.dp))

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 20.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item  {
                        UserDireccionesScreen(usuario, modifier)
                    }
                }
            }
        }
    }
}
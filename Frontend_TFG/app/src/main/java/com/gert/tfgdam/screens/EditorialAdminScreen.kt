package com.gert.tfgdam.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.model.Editorial
import com.gert.tfgdam.viewmodel.EditorialAdminViewModel
import kotlinx.coroutines.delay

@Composable
fun EditorialAdminScreen(
    viewModel: EditorialAdminViewModel = viewModel(),
    navController: NavController
) {
    val editoriales = viewModel.editoriales
    var showEmpty by remember { mutableStateOf(false) }
    var abrirModal by remember { mutableStateOf(false) }

    LaunchedEffect(editoriales) {
        if (editoriales.isEmpty()) {
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
                    text = "Editoriales",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )

                Button (
                    onClick = { abrirModal = true },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                if (abrirModal) {
                    CrearEditarEditorial(
                        viewModel = viewModel,
                        showDialog = abrirModal,
                        onDismiss = { abrirModal = false },
                        onSave = {
                            abrirModal = false
                        }
                    )
                }
            }

            if(editoriales.isEmpty() && showEmpty) {
                Text(
                    text = "No hay editoriales disponibles",
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
                    editoriales.forEach { editorial ->
                        item{
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = editorial.id.toString() ?: "",
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp)
                                )

                                Text(
                                    text = editorial.nombre ?: "",
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

@Composable
fun CrearEditarEditorial(
    editorial: Editorial? = null,
    viewModel: EditorialAdminViewModel = viewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 35.dp, bottom = 20.dp, start = 8.dp, end = 8.dp)
                            .fillMaxWidth(),
                    ) {
                        if (editorial != null) {
                            Text(
                                text = "Editar",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 40.sp,
                                lineHeight = 40.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            TextFieldEstiloAlternativo(
                                value = viewModel.idEditorial,
                                onValueChange = { viewModel.idEditorial = it },
                                label = "Id",
                                isPassword = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                isEnabled = false
                            )
                        } else {
                            Text(
                                text = "Crear",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 40.sp,
                                lineHeight = 40.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        TextFieldEstiloAlternativo(
                            value = viewModel.nombreEditorial,
                            onValueChange = { viewModel.nombreEditorial = it },
                            label = "Nombre",
                            isPassword = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        if (viewModel.errorMessage !== "") {
                            Text(
                                text = viewModel.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }

                        if (editorial != null) {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                onClick = {
                                    viewModel.editarEditorial {
                                        onSave()
                                    }
                                },
                                enabled = !viewModel.isLoading
                            ) {
                                if (viewModel.isLoading) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Text(
                                        text = "EDITAR",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        } else {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                onClick = {
                                    viewModel.crearEditorial {
                                        onSave()
                                    }
                                },
                                enabled = !viewModel.isLoading
                            ) {
                                if (viewModel.isLoading) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Text(
                                        text = "GUARDAR",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    IconButton(
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                }
            }
        }
    }
}
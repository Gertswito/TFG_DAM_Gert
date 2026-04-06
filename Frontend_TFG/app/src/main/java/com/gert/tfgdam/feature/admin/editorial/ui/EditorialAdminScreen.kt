package com.gert.tfgdam.feature.admin.editorial.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.gert.tfgdam.feature.admin.autor.viewmodel.AutorAdminViewModel
import com.gert.tfgdam.feature.admin.editorial.model.Editorial
import com.gert.tfgdam.feature.admin.editorial.viewmodel.EditorialAdminViewModel
import com.gert.tfgdam.ui.theme.estiloreutilizable.textfield.TextFieldBuscador
import com.gert.tfgdam.ui.theme.estiloreutilizable.textfield.TextFieldEstiloAlternativo
import kotlinx.coroutines.delay

@Composable
fun EditorialAdminScreen(
    viewModel: EditorialAdminViewModel = viewModel(),
) {
    val editoriales = viewModel.editoriales
    val editorialesFiltradas = viewModel.editorialesFiltradas
    var showEmpty by remember { mutableStateOf(false) }
    var abrirModal by remember { mutableStateOf(false) }
    var editorialSeleccionada by remember { mutableStateOf<Editorial?>(null) }
    var idEditorialSelccionada by remember { mutableStateOf<Long?>(null) }

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
                    .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 10.dp),
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
                        onDismiss = {
                            viewModel.restaurarCamposEditorial(null)
                            abrirModal = false
                        },
                        onSave = {
                            abrirModal = false
                        }
                    )
                }
            }

            TextFieldBuscador(
                value = viewModel.buscador,
                onValueChange = { viewModel.onBuscadorChange(it) },
                placeholder = "Buscar editorial",
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            if(viewModel.isLoadingBusqueda) {
                Text(
                    text = "Buscando...",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else if (viewModel.buscador.isNotEmpty() && editorialesFiltradas.isEmpty() && !viewModel.isLoadingBusqueda) {
                Text(
                    text = "No hay resultados",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else if (editoriales.isEmpty() && showEmpty) {
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
                            .weight(1f)
                            .padding(start = 8.dp, top = 8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Nombre",
                        modifier = Modifier
                            .weight(3f)
                            .padding(end = 8.dp, top = 8.dp),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "",
                        modifier = Modifier.width(100.dp)
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
                    if (viewModel.buscador.isEmpty()) {
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
                                            .weight(3f)
                                            .padding(end = 8.dp)
                                    )

                                    Row(
                                        modifier = Modifier.width(100.dp).padding(end = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = { editorialSeleccionada = editorial },
                                            modifier = Modifier.width(50.dp).padding(end = 8.dp),
                                            colors = IconButtonDefaults.iconButtonColors(
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                contentColor = MaterialTheme.colorScheme.onPrimary
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Editar"
                                            )
                                        }

                                        IconButton(
                                            onClick = { idEditorialSelccionada = editorial.id },
                                            modifier = Modifier.width(50.dp),
                                            colors = IconButtonDefaults.iconButtonColors(
                                                containerColor = MaterialTheme.colorScheme.onError,
                                                contentColor = MaterialTheme.colorScheme.onPrimary
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Eliminar"
                                            )
                                        }
                                    }
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    } else {
                        editorialesFiltradas.forEach { editorial ->
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
                                            .weight(3f)
                                            .padding(end = 8.dp)
                                    )

                                    Row(
                                        modifier = Modifier.width(100.dp).padding(end = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = { editorialSeleccionada = editorial },
                                            modifier = Modifier.width(50.dp).padding(end = 8.dp),
                                            colors = IconButtonDefaults.iconButtonColors(
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                contentColor = MaterialTheme.colorScheme.onPrimary
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Editar"
                                            )
                                        }

                                        IconButton(
                                            onClick = { idEditorialSelccionada = editorial.id },
                                            modifier = Modifier.width(50.dp),
                                            colors = IconButtonDefaults.iconButtonColors(
                                                containerColor = MaterialTheme.colorScheme.onError,
                                                contentColor = MaterialTheme.colorScheme.onPrimary
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Eliminar"
                                            )
                                        }
                                    }
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                if (editorialSeleccionada != null) {
                    CrearEditarEditorial(
                        viewModel = viewModel,
                        editorial = editorialSeleccionada,
                        showDialog = true,
                        onDismiss = {
                            viewModel.restaurarCamposEditorial(null)
                            editorialSeleccionada = null
                        },
                        onSave = {
                            editorialSeleccionada = null
                        }
                    )
                }

                if (idEditorialSelccionada != null) {
                    EliminarEditorial(
                        idEditorial = idEditorialSelccionada!!,
                        showDialog = true,
                        onDismiss = {
                            idEditorialSelccionada = null
                        },
                        onSave = {
                            idEditorialSelccionada = null
                        }
                    )
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
        LaunchedEffect(Unit) {
            viewModel.errorMessage = ""

            if (editorial != null) {
                viewModel.restaurarCamposEditorial(editorial)
            }
        }

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

                            Spacer(modifier = Modifier.height(20.dp))
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
                        onClick = {
                            viewModel.restaurarCamposEditorial(null)
                            onDismiss()
                        },
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

@Composable
fun EliminarEditorial(
    idEditorial: Long,
    viewModel: EditorialAdminViewModel = viewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    if (showDialog) {
        LaunchedEffect(Unit) {
            viewModel.errorMessage = ""
        }

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
                        Text(
                            text = "Eliminar",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 40.sp,
                            lineHeight = 40.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = ("¿Seguro que quieres borrar la editorial con id ") + (idEditorial.toString()) + "?",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (viewModel.errorMessage !== "") {
                            Text(
                                text = viewModel.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            onClick = {
                                viewModel.eliminarEditorial(idEditorial) {
                                    onSave()
                                }
                            },
                            enabled = !viewModel.isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onError,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            if (viewModel.isLoading) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "ELIMINAR",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = {
                            onDismiss()
                        },
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
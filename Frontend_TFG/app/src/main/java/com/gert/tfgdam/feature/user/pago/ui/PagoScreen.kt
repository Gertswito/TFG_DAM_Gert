package com.gert.tfgdam.feature.user.pago.ui

import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.feature.user.carrito.viewmodel.CarritoViewModel
import com.gert.tfgdam.feature.user.usersettings.direcciones.ui.DireccionItem
import com.gert.tfgdam.feature.user.usersettings.direcciones.ui.EditarDireccionModal
import com.gert.tfgdam.feature.user.pago.viewmodel.PagoViewModel
import com.gert.tfgdam.feature.user.usersettings.direcciones.viewmodel.UserDireccionesViewModel
import com.gert.tfgdam.feature.user.usersettings.viewmodel.UserSettingsViewModel
import com.gert.tfgdam.ui.theme.estiloreutilizable.item.LibroItemCarrito
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PagoScreen(
    navController: NavController,
    viewModel: PagoViewModel = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel(),
    userSettingsViewModel: UserSettingsViewModel = viewModel(),
    userDireccionesViewModel: UserDireccionesViewModel = viewModel()
){
    val context = LocalContext.current
    val carritoItems by carritoViewModel.carritoItems.collectAsState()
    var total by remember { mutableStateOf(0.0) }
    val usuario = userSettingsViewModel.usuarioSesionEntero

    LaunchedEffect(carritoItems, usuario) {
        total = carritoViewModel.calcularTotal()

        if (usuario == null) {
            userSettingsViewModel.cargarUsuarioSesion(context)
        }

        usuario?.let {
            userDireccionesViewModel.setDireccionesIniciales(it, it.direcciones)
        }
    }

    LazyColumn (
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(23.dp))

            var expanded by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { expanded = !expanded },
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Carrito",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )

                        val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()
                        val formatoDinero = NumberFormat.getCurrencyInstance(locale)
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onBackground)) {
                                    append("Total: ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary)) {
                                    append(formatoDinero.format(total))
                                }
                            }
                        )
                    }

                    AnimatedVisibility(
                        visible = expanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        val maxHeight = 275.dp
                        val minHeight = 150.dp
                        val dynamicHeight = if (carritoItems.size >= 2) maxHeight else minHeight

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = minHeight, max = dynamicHeight)
                        ) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp, horizontal = 10.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                items(carritoItems, key = { it.libro.id!! }) { item ->
                                    LibroItemCarrito(item)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp),
                        contentAlignment = Alignment.CenterStart
                    ){
                        Text(
                            text = "Dirección de envío",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp)
                    ) {
                        if (userDireccionesViewModel.direcciones.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(210.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No se han encontrado direcciones",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 300.dp, min = 210.dp)
                            ) {
                                items(items = userDireccionesViewModel.direcciones, key = { it.id!! }) { direccion ->
                                    DireccionItem(direccion, true)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            onClick = { userDireccionesViewModel.clickAbrirModalOCerrar() }
                        ) {
                            Text(
                                text = "AÑADIR DIRECCIÓN",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        if (userDireccionesViewModel.abrirModalEditarDireccion) {
                            EditarDireccionModal(
                                showDialog = userDireccionesViewModel.abrirModalEditarDireccion,
                                direccionEditar = null,
                                viewModel = userDireccionesViewModel,
                                onDismiss = {
                                    userDireccionesViewModel.abrirModalEditarDireccion = false
                                },
                                onSave = { direccionActualizada ->
                                    userDireccionesViewModel.actualizarDireccion(
                                        direccionActualizada
                                    )
                                    userDireccionesViewModel.abrirModalEditarDireccion = false
                                }
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .height(115.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Método de pago: PayPal",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val scope = rememberCoroutineScope()

                        Button(
                            onClick = {
                                scope.launch {
                                    val stockOk = viewModel.validarStock(carritoItems)
                                    if (!stockOk) return@launch
                                    viewModel.iniciarProcesoPago(carritoItems, userDireccionesViewModel.direccionSeleccionada!!, usuario!!)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = userDireccionesViewModel.direccionSeleccionada != null && carritoItems.isNotEmpty() && usuario != null && !viewModel.isLoading
                        ) {
                            Text(
                                text = "FINALIZAR COMPRA",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        val context = LocalContext.current
                        val approvalUrl = viewModel.approvalUrl

                        LaunchedEffect(approvalUrl) {
                            approvalUrl?.let {
                                val customTabsIntent = CustomTabsIntent.Builder().build()
                                customTabsIntent.launchUrl(context, Uri.parse(it))
                            }
                        }

                        if (viewModel.errorMessage !== "") {
                            LaunchedEffect(viewModel.errorMessage) {
                                Toast.makeText(context, viewModel.errorMessage, Toast.LENGTH_LONG).show()
                                viewModel.errorMessage = ""
                            }
                        }
                        if (viewModel.successMessage !== "") {
                            LaunchedEffect(viewModel.successMessage) {
                                Toast.makeText(context, viewModel.successMessage, Toast.LENGTH_LONG).show()
                                viewModel.successMessage = ""
                            }
                        }
                    }
                }
            }
        }
    }
}
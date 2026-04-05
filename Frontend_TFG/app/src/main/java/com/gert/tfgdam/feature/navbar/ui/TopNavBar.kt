package com.gert.tfgdam.feature.navbar.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gert.tfgdam.R
import com.gert.tfgdam.feature.navbar.navitem.NavItemList
import com.gert.tfgdam.core.navigation.routes.Routes
import com.gert.tfgdam.core.util.Jwt.JwtManager
import com.gert.tfgdam.feature.user.carrito.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(
    navController: NavController,
    jwtManager: JwtManager = JwtManager,
    carritoViewModel: CarritoViewModel = viewModel()
) {
    val context = LocalContext.current
    val token by jwtManager.getToken(context).collectAsState(initial = null)
    val role = token?.let { JwtManager.getUserInfoFromToken(it)?.rol }
    val navItems by NavItemList.getNavItems(context).collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    val carritoItems by carritoViewModel.carritoItems.collectAsState()
    val totalCarrito = carritoItems.sumOf { it.cantidad }

    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Image(
                painter = painterResource(id = R.drawable.librerias_gert_full_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(28.dp)
                    .clickable { when(role) {
                        "ADMIN" -> navController.navigate(Routes.HOME_ADMIN) {
                            popUpTo(Routes.HOME_ADMIN) { inclusive = true }
                        }
                        else -> navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }}
            )
        },
        actions = {
            navItems.forEach { navItem ->
                if (role == "ADMIN" && navItem.route == Routes.CARRITO) return@forEach

                if (navItem.route == Routes.CARRITO) {
                    IconButton(onClick = { navController.navigate(navItem.route) }) {
                        BadgedBox(
                            modifier = Modifier.padding(top = 4.dp),
                            badge = {
                                if (totalCarrito > 0 && totalCarrito < 10) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary,
                                    ) {
                                        Text(
                                            text = totalCarrito.toString(),
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            textAlign = TextAlign.Center,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                } else if (totalCarrito >= 10 && totalCarrito <= 99) {
                                    Badge(
                                        modifier = Modifier.offset(x = (-4).dp, y = (0).dp),
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary,
                                    ) {
                                        Text(
                                            text = totalCarrito.toString(),
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            textAlign = TextAlign.Center,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                } else if (totalCarrito > 99) {
                                    Badge(
                                        modifier = Modifier.offset(x = (-11).dp, y = (0).dp),
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary,
                                    ) {
                                        Text(
                                            text = "+99",
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            textAlign = TextAlign.Center,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = navItem.label
                            )
                        }
                    }
                } else {
                    IconButton(onClick = { navController.navigate(navItem.route) }) {
                        Icon(
                            imageVector = navItem.icon,
                            contentDescription = navItem.label
                        )
                    }
                }
            }

            if (!token.isNullOrEmpty()) {
                IconButton(
                    onClick = {
                        carritoViewModel.vibrar(context)
                        coroutineScope.launch {
                            jwtManager.clearToken(context)
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.HOME) { inclusive = true }
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                    )
                }
            }
        }
    )
}
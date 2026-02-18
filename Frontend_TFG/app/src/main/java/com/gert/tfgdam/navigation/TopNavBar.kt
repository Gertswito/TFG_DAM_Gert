package com.gert.tfgdam.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gert.tfgdam.R
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.util.JwtManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(navController: NavController, jwtManager: JwtManager = JwtManager) {
    val context = LocalContext.current
    val token by jwtManager.getToken(context).collectAsState(initial = null)
    val role = token?.let { JwtManager.getUserInfoFromToken(it)?.rol }
    val navItems by NavItemList.getNavItems(context).collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

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
                IconButton(onClick = { navController.navigate(navItem.route) }) {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.label
                    )
                }
            }

            if (!token.isNullOrEmpty()) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            jwtManager.clearToken(context)
                            navController.navigate(Routes.LOGIN) {
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
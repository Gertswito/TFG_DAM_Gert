package com.gert.tfgdam.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gert.tfgdam.R
import com.gert.tfgdam.routes.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(navController: NavController) {
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
                    .clickable { navController.navigate(Routes.HOME) }
            )
        },
        actions = {
            NavItemList.navItemList.forEach { navItem ->
                IconButton(onClick = { navController.navigate(navItem.route) }) {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.label
                    )
                }
            }
        }
    )
}
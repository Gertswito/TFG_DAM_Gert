package com.gert.tfgdam.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home

object NavItemList {
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Login", Icons.Default.AccountCircle)
    )
}
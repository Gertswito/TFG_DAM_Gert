package com.gert.tfgdam.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import com.gert.tfgdam.routes.Routes

object NavItemList {
    val navItemList = listOf(
        NavItem("Login", Icons.Default.AccountCircle, Routes.LOGIN)
    )
}
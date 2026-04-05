package com.gert.tfgdam.feature.navbar.navitem

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import com.gert.tfgdam.core.navigation.routes.Routes
import com.gert.tfgdam.core.util.Jwt.JwtManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object NavItemList {
    fun getNavItems(context: Context): Flow<List<NavItem>> = flow {
        JwtManager.getToken(context).collect { token ->
            val items = if (!token.isNullOrEmpty()) {
                listOf(
                    NavItem("Carrito", Icons.Default.ShoppingCart, Routes.CARRITO),
                    NavItem("User Details", Icons.Default.AccountBox, Routes.USER_SETTINGS)
                )
            } else {
                listOf(
                    NavItem("Login", Icons.Default.AccountCircle, Routes.LOGIN)
                )
            }
            emit(items)
        }
    }
}
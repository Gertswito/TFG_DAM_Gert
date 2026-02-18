package com.gert.tfgdam.navigation

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.util.JwtManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

object NavItemList {
    fun getNavItems(context: Context): Flow<List<NavItem>> = flow {
        JwtManager.getToken(context).collect { token ->
            val items = if (!token.isNullOrEmpty()) {
                listOf(
                    NavItem("Home", Icons.Default.Home, Routes.HOME)
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
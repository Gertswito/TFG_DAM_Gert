package com.gert.tfgdam.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.screens.HomeScreen
import com.gert.tfgdam.screens.LoginScreen
import com.gert.tfgdam.screens.RegisterScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) { HomeScreen() }

        composable(Routes.LOGIN) { backStackEntry ->
            LoginScreen(
                navController = navController
            )
        }

        composable(Routes.REGISTER) { backStackEntry ->
            RegisterScreen(
                navController = navController
            )
        }
    }
}
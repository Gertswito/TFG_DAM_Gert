package com.gert.tfgdam.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.screens.HomeAdminScreen
import com.gert.tfgdam.screens.HomeScreen
import com.gert.tfgdam.screens.LoginScreen
import com.gert.tfgdam.screens.RegisterScreen
import com.gert.tfgdam.screens.UserDetailsScreen

@Composable
fun AppNavHost(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
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

        composable(Routes.USER_DETAILS) { UserDetailsScreen() }

        composable(Routes.HOME_ADMIN) { HomeAdminScreen() }
    }
}
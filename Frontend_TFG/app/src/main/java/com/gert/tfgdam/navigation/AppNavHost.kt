package com.gert.tfgdam.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.screens.HomeAdminScreen
import com.gert.tfgdam.screens.HomeScreen
import com.gert.tfgdam.screens.LoginScreen
import com.gert.tfgdam.screens.RegisterScreen
import com.gert.tfgdam.screens.TipoLibroGeneroSelectedScreen
import com.gert.tfgdam.screens.TipoLibroGenerosScreen
import com.gert.tfgdam.screens.UserDetailsScreen
import com.gert.tfgdam.viewmodel.TipoLibroGenerosViewModel

@Composable
fun AppNavHost(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.HOME) { HomeScreen(navController = navController) }

        composable(Routes.HOME_ADMIN) { HomeAdminScreen() }

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

        composable(
            route = Routes.TIPO_LIBRO_GENEROS,
            arguments = listOf(
                navArgument("tipoLibro") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val tipoLibro = backStackEntry.arguments?.getString("tipoLibro") ?: ""

            TipoLibroGenerosScreen(
                tipoLibroString = tipoLibro,
                modifier = Modifier,
                navController = navController
            )
        }

        composable(
            route = Routes.TIPO_LIBRO_GENERO_SELECTED,
            arguments = listOf(
                navArgument("tipoLibro") { type = NavType.StringType },
                navArgument("genero") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val tipoLibro = backStackEntry.arguments?.getString("tipoLibro") ?: ""
            val genero = backStackEntry.arguments?.getString("genero") ?: ""

            TipoLibroGeneroSelectedScreen(
                tipoLibroString = tipoLibro,
                generoString = genero,
                modifier = Modifier,
                navController = navController
            )
        }
    }
}
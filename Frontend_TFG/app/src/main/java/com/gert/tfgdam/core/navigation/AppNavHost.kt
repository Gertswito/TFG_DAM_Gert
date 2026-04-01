package com.gert.tfgdam.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gert.tfgdam.core.navigation.routes.Routes
import com.gert.tfgdam.feature.admin.autor.ui.AutorAdminScreen
import com.gert.tfgdam.feature.user.carrito.ui.CarritoScreen
import com.gert.tfgdam.feature.admin.cliente.ui.ClienteAdminScreen
import com.gert.tfgdam.feature.user.comprafinalizada.CompraFinalizadaScreen
import com.gert.tfgdam.feature.admin.editorial.ui.EditorialAdminScreen
import com.gert.tfgdam.feature.admin.genero.ui.GeneroAdminScreen
import com.gert.tfgdam.feature.user.historialcompra.ui.HistorialCompraScreen
import com.gert.tfgdam.feature.admin.homeadmin.ui.HomeAdminScreen
import com.gert.tfgdam.feature.user.home.ui.HomeScreen
import com.gert.tfgdam.feature.admin.libro.ui.LibroAdminScreen
import com.gert.tfgdam.feature.user.libro.detail.ui.LibroDetailsScreen
import com.gert.tfgdam.feature.user.libro.novedades.ui.LibrosNovedadesScreen
import com.gert.tfgdam.feature.user.libro.porautor.ui.LibrosPorAutorScreen
import com.gert.tfgdam.feature.user.libro.poreditorial.ui.LibrosPorEditorialScreen
import com.gert.tfgdam.feature.user.listadeseados.ui.ListaDeseadosScreen
import com.gert.tfgdam.feature.login.ui.LoginScreen
import com.gert.tfgdam.feature.user.pago.ui.PagoScreen
import com.gert.tfgdam.feature.register.ui.RegisterScreen
import com.gert.tfgdam.feature.admin.tipolibro.ui.TipoLibroAdminScreen
import com.gert.tfgdam.feature.user.libro.portipoygenero.ui.TipoLibroGeneroSelectedScreen
import com.gert.tfgdam.feature.user.libro.portipo.ui.TipoLibroGenerosScreen
import com.gert.tfgdam.feature.user.usersettings.ui.UserSettingsScreen
import com.gert.tfgdam.feature.admin.venta.ui.VentaAdminScreen

@Composable
fun AppNavHost(navController: NavHostController, startDestination: String, orderIdPaypal: String?, ) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Routes.HOME) { HomeScreen(navController = navController) }

        composable(Routes.HOME_ADMIN) { HomeAdminScreen(navController = navController) }

        composable(Routes.LOGIN) { backStackEntry -> LoginScreen(navController = navController) }

        composable(Routes.REGISTER) { backStackEntry -> RegisterScreen(navController = navController) }

        composable(Routes.USER_SETTINGS) { UserSettingsScreen() }

        composable(
            route = Routes.TIPO_LIBRO_GENEROS,
            arguments = listOf(
                navArgument("tipoLibro") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val tipoLibro = backStackEntry.arguments?.getString("tipoLibro") ?: ""

            TipoLibroGenerosScreen(tipoLibroString = tipoLibro, modifier = Modifier, navController = navController)
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

            TipoLibroGeneroSelectedScreen(tipoLibroString = tipoLibro, generoString = genero, navController = navController)
        }

        composable(
            route = Routes.LIBRO_DETAILS,
            arguments = listOf(
                navArgument("libroId") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val libroId = backStackEntry.arguments?.getString("libroId") ?: ""

            LibroDetailsScreen(libroId = libroId.toLong(), navController = navController)
        }

        composable(
            route = Routes.LIBROS_POR_AUTOR,
            arguments = listOf(
                navArgument("autor") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val autor = backStackEntry.arguments?.getString("autor") ?: ""

            LibrosPorAutorScreen(autor = autor, navController = navController)
        }

        composable(
            route = Routes.LIBROS_POR_EDITORIAL,
            arguments = listOf(
                navArgument("editorial") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val editorial = backStackEntry.arguments?.getString("editorial") ?: ""

            LibrosPorEditorialScreen(editorial = editorial, navController = navController)
        }

        composable(Routes.LIBROS_NOVEDADES) { LibrosNovedadesScreen(navController = navController) }

        composable(Routes.CARRITO) { CarritoScreen(navController = navController) }

        composable(Routes.HISTORIAL_COMPRA) { HistorialCompraScreen() }

        composable(Routes.LISTA_DESEADOS) { ListaDeseadosScreen(navController = navController) }

        composable(Routes.PAGO) { PagoScreen(navController = navController) }

        composable(
            Routes.COMPRA_FINALIZADA,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""

            CompraFinalizadaScreen(navController = navController, orderId = orderId)
        }

        composable(Routes.AUTOR_ADMIN) { AutorAdminScreen() }

        composable(Routes.EDITORIAL_ADMIN) { EditorialAdminScreen() }

        composable(Routes.TIPO_LIBRO_ADMIN) { TipoLibroAdminScreen() }

        composable(Routes.CLIENTE_ADMIN) { ClienteAdminScreen() }

        composable(Routes.LIBRO_ADMIN) { LibroAdminScreen() }

        composable(Routes.VENTA_ADMIN) { VentaAdminScreen() }

        composable(Routes.GENERO_ADMIN) { GeneroAdminScreen() }
    }

    LaunchedEffect(orderIdPaypal) {
        orderIdPaypal?.let { orderId ->
            navController.navigate(Routes.COMPRA_FINALIZADA.replace("{orderId}", orderId)) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
}
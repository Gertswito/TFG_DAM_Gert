package com.gert.tfgdam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.gert.tfgdam.navigation.AppNavHost
import com.gert.tfgdam.navigation.TopNavBar
import com.gert.tfgdam.routes.Routes
import com.gert.tfgdam.ui.theme.TFGDAMGertTheme
import com.gert.tfgdam.util.JwtManager
import kotlinx.coroutines.flow.firstOrNull

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val tokenFlow = JwtManager.getToken(context)
            val tokenState = produceState<String?>(initialValue = null, key1 = tokenFlow) {
                value = tokenFlow.firstOrNull()
            }
            val token = tokenState.value
            val role = token?.let { JwtManager.getUserInfoFromToken(it)?.rol }

            TFGDAMGertTheme {
                Scaffold(
                    topBar = { TopNavBar(navController) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        if (token == null) {
                            CircularProgressIndicator()
                        } else {
                            AppNavHost(
                                navController = navController,
                                startDestination = when (role) {
                                    "ADMIN" -> Routes.HOME_ADMIN
                                    "USER" -> Routes.HOME
                                    else -> Routes.HOME
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.gert.tfgdam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.gert.tfgdam.navigation.TopNavBar
import com.gert.tfgdam.screens.HomeScreen
import com.gert.tfgdam.screens.LoginScreen
import com.gert.tfgdam.ui.theme.TFGDAMGertTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TFGDAMGertTheme {
                TopNavScreen()
            }
        }
    }
}

@Composable
fun TopNavScreen() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopNavBar(
                selectedIndex = selectedIndex,
                onHomeClick = { selectedIndex = 0 },
                onLoginClick = { selectedIndex = 1 }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ContentScreen(selectedIndex = selectedIndex)
        }
    }
}

@Composable
fun ContentScreen(selectedIndex: Int) {
    when(selectedIndex) {
        0 -> HomeScreen()
        1 -> LoginScreen()
    }
}
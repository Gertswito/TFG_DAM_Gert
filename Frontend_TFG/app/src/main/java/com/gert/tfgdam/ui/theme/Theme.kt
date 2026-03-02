package com.gert.tfgdam.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val colorScheme = darkColorScheme(
    primary = VerdeOscurillo,
    onPrimary = Blanco,
    secondary = Dorado,
    onSecondary = Blanco,
    tertiary = VerdeOscurillo,
    onTertiary = Blanco,
    background = NegroClarito,
    onBackground = Blanco,
    surface = CasiBlack,
    onSurface = Blanco,
    surfaceVariant = Negro,
    onSurfaceVariant = Blanco,
    inverseSurface = BlancoOscurito,
    inverseOnSurface = CasiBlack,
    onError = ErrorRojo
)

@Composable
fun TFGDAMGertTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
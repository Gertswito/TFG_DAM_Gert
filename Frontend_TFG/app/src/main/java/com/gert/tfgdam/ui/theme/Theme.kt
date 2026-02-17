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

private val DarkColorScheme = darkColorScheme(
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

private val LightColorScheme = lightColorScheme(
    primary = VerdeOscurillo,
    onPrimary = Blanco,
    secondary = Dorado,
    onSecondary = Blanco,
    tertiary = VerdeOscurillo,
    onTertiary = Blanco,
    background = Blanco,
    onBackground = Negro,
    surface = CasiWhite,
    onSurface = Negro,
    surfaceVariant = BlancoOscurito,
    onSurfaceVariant = Negro,
    inverseSurface = NegroClarito,
    inverseOnSurface = CasiWhite,
    onError = ErrorRojo
)

@Composable
fun TFGDAMGertTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
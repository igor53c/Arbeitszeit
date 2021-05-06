package glavni.paket.arbeitszeit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Gray50,
    onPrimary = Gray100,
    primaryVariant = Purple700,
    secondary = Blau50,
    onSecondary = Blau100,
    onSurface = Gray100,
    background = Color.Black,
    error = Color.Red
)

private val LightColorPalette = lightColors(
    primary = Gray100,
    onPrimary = Gray50,
    primaryVariant = Purple700,
    secondary = Blau100,
    onSecondary = Blau50,
    onSurface = Gray50,
    background = Color.White,
    error = Color.Red
)

@Composable
fun ArbeitszeitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
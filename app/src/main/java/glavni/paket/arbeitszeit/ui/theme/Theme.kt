package glavni.paket.arbeitszeit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Blau10,
    onPrimary = Gray100,
    primaryVariant = BlauLight100,
    secondary = BlauLight50,
    secondaryVariant = UltramarineBlue50,
    onSecondary = Gray40,
    onSurface = Blau10,
    background = Color.Black,
    error = Color.Red
)

private val LightColorPalette = lightColors(
    primary = Gray100,
    onPrimary = Blau10,
    primaryVariant = BlauLight100,
    secondary = BlauLight50,
    secondaryVariant = UltramarineBlue50,
    onSecondary = Blau10,
    onSurface = UltramarineBlue50,
    background = Color.White,
    error = Color.Red
)

@Composable
fun ArbeitszeitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
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
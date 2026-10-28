package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
  primary = ShopGreenPrimary,
  onPrimary = Color.White,
  primaryContainer = ShopGreenContainer,
  onPrimaryContainer = ShopOnGreenContainer,
  secondary = ShopGreenLight,
  onSecondary = Color.White,
  secondaryContainer = ShopSurfaceVariant,
  onSecondaryContainer = ShopTextPrimary,
  tertiary = ShopAccentAmber,
  onTertiary = Color.White,
  background = ShopBackground,
  onBackground = ShopTextPrimary,
  surface = ShopSurface,
  onSurface = ShopTextPrimary,
  surfaceVariant = ShopSurfaceVariant,
  onSurfaceVariant = ShopTextSecondary,
  outline = ShopOutline,
  error = ShopAccentCoral,
  onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
  primary = ShopGreenDarkPrimary,
  onPrimary = Color.Black,
  primaryContainer = ShopGreenDarkContainer,
  onPrimaryContainer = Color.White,
  secondary = ShopGreenLight,
  onSecondary = Color.Black,
  secondaryContainer = ShopDarkSurfaceVariant,
  onSecondaryContainer = Color.White,
  tertiary = ShopAccentAmber,
  onTertiary = Color.Black,
  background = ShopDarkBackground,
  onBackground = Color.White,
  surface = ShopDarkSurface,
  onSurface = Color.White,
  surfaceVariant = ShopDarkSurfaceVariant,
  onSurfaceVariant = Color(0xFFCBD5E1),
  outline = Color(0xFF334E42),
  error = ShopAccentCoral,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Default to false so our brand emerald green and crisp white theme is always displayed
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
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

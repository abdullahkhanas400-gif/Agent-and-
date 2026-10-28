package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ShopGreenContainer
import com.example.ui.theme.ShopGreenPrimary

@Composable
fun ProductArtIllustration(
    categoryKey: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 48.dp,
    tintColor: Color = ShopGreenPrimary
) {
    val bgGradients = when (categoryKey) {
        "earbuds" -> listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
        "smartwatch" -> listOf(Color(0xFFE0F2FE), Color(0xFFBAE6FD))
        "tshirt" -> listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A))
        "mug" -> listOf(Color(0xFFFCE7F3), Color(0xFFFBCFE8))
        "shoes" -> listOf(Color(0xFFE6FFFA), Color(0xFFB2F5EA))
        "cream" -> listOf(Color(0xFFDCFCE7), Color(0xFFBBF7D0))
        "honey" -> listOf(Color(0xFFFFFBEB), Color(0xFFFDE68A))
        "charger" -> listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0))
        else -> listOf(ShopGreenContainer, Color(0xFFD1E7DD))
    }

    val icon = when (categoryKey) {
        "earbuds" -> Icons.Default.Headphones
        "smartwatch" -> Icons.Default.Watch
        "tshirt" -> Icons.Default.Checkroom
        "mug" -> Icons.Default.Coffee
        "shoes" -> Icons.Default.DirectionsRun
        "cream" -> Icons.Default.Spa
        "honey" -> Icons.Default.Eco
        "charger" -> Icons.Default.Bolt
        else -> Icons.Default.ShoppingBag
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.radialGradient(bgGradients)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            tint = tintColor
        )
    }
}

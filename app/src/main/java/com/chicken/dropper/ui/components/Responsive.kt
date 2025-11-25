package com.chicken.dropper.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

/**
 * Small helper to keep the vertical-only UI comfortable on devices
 * with different heights and widths.
 */
@Composable
fun rememberVerticalUiScale(
    baseWidthDp: Float = 360f,
    baseHeightDp: Float = 800f,
    minScale: Float = 0.85f,
    maxScale: Float = 1.2f,
): Float {
    val configuration = LocalConfiguration.current
    val widthScale = configuration.screenWidthDp / baseWidthDp
    val heightScale = configuration.screenHeightDp / baseHeightDp
    val baseScale = min(widthScale, heightScale)
    return baseScale.coerceIn(minScale, maxScale)
}

fun Dp.scaled(scale: Float): Dp = (value * scale).dp

fun TextUnit.scaled(scale: Float): TextUnit = (value * scale).sp

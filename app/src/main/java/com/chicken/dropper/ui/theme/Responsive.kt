package com.chicken.dropper.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

/**
 * Calculates a scale factor based on the current screen size compared to the
 * original mock sizes used in the design. This helps keep UI proportions
 * consistent across phones and tablets without reworking every measurement.
 */
@Composable
fun rememberScreenScale(
    baseWidth: Float = 411f,
    baseHeight: Float = 915f
): Float {
    val configuration = LocalConfiguration.current
    val widthScale = configuration.screenWidthDp / baseWidth
    val heightScale = configuration.screenHeightDp / baseHeight
    return min(widthScale, heightScale)
}

/**
 * Quickly scale a dp measurement using the provided factor.
 */
fun Dp.scaled(scale: Float): Dp = (this.value * scale).dp

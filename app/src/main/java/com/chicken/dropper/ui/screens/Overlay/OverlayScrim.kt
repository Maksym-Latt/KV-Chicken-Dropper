package com.chicken.dropper.ui.screens.Overlay

import androidx.compose.animation.core.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Composable
fun rememberDelayedScrimColor(
    targetColor: Color,
    delayMillis: Int = 150,
    durationMillis: Int = 180
): Color {
    var isVisible by remember { mutableStateOf(false) }

    val scrimColor by animateColorAsState(
        targetValue = if (isVisible) targetColor else targetColor.copy(alpha = 0f),
        animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis),
        label = "scrimColor"
    )

    LaunchedEffect(Unit) {
        isVisible = true
    }

    return scrimColor
}

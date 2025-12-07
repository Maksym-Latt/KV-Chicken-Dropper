package com.chicken.dropper.ui.splashscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.chicken.dropper.ui.components.ShinyStrokeLabel
import kotlinx.coroutines.delay

@Composable
fun LoadingTextAnimated(
    modifier: Modifier = Modifier,
    content: String = "LOADING",
    stepMs: Long = 300L
) {
    val order = intArrayOf(1, 2, 3, 0)
    var idx by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(stepMs)
            idx = (idx + 1) % order.size
        }
    }
    val dots = order[idx]

    val display = buildString {
        append(content)
        if (dots > 0) append(" ")
        repeat(dots) { append(".") }
    }

    ShinyStrokeLabel(
        caption = display,
        shineBrush = Brush.verticalGradient(
            listOf(Color(0xFFFFC107), Color(0xFFFFA200))
        ),
        strokeTint = Color(0xFF6A3C00),
        size = 38.sp,
        stroke = 10f,
    )
}
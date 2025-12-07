package com.chicken.dropper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameTitle(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ShinyStrokeLabel(
            caption = "CHICKEN",
            size = 64.sp,
            stroke = 10f,
            strokeTint = Color(0xFF551A32),
            shineBrush = Brush.verticalGradient(
                listOf(Color(0xFFFF88D0), Color(0xFFCA3CC7))
            ),
        )

        ShinyStrokeLabel(
            caption = "Dropper",
            shineBrush = Brush.verticalGradient(
                listOf(Color(0xFFFFC107), Color(0xFFFFC107))
            ),
            strokeTint = Color(0xFF6A3C00),
            size = 30.sp,
            stroke = 10f,
            modifier = Modifier
                .offset(y = (-50).dp)
        )
    }
}

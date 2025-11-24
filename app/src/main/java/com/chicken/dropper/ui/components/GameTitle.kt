package com.chicken.dropper.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GradientOutlinedText(
            text = "CHICKEN",
            gradient = Brush.verticalGradient(listOf(Color(0xFFFFE082), Color(0xFFFFC107))),
            fontSize = 42.sp,
            strokeWidth = 8f,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        GradientOutlinedText(
            text = "DROPPER",
            gradient = Brush.verticalGradient(listOf(Color(0xFFFFF59D), Color(0xFFFFD54F))),
            fontSize = 36.sp,
            strokeWidth = 8f
        )
    }
}

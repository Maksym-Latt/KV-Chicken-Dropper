package com.chicken.dropper.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class ChickenButtonStyle {
    Green,
    Magenta
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ChickenButtonStyle = ChickenButtonStyle.Green
) {
    val (border, gradient) = when (style) {
        ChickenButtonStyle.Green ->
            Color(0xFF4D8F00) to listOf(Color(0xFF9CE33F), Color(0xFF61B500))

        ChickenButtonStyle.Magenta ->
            Color(0xFFB71679) to listOf(Color(0xFFFD84C7), Color(0xFFC23DC9))
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        tonalElevation = 6.dp,
        shadowElevation = 12.dp,
        border = BorderStroke(2.dp, border),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(Brush.verticalGradient(gradient))
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.uppercase(),
                color = Color(0xFFFFFDF3),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
            )
        }
    }
}

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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ChickenButtonStyle {
    Green, Magenta
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ChickenButtonStyle = ChickenButtonStyle.Green
) {
    val (border, gradientMain, gradientHighlight) = when (style) {
        ChickenButtonStyle.Green ->
            Triple(
                Color(0xFF3A6B00),
                listOf(Color(0xFF9EE94F), Color(0xFF5BAF00)),
                listOf(Color(0xFFFFFFFF).copy(alpha = 0.45f), Color.Transparent)
            )

        ChickenButtonStyle.Magenta ->
            Triple(
                Color(0xFF8A1358),
                listOf(Color(0xFF5E2C52), Color(0xFFC23DC9)),
                listOf(Color(0xFFFFFFFF).copy(alpha = 0.45f), Color.Transparent)
            )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable(onClick = onClick)
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color.Black.copy(alpha = 0.4f),
                spotColor = Color.Black.copy(alpha = 0.4f)
            ),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(3.dp, border),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(Brush.verticalGradient(gradientMain))
                .drawBehind {
                    drawRect(
                        brush = Brush.verticalGradient(gradientHighlight),
                        alpha = 1f,
                        topLeft = androidx.compose.ui.geometry.Offset(0f, 0f),
                        size = androidx.compose.ui.geometry.Size(size.width, size.height * 0.45f)
                    )
                }
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            GradientOutlinedText(
                text = text.uppercase(),
                fontSize = 28.sp,
                outlineWidth = 4f,
                outlineColor = Color(0xff000000),
                gradient = Brush.verticalGradient(
                    listOf(Color(0xFFFFEAB4), Color(0xFFE2A53C))
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

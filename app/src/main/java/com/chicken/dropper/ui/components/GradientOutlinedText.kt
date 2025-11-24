package com.chicken.dropper.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text

@Composable
fun GradientOutlinedText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 48.sp,
    strokeWidth: Float = 8f,
    gradient: Brush = Brush.verticalGradient(listOf(Color(0xFFFFE082), Color(0xFFFFD54F)))
) {
    val outlineColor = Color(0xFF7A4F00)
    val content = buildAnnotatedString {
        withStyle(
            SpanStyle(
                brush = gradient,
            )
        ) {
            append(text)
        }
    }

    Text(
        text = content,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.displaySmall.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
        )
    )
}

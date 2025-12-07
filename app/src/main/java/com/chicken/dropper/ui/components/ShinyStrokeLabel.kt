package com.chicken.dropper.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.chicken.dropper.R

@Composable
fun ShinyStrokeLabel(
    caption: String,
    modifier: Modifier = Modifier,

    expandHorizontal: Boolean = true,
    alignment: TextAlign = TextAlign.Center,

    size: TextUnit = 48.sp,
    stroke: Float = 8f,
    strokeTint: Color = Color(0xFF7A4F00),
    shineBrush: Brush = Brush.verticalGradient(
        listOf(Color(0xFFFFE082), Color(0xFFFFD54F))
    )
) {
    val fontFamily = remember {
        FontFamily(Font(R.font.tillana_extra_bold, weight = FontWeight.ExtraBold))
    }

    val styledText = MaterialTheme.typography.displayLarge.copy(
        fontSize = size,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = fontFamily,
        textAlign = alignment
    )

    val internalModifier =
        if (expandHorizontal) Modifier.fillMaxWidth() else Modifier

    Box(modifier = modifier) {

        val gradientText = buildAnnotatedString {
            withStyle(SpanStyle(brush = shineBrush)) { append(caption) }
        }

        Text(
            text = caption,
            style = styledText.copy(
                color = strokeTint,
                drawStyle = Stroke(width = stroke, join = StrokeJoin.Round)
            ),
            modifier = internalModifier
        )

        Text(
            text = gradientText,
            style = styledText,
            color = Color.White,
            modifier = internalModifier
        )
    }
}
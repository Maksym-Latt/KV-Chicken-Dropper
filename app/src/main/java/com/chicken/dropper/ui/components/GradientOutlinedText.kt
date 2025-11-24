package com.chicken.dropper.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.StrokeJoin
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.R

@Composable
fun GradientOutlinedText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 48.sp,
    outlineWidth: Float = 8f,
    outlineColor: Color = Color(0xFF7A4F00),
    gradient: Brush = Brush.verticalGradient(
        listOf(Color(0xFFFFE082), Color(0xFFFFD54F))
    )
) {
    val fontFamily = remember { FontFamily(Font(R.font.tillana_extra_bold, weight = FontWeight.ExtraBold)) }

    val styledText = MaterialTheme.typography.displayLarge.copy(
        fontSize = fontSize,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = fontFamily,
        textAlign = TextAlign.Center
    )

    Box(
        modifier = modifier
    ) {
        val content = buildAnnotatedString {
            withStyle(SpanStyle(brush = gradient)) { append(text) }
        }

        Text(
            text = text,
            style = styledText.copy(
                color = outlineColor,
                drawStyle = Stroke(width = outlineWidth, join = StrokeJoin.Round)
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = content,
            style = styledText,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun AutoResizeGradientText(
    text: String,
    modifier: Modifier = Modifier,
    maxFontSize: TextUnit = 40.sp,
    minFontSize: TextUnit = 18.sp,
    gradient: Brush,
    strokeWidth: Float = 6f
)   {
    var fittedSize by remember { mutableStateOf(maxFontSize) }
    var ready by remember { mutableStateOf(false) }

    val density = LocalDensity.current

    BoxWithConstraints(modifier) {

        val maxWidthPx = constraints.maxWidth.toFloat()
        val maxHeightPx = constraints.maxHeight.toFloat()

        LaunchedEffect(text, maxWidthPx, maxHeightPx) {
            var size = maxFontSize

            with(density) {
                while (size > minFontSize) {

                    val px = size.toPx()

                    val paint = android.graphics.Paint().apply {
                        textSize = px
                        isFakeBoldText = true
                        this.strokeWidth = strokeWidth
                        style = android.graphics.Paint.Style.FILL_AND_STROKE
                    }

                    val width = paint.measureText(text)
                    val height = paint.fontMetrics.run { bottom - top }

                    val fitsWidth = width <= maxWidthPx
                    val fitsHeight = height <= maxHeightPx

                    if (fitsWidth && fitsHeight) break

                    size *= 0.88f
                }

                fittedSize = size
                ready = true
            }
        }

        if (ready) {
            val annotated = buildAnnotatedString {
                withStyle(SpanStyle(brush = gradient)) { append(text) }
            }

            Text(
                text = annotated,
                fontSize = fittedSize,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier,
            )
        }
    }
}

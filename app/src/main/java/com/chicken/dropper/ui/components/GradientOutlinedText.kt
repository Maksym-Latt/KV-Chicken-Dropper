package com.chicken.dropper.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.core.content.res.ResourcesCompat
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
    val context = LocalContext.current

    // Загружаем extra-bold ttf
    val tillanaExtraBold = remember {
        ResourcesCompat.getFont(context, R.font.tillana_extra_bold)
    }

    val styledText = MaterialTheme.typography.displayLarge.copy(
        fontSize = fontSize,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center
    )

    Box(
        modifier = modifier.drawBehind {
            drawIntoCanvas { canvas ->
                val paint = android.graphics.Paint().apply {
                    isAntiAlias = true
                    style = android.graphics.Paint.Style.STROKE
                    strokeWidth = outlineWidth
                    color = outlineColor.toArgb()
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = styledText.fontSize.toPx()
                    typeface = tillanaExtraBold
                }

                val x = size.width / 2f
                val y = (size.height / 2f) - ((paint.descent() + paint.ascent()) / 2f)

                canvas.nativeCanvas.drawText(text, x, y, paint)
            }
        }
    ) {
        val content = buildAnnotatedString {
            withStyle(SpanStyle(brush = gradient)) { append(text) }
        }

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

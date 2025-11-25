package com.chicken.dropper.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.graphicsLayer
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween

enum class ChickenButtonStyle {
    Green, Magenta
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ChickenButtonStyle = ChickenButtonStyle.Green,
    fontSize: TextUnit = 32.sp,
    content: (@Composable RowScope.() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "primaryPressScale"
    )

    val (border, gradientMain) = when (style) {
        ChickenButtonStyle.Green ->
            Pair(
                Color(0xFF1F3406),
                listOf(Color(0xFF9FEF26), Color(0xFF448B0D))
            )

        ChickenButtonStyle.Magenta ->
            Pair(
                Color(0xFF44092B),
                listOf(Color(0xFFEB55B4), Color(0xFFA47E97))
            )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .graphicsLayer(
                scaleX = pressScale,
                scaleY = pressScale
            )
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = true),
                onClick = onClick
            )
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color.Black.copy(alpha = 0.4f),
                spotColor = Color.Black.copy(alpha = 0.4f)
            ),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(2.dp, border),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(gradientMain)
                )
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (content != null) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            } else {
                GradientOutlinedText(
                    text = text.uppercase(),
                    fontSize = fontSize,
                    outlineWidth = 4f,
                    outlineColor = Color(0xff000000),
                    gradient = Brush.verticalGradient(
                        listOf(Color(0xFFEDE622), Color(0xFFE1AC14))
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


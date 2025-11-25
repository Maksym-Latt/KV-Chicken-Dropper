package com.chicken.dropper.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SecondaryButton(
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,

    // ------ параметры стиля ------
    gradient: Brush = Brush.verticalGradient(
        listOf(Color(0xFFFFA3DA), Color(0xFF834265))
    ),
    borderColor: Color = Color(0xff000000),

    // ------ параметры размеров ------
    buttonSize: Dp = 60.dp,
    iconSize: Dp = 30.dp,
    cornerRadius: Dp = 14.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(durationMillis = 140),
        label = "secondaryPressScale"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(cornerRadius),
        color = Color.Transparent,
        border = BorderStroke(2.dp, borderColor),
        modifier = modifier
            .size(buttonSize)
            .scale(pressScale)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(cornerRadius),
                clip = false
            ),
        interactionSource = interactionSource,
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(buttonSize * 0.17f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
package com.chicken.dropper.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResourceBadge(
    amount: Int,
    iconResId: Int,
    modifier: Modifier = Modifier,

    fillGradient: Brush = Brush.verticalGradient(
        listOf(Color(0xFFB9FF5E), Color(0xFF63B600))
    ),
    outlineTint: Color = Color(0x003c7e00),

    badgeHeight: Dp = 60.dp,
    sidePadding: Dp = 18.dp,
    topBottomPadding: Dp = 6.dp,
    shapeRadius: Dp = 30.dp,
    elevationSize: Dp = 8.dp,
    iconSize: Dp = 36.dp
) {
    Surface(
        shape = RoundedCornerShape(shapeRadius),
        color = Color.Transparent,
        border = BorderStroke(2.dp, outlineTint),
        modifier = modifier
            .height(badgeHeight)
            .shadow(
                elevation = elevationSize,
                shape = RoundedCornerShape(shapeRadius),
                clip = false
            )
            .wrapContentWidth()
    ) {
        Box(
            modifier = Modifier
                .background(fillGradient)
                .padding(horizontal = sidePadding, vertical = topBottomPadding)
        ) {
            Row(
                modifier = Modifier.height(badgeHeight - topBottomPadding * 2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShinyStrokeLabel(
                    caption = amount.toString(),
                    size = 26.sp,
                    stroke = 4f,
                    expandHorizontal = false,
                    strokeTint = Color(0xff000000),
                    shineBrush = Brush.verticalGradient(
                        listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF))
                    ),
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}

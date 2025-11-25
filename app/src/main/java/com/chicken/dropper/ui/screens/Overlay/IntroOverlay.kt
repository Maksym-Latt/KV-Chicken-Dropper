package com.chicken.dropper.ui.screens.Overlay

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.GradientOutlinedText
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.components.rememberVerticalUiScale
import com.chicken.dropper.ui.components.scaled

@Composable
fun IntroOverlay(
    onStart: () -> Unit,
) {
    val scale = rememberVerticalUiScale()
    val scrimColor = rememberDelayedScrimColor(targetColor = Color(0xB0000000))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(scrimColor),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            shape = RoundedCornerShape(26.dp),
            color = Color.Transparent,
            border = BorderStroke(4.dp, Color.Black),
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf( Color(0xffb6428a),
                                Color(0xff694e60))
                        )
                    )
                    .padding(
                        horizontal = 18.dp.scaled(scale),
                        vertical = 28.dp.scaled(scale)
                    )
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp.scaled(scale))
                ) {

                    // ---------- TITLE ----------
                    GradientOutlinedText(
                        text = "How to Play",
                        fontSize = 38.sp.scaled(scale),
                        outlineWidth = 9f,
                        outlineColor = Color(0xFF114B70),
                        fillWidth = false,
                        textAlign = TextAlign.Center,
                        gradient = Brush.verticalGradient(
                            listOf(Color(0xFFFFF38A), Color(0xFFFFC300))
                        )
                    )


                    // ---------- STEP 1 ----------
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp.scaled(scale)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.egg),
                            contentDescription = null,
                            modifier = Modifier.size(56.dp.scaled(scale))
                        )
                        Text(
                            text = "Tap the screen to drop the egg.",
                            color = Color.White,
                            fontSize = 14.sp.scaled(scale),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // ---------- STEP 2 ----------
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp.scaled(scale)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bucket),
                            contentDescription = null,
                            modifier = Modifier.size(56.dp.scaled(scale))
                        )
                        Text(
                            text = "Catch it with the moving bucket.",
                            color = Color.White,
                            fontSize = 14.sp.scaled(scale),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // ---------- STEP 3 ----------
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp.scaled(scale)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.heart),
                            contentDescription = null,
                            modifier = Modifier.size(56.dp.scaled(scale))
                        )
                        Text(
                            text = "Miss = lose a life. Run out = game over.",
                            color = Color.White,
                            fontSize = 14.sp.scaled(scale),
                            fontWeight = FontWeight.Bold
                        )
                    }


                    // ---------- BUTTON ----------
                    PrimaryButton(
                        text = "Start",
                        onClick = onStart,
                        fontSize = 26.sp.scaled(scale),
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
            }
        }
    }
}


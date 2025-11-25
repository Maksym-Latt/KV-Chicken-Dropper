package com.chicken.dropper.ui.screens.Overlay

import androidx.compose.foundation.BorderStroke
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xB0000000)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = Color.Transparent,
            modifier = Modifier.padding(16.dp),
            border = BorderStroke(4.dp, Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF4CC1FF), Color(0xFF1C7EB7))
                        )
                    )
                    .padding(horizontal = 18.dp.scaled(scale), vertical = 28.dp.scaled(scale))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp.scaled(scale))
                ) {
                    GradientOutlinedText(
                        text = "Как играть?",
                        fontSize = 38.sp.scaled(scale),
                        outlineWidth = 9f,
                        outlineColor = Color(0xFF114B70),
                        fillWidth = false,
                        textAlign = TextAlign.Center,
                        gradient = Brush.verticalGradient(
                            listOf(Color(0xFFFFF38A), Color(0xFFFFC300))
                        )
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp.scaled(scale)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.egg),
                            contentDescription = null,
                            modifier = Modifier.size(56.dp.scaled(scale))
                        )
                        Text(
                            text = "Тапни по экрану, когда ведро под курицей",
                            color = Color.White,
                            fontSize = 20.sp.scaled(scale),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp.scaled(scale)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.bucket),
                            contentDescription = null,
                            modifier = Modifier.size(72.dp.scaled(scale))
                        )
                        Text(
                            text = "Попади яйцом в ведро, промах — минус жизнь",
                            color = Color.White,
                            fontSize = 20.sp.scaled(scale),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp.scaled(scale)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.heart),
                            contentDescription = null,
                            modifier = Modifier.size(52.dp.scaled(scale))
                        )
                        Text(
                            text = "Собери максимум очков, пока есть сердечки",
                            color = Color.White,
                            fontSize = 20.sp.scaled(scale),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.size(4.dp.scaled(scale)))

                    PrimaryButton(
                        text = "Понял, погнали!",
                        onClick = onStart,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        fontSize = 24.sp.scaled(scale)
                    )
                }
            }
        }
    }
}

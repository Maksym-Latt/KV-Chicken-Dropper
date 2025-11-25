package com.chicken.dropper.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.GradientOutlinedText
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.components.rememberVerticalUiScale
import com.chicken.dropper.ui.components.scaled

@Composable
fun ResultScreen(
    score: Int,
    onRetry: () -> Unit,
    onMenu: () -> Unit
) {
    val scale = rememberVerticalUiScale()
    Box(modifier = Modifier.fillMaxSize()) {
        // --- BG ---
        Image(
            painter = painterResource(id = R.drawable.bg_game),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // --- DARK OVERLAY ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA000000))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp.scaled(scale), vertical = 32.dp.scaled(scale)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))
            // ---------- TITLE ----------
            GradientOutlinedText(
                text = "Score of eggs:",
                fontSize = 32.sp.scaled(scale),
                outlineWidth = 10f,
                textAlign = TextAlign.Center,
                fillWidth = false,
                outlineColor = Color(0xFF6A3C00),
                gradient = Brush.verticalGradient(
                    listOf(Color(0xFFFFF38A), Color(0xFFFFC300))
                )
            )

            GradientOutlinedText(
                text = score.toString(),
                fontSize = 24.sp.scaled(scale),
                outlineWidth = 10f,
                textAlign = TextAlign.Center,
                fillWidth = false,
                outlineColor = Color(0xFF6A3C00),
                gradient = Brush.verticalGradient(
                    listOf(Color(0xFFFFD48A), Color(0xFFFF9900))
                )
            )

            // ---------- CHICKEN ----------
            Image(
                painter = painterResource(id = R.drawable.chicken_1_drop),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.65f),
                contentScale = ContentScale.Crop
            )

            // ---------- BROKEN EGG ----------
            Image(
                painter = painterResource(id = R.drawable.egg_broke),
                contentDescription = null,
                modifier = Modifier
                    .size(140.dp.scaled(scale))
            )

            // ---------- BUTTONS ----------
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(
                    text = "Retry",
                    onClick = onRetry,
                    style = ChickenButtonStyle.Magenta,
                    modifier = Modifier.fillMaxWidth(0.6f)
                )

                PrimaryButton(
                    text = "START",
                    onClick = onMenu,
                    style = ChickenButtonStyle.Green,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
package com.chicken.dropper.splashscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.unit.dp
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.GameTitle

@Composable
internal fun SplashScreen(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF7AD8FF), Color(0xFFFFF4D9))
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(modifier = Modifier.weight(2f))
            GameTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.weight(0.1f))
            Image(
                painter = painterResource(id = R.drawable.chicken_1_egg),
                contentDescription = "Chicken mascot",
                modifier = Modifier
                    .fillMaxWidth(0.6f),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.weight(0.1f))
            AnimatedLoadingText(
                modifier = Modifier
            )
            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}

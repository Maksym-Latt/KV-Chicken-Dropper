package com.chicken.dropper.ui.screens.Overlay

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.PrimaryButton

@Composable
public fun PauseOverlay(onResume: () -> Unit, onRestart: () -> Unit, onQuit: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF8C2A83),
            tonalElevation = 10.dp,
            shadowElevation = 18.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Paused",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color(0xFFFFE5FB),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PauseIconHolder(icon = R.drawable.ic_music)
                    PauseIconHolder(icon = R.drawable.ic_sound)
                    PauseIconHolder(icon = R.drawable.ic_vibration)
                }
                PrimaryButton(text = "Continue", onClick = onResume, style = ChickenButtonStyle.Magenta)
                PrimaryButton(text = "Restart", onClick = onRestart, style = ChickenButtonStyle.Magenta)
                PrimaryButton(text = "Main menu", onClick = onQuit, style = ChickenButtonStyle.Magenta)
            }
        }
    }
}


@Composable
private fun PauseIconHolder(icon: Int) {
    Surface(
        modifier = Modifier.size(44.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFD85BAE)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(28.dp))
        }
    }
}

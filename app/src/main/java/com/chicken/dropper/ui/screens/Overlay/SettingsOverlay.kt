package com.chicken.dropper.ui.screens.Overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chicken.dropper.R
import com.chicken.dropper.ui.viewmodel.AudioSettingsState

@Composable
fun SettingsOverlay(
    state: AudioSettingsState,
    onToggleMusic: () -> Unit,
    onToggleSound: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0xAA000000))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onDismiss
                )
        )

        Surface(
            modifier = Modifier
                .align(Alignment.Center),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF8C2A83),
            tonalElevation = 10.dp,
            shadowElevation = 18.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color(0xFFFFE5FB)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AudioToggleButton(
                        icon = R.drawable.ic_music,
                        isEnabled = state.isMusicEnabled,
                        onClick = onToggleMusic
                    )
                    AudioToggleButton(
                        icon = R.drawable.ic_sound,
                        isEnabled = state.isSoundEnabled,
                        onClick = onToggleSound
                    )
                }
            }
        }
    }
}

package com.chicken.dropper.ui.screens.Overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.GradientOutlinedText
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.viewmodel.AudioSettingsState

@Composable
fun SettingsOverlay(
    state: AudioSettingsState,
    onToggleMusic: () -> Unit,
    onToggleSound: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onDismiss
            ),
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
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                GradientOutlinedText(
                    text = "Settings",
                    fontSize = 32.sp,
                    outlineWidth = 8f,
                    outlineColor = Color(0xFF522D00),
                    gradient = Brush.verticalGradient(
                        listOf(Color(0xFFFFF38A), Color(0xFFFFC300))
                    ),
                    fillWidth = false
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    PauseIconToggle(
                        iconOn = Icons.Default.MusicNote,
                        iconOff = Icons.Default.MusicOff,
                        enabled = state.isMusicEnabled,
                        onClick = onToggleMusic
                    )

                    PauseIconToggle(
                        iconOn = Icons.Default.VolumeUp,
                        iconOff = Icons.Default.VolumeOff,
                        enabled = state.isSoundEnabled,
                        onClick = onToggleSound
                    )
                }

                PrimaryButton(
                    text = "Close",
                    onClick = onDismiss,
                    style = ChickenButtonStyle.Magenta,
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
            }
        }
    }
}
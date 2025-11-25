package com.chicken.dropper.ui.screens.Overlay

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.GradientOutlinedText
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.components.rememberVerticalUiScale
import com.chicken.dropper.ui.components.scaled
import com.chicken.dropper.ui.viewmodel.AudioSettingsState

@Composable
fun SettingsOverlay(
    audioState: AudioSettingsState,
    onToggleMusic: () -> Unit,
    onToggleSound: () -> Unit,
    onDismiss: () -> Unit
) {
    val scale = rememberVerticalUiScale()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000)),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            shape = RoundedCornerShape(26.dp),
            border = BorderStroke(4.dp, Color.Black),
            modifier = Modifier
                .padding(16.dp),
            color = Color.Transparent
        ) {

            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xffb6428a),
                                Color(0xff694e60)
                            )
                        )
                    )
                    .padding(horizontal = 12.dp.scaled(scale), vertical = 32.dp.scaled(scale))
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp.scaled(scale))
                ) {

                    // ---------- TITLE ----------
                    GradientOutlinedText(
                        text = "Settings",
                        fontSize = 40.sp.scaled(scale),
                        outlineWidth = 10f,
                        outlineColor = Color(0xFF551A32),
                        textAlign = TextAlign.Center,
                        fillWidth = false,
                        gradient = Brush.verticalGradient(
                            listOf(Color(0xFFFFF38A), Color(0xFFFFC300))
                        )
                    )

                    // ---------- AUDIO TOGGLES ----------
                    Column(
                        verticalArrangement = Arrangement.spacedBy(14.dp.scaled(scale)),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        SettingRowToggle(
                            title = "Music",
                            enabled = audioState.isMusicEnabled,
                            iconOn = Icons.Default.MusicNote,
                            iconOff = Icons.Default.MusicOff,
                            onClick = onToggleMusic,
                            scale = scale
                        )

                        SettingRowToggle(
                            title = "Sound FX",
                            enabled = audioState.isSoundEnabled,
                            iconOn = Icons.Default.VolumeUp,
                            iconOff = Icons.Default.VolumeOff,
                            onClick = onToggleSound,
                            scale = scale
                        )

                        PrimaryButton(
                            text = "Close",
                            onClick = onDismiss,
                            style = ChickenButtonStyle.Magenta,
                            modifier = Modifier
                                .padding(top = 8.dp.scaled(scale)),
                            fontSize = 28.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingRowToggle(
    title: String,
    enabled: Boolean,
    iconOn: ImageVector,
    iconOff: ImageVector,
    onClick: () -> Unit,
    scale: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp.scaled(scale))
            .height(56.dp.scaled(scale)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // ---- text ----
        Text(
            text = title,
            fontSize = 26.sp.scaled(scale),
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        // ---- button ----
        PauseIconToggle(
            iconOn = iconOn,
            iconOff = iconOff,
            enabled = enabled,
            onClick = onClick,
            scale = scale
        )
    }
}
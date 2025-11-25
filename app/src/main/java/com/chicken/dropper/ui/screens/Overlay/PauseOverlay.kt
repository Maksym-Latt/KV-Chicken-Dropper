package com.chicken.dropper.ui.screens.Overlay

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.GradientOutlinedText
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.viewmodel.AudioSettingsState

@Composable
fun PauseOverlay(
    audioState: AudioSettingsState,
    onToggleMusic: () -> Unit,
    onToggleSound: () -> Unit,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onQuit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000)),
        contentAlignment = Alignment.Center
    ) {

        // --- центр. окно ---
        Surface(
            shape = RoundedCornerShape(26.dp),
            border = BorderStroke(4.dp, Color.Black),  // чёрная окантовка
            modifier = Modifier
                .padding(16.dp),
            color = Color.Transparent
        ) {

            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFFFFA3DA),
                                Color(0xFF8C2A83)
                            )
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 32.dp)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    // ---------- TITLE ----------
                    GradientOutlinedText(
                        text = "Paused",
                        fontSize = 40.sp,
                        outlineWidth = 10f,
                        outlineColor = Color(0xFF551A32),
                        textAlign = TextAlign.Center,
                        fillWidth = false,
                        gradient = Brush.verticalGradient(
                            listOf(Color(0xFFFFF38A), Color(0xFFFFC300))
                        )
                    )

                    // ---------- AUDIO TOGGLES ----------
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        PauseIconToggle(
                            iconOn = Icons.Default.MusicNote,
                            iconOff = Icons.Default.MusicOff,
                            enabled = audioState.isMusicEnabled,
                            onClick = onToggleMusic
                        )

                        PauseIconToggle(
                            iconOn = Icons.Default.VolumeUp,
                            iconOff = Icons.Default.VolumeOff,
                            enabled = audioState.isSoundEnabled,
                            onClick = onToggleSound
                        )

                    }

                    // ---------- BUTTONS ----------
                    PauseMenuButton(text = "Continue", onClick = onResume)
                    PauseMenuButton(text = "Restart", onClick = onRestart)
                    PauseMenuButton(text = "Main menu", onClick = onQuit)
                }
            }
        }
    }
}


@Composable
private fun PauseMenuButton(text: String, onClick: () -> Unit) {
    PrimaryButton(
        text = text,
        onClick = onClick,
        style = ChickenButtonStyle.Magenta,
        modifier = Modifier.fillMaxWidth(0.8f)
    )
}

@Composable
fun PauseIconToggle(
    iconOn: ImageVector,
    iconOff: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val bg = Brush.verticalGradient(
        if (enabled)
            listOf(Color(0xFFFF7FD5), Color(0xFF9B3F8A))
        else
            listOf(Color(0xFF6A345E), Color(0xFF3A1C38))
    )

    val tint = Brush.verticalGradient(
        listOf(Color(0xFFF1FF2A), Color(0xFFE89819))
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color.Transparent,
        border = BorderStroke(2.dp, Color.Black),
        modifier = Modifier.size(54.dp)
    ) {
        Box(
            modifier = Modifier.background(bg),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(tint, blendMode = BlendMode.SrcIn)
                        }
                    }
            ) {
                Icon(
                    imageVector = if (enabled) iconOn else iconOff,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

package com.chicken.dropper.ui.screens.Overlay

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.ui.components.ButtonVisualMode
import com.chicken.dropper.ui.components.ShinyStrokeLabel
import com.chicken.dropper.ui.components.ActionButton
import com.chicken.dropper.ui.viewmodel.AudioSettingsState
import com.chicken.dropper.ui.components.rememberVerticalUiScale
import com.chicken.dropper.ui.components.scaled

@Composable
fun PauseOverlay(
    audioState: AudioSettingsState,
    onToggleMusic: () -> Unit,
    onToggleSound: () -> Unit,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onQuit: () -> Unit
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
                    ShinyStrokeLabel(
                        caption = "Paused",
                        size = 40.sp.scaled(scale),
                        stroke = 10f,
                        strokeTint = Color(0xFF551A32),
                        alignment = TextAlign.Center,
                        expandHorizontal = false,
                        shineBrush = Brush.verticalGradient(
                            listOf(Color(0xFFFFF38A), Color(0xFFFFC300))
                        )
                    )

                    // ---------- AUDIO TOGGLES ----------
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp.scaled(scale)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        PauseIconToggle(
                            iconOn = Icons.Default.VolumeUp,
                            iconOff = Icons.Default.VolumeOff,
                            enabled = audioState.isMusicEnabled,
                            onClick = onToggleMusic,
                            scale = scale
                        )

                        PauseIconToggle(
                            iconOn = Icons.Default.MusicNote,
                            iconOff = Icons.Default.MusicOff,
                            enabled = audioState.isSoundEnabled,
                            onClick = onToggleSound,
                            scale = scale
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
    ActionButton(
        label = text,
        onClick = onClick,
        visualMode = ButtonVisualMode.Magenta,
        modifier = Modifier.fillMaxWidth(0.8f),
        labelSize = 26.sp
    )
}

@Composable
fun PauseIconToggle(
    iconOn: ImageVector,
    iconOff: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
    scale: Float = 1f
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
        modifier = Modifier.size(64.dp.scaled(scale))
    ) {
        Box(
            modifier = Modifier.background(bg),
            contentAlignment = Alignment.Center
        ) {
            GradientIcon(
                icon = if (enabled) iconOn else iconOff,
                tint = tint,
                modifier = Modifier.size(48.dp.scaled(scale))
            )
        }
    }
}

// ---------------------------
// ICON WITH GRADIENT TINT
// ---------------------------
@Composable
fun GradientIcon(
    icon: ImageVector,
    tint: Brush,
    modifier: Modifier = Modifier
) {
    val painter = rememberVectorPainter(image = icon)

    Box(
        modifier = modifier
            .drawWithCache {
                onDrawWithContent {
                    drawIntoCanvas { canvas ->
                        val layerPaint = Paint()
                        val bounds = Rect(Offset.Zero, size)

                        // слой для маски и градиента
                        canvas.saveLayer(bounds, layerPaint)

                        // рисуем вектор белым — как маску
                        with(painter) {
                            draw(
                                size = size,
                                alpha = 1f,
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }

                        // льём градиент только в пределах маски
                        drawRect(
                            brush = tint,
                            blendMode = BlendMode.SrcIn
                        )

                        canvas.restore()
                    }
                }
            }
    )
}

package com.chicken.dropper.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.PrimaryButton

@Composable
fun GameScreen(
    onFinish: (Int) -> Unit,
    onQuit: () -> Unit,
    viewModel: GameViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.isGameOver) {
        if (state.isGameOver) onFinish(state.score)
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val bucketX = (state.bucketX * screenWidth).dp - 48.dp
    val eggYOffset = state.eggY?.let { (it * (LocalConfiguration.current.screenHeightDp - 200)).dp }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        GameTopBar(
            score = state.score,
            lives = state.lives,
            eggs = state.eggs,
            onPause = { viewModel.togglePause() }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 96.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.plate),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                contentScale = ContentScale.FillWidth
            )
            val chickenRes = if (state.chickenLookingDown) {
                state.selectedSkin?.dropSprite ?: R.drawable.chicken_1_drop
            } else {
                state.selectedSkin?.eggSprite ?: R.drawable.chicken_1_egg
            }
            Image(
                painter = painterResource(id = chickenRes),
                contentDescription = null,
                modifier = Modifier
                    .size(168.dp)
                    .align(Alignment.TopCenter)
            )
        }

        state.eggY?.let {
            Image(
                painter = painterResource(id = R.drawable.egg),
                contentDescription = null,
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = eggYOffset ?: 0.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 42.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(id = if (state.goldenBucket) R.drawable.bucket else R.drawable.bucket),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 96.dp, height = 82.dp)
                    .offset(x = bucketX)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            PrimaryButton(
                text = "START",
                onClick = { viewModel.dropEgg() },
                modifier = Modifier.padding(horizontal = 48.dp)
            )
        }

        if (state.isPaused) {
            PauseOverlay(
                onResume = { viewModel.resume() },
                onRestart = { viewModel.restart() },
                onQuit = onQuit
            )
        }
    }
}

@Composable
private fun GameTopBar(score: Int, lives: Int, eggs: Int, onPause: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.displayCutout)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onPause,
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp)),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color(0xFFFE83C6),
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Default.Pause, contentDescription = null)
        }

        Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color(0xFF5F2A7F).copy(alpha = 0.8f),
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "Score: ${score.toString().padStart(4, '0')}",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 20.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.egg),
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = eggs.toString(),
                            color = Color(0xFFFFF8E6),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(lives) {
                        Image(
                            painter = painterResource(id = R.drawable.heart),
                            contentDescription = null,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PauseOverlay(onResume: () -> Unit, onRestart: () -> Unit, onQuit: () -> Unit) {
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

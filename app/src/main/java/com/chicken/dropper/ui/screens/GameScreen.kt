package com.chicken.dropper.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.HudBar
import com.chicken.dropper.ui.components.PrimaryButton

@Composable
fun GameScreen(
    onFinish: (Int) -> Unit,
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

        HudBar(score = state.score, lives = state.lives)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp),
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
            val chickenRes = if (state.chickenLookingDown) R.drawable.chicken_1_drop else R.drawable.chicken_1_egg
            Image(
                painter = painterResource(id = chickenRes),
                contentDescription = null,
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.TopCenter)
            )
        }

        state.eggY?.let { y ->
            Image(
                painter = painterResource(id = R.drawable.egg),
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
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

        IconButton(
            onClick = { viewModel.togglePause() },
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Pause, contentDescription = null, tint = Color.White)
        }

        if (state.isPaused) {
            PauseOverlay(onResume = { viewModel.resume() }, onRestart = { viewModel.restart() })
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            PrimaryButton(text = "Drop", onClick = { viewModel.dropEgg() }, modifier = Modifier.padding(horizontal = 64.dp))
        }
    }
}

@Composable
private fun PauseOverlay(onResume: () -> Unit, onRestart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            color = Color(0xFF4E2C7A).copy(alpha = 0.85f)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(text = "Resume", onClick = onResume, modifier = Modifier.padding(bottom = 12.dp))
                PrimaryButton(text = "Restart", onClick = onRestart)
            }
        }
    }
}

package com.chicken.dropper.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.GradientOutlinedText
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.components.SecondaryButton
import com.chicken.dropper.ui.screens.Overlay.PauseOverlay
import com.chicken.dropper.ui.viewmodel.AudioSettingsViewModel

@Composable
fun GameScreen(
    onFinish: (Int) -> Unit,
    onQuit: () -> Unit,
    audioSettingsViewModel: AudioSettingsViewModel,
    viewModel: GameViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val audioState by audioSettingsViewModel.state.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_PAUSE) {
                    viewModel.togglePause()
                }
            }
        )
    }

    // Назад = пауза
    BackHandler(enabled = true) {
        viewModel.togglePause()
    }

    LaunchedEffect(state.isGameOver) {
        if (state.isGameOver) onFinish(state.score)
    }

    LaunchedEffect(Unit) { audioSettingsViewModel.playGameMusic() }
    LaunchedEffect(state.isPaused) {
        if (state.isPaused) {
            audioSettingsViewModel.pauseMusic()
        } else {
            audioSettingsViewModel.resumeMusic()
        }
    }

    var lastScore by remember { mutableStateOf(state.score) }
    var lastLives by remember { mutableStateOf(state.lives) }

    LaunchedEffect(state.isDropping) {
        if (state.isDropping) audioSettingsViewModel.playDrop()
    }

    LaunchedEffect(state.score) {
        if (state.score > lastScore) {
            audioSettingsViewModel.playHit()
        }
        lastScore = state.score
    }

    LaunchedEffect(state.lives) {
        if (state.lives < lastLives) {
            if (state.lives <= 0) {
                audioSettingsViewModel.playLose()
            } else {
                audioSettingsViewModel.playMiss()
            }
        }
        lastLives = state.lives
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val bucketX = (state.bucketX * screenWidth).dp - 48.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    viewModel.dropEgg()
                }
            }
    ) {

        Image(
            painter = painterResource(id = R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // ---------- TOP BAR ----------
        GameTopBar(
            score = state.score,
            lives = state.lives,
            onPause = { viewModel.togglePause() }
        )

        // ---------- PLATE + CHICKEN ----------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // КУРИЦА
            val chickenRes =
                if (state.chickenLookingDown) state.selectedSkin?.dropSprite ?: R.drawable.chicken_1_drop
                else state.selectedSkin?.eggSprite ?: R.drawable.chicken_1_egg

            Image(
                painter = painterResource(id = chickenRes),
                contentDescription = null,
                modifier = Modifier
                    .size(180.dp)
            )

            // ПЛИТА
            Image(
                painter = painterResource(id = R.drawable.plate),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentScale = ContentScale.Fit
            )
        }



        // ---------- EGG FALLING ----------
        state.eggY?.let { eggPos ->
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            val bucketHeight = 100.dp
            val eggMaxY = screenHeight - bucketHeight - 40.dp

            val density = LocalDensity.current

            val eggYOffset = with(density) {
                (eggPos * eggMaxY.toPx()).toDp()
            }

            Image(
                painter = painterResource(id = R.drawable.egg),
                contentDescription = null,
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = eggYOffset)
            )
        }

        // ---------- BUCKET ----------
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.bucket),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 120.dp, height = 100.dp)
                    .offset(x = bucketX)
            )
        }

        // ---------- BROKEN EGG ----------
        if (state.brokenEggVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 22.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.egg_broke),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )
            }
        }

        // ---------- PAUSE OVERLAY ----------
        if (state.isPaused) {
            PauseOverlay(
                audioState = audioState,
                onToggleMusic = audioSettingsViewModel::toggleMusic,
                onToggleSound = audioSettingsViewModel::toggleSound,
                onResume = { viewModel.resume() },
                onRestart = { viewModel.restart() },
                onQuit = onQuit
            )
        }
    }
}

@Composable
fun GameTopBar(
    score: Int,
    lives: Int,
    onPause: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .windowInsetsPadding(WindowInsets.displayCutout),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        // ---- PAUSE BUTTON ----
        SecondaryButton(
            icon = painterResource(id = R.drawable.ic_pause),
            onClick = onPause,
            buttonSize = 60.dp,
            iconSize = 32.dp
        )

        // ---- SCORE + HEARTS ----
        Column(
            horizontalAlignment = Alignment.End
        ) {

            // --- SCORE ---
            GradientOutlinedText(
                text = "Score: ${score.toString().padStart(4, '0')}",
                fontSize = 22.sp,
                outlineWidth = 7f,
                fillWidth = false,
                textAlign = TextAlign.Left,
                outlineColor = Color(0xFF522D00),
                gradient = Brush.verticalGradient(
                    listOf(Color(0xFFFFF38A), Color(0xFFFFC300))
                )
            )

            // --- HEARTS ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(lives) {
                    Image(
                        painter = painterResource(id = R.drawable.heart),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}
package com.chicken.dropper.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.GradientOutlinedText
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

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val scaleFactor = remember(screenWidth, screenHeight) {
        // Use current layout as a baseline while softly adapting to wider or taller screens
        val widthRatio = screenWidth / 411f
        val heightRatio = screenHeight / 891f
        maxOf(0.85f, minOf(widthRatio, heightRatio))
    }
    fun scaledDp(value: Float) = (value * scaleFactor).dp

    val bucketX = (state.bucketX * screenWidth).dp - scaledDp(48f)

    val idleBob by rememberInfiniteTransition(label = "chickenIdle").animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 1800, easing = LinearEasing), RepeatMode.Reverse),
        label = "chickenBob"
    )
    val chickenDropOffset by animateFloatAsState(
        targetValue = if (state.isDropping) 6f else 0f,
        animationSpec = tween(durationMillis = 220, easing = LinearEasing),
        label = "chickenDrop"
    )
    val chickenOffset = scaledDp(idleBob + chickenDropOffset)

    val eggWobble by rememberInfiniteTransition(label = "eggWobble").animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 700, easing = LinearEasing), RepeatMode.Reverse),
        label = "eggTilt"
    )

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
            onPause = { viewModel.togglePause() },
            scaleFactor = scaleFactor
        )

        // ---------- PLATE + CHICKEN ----------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = scaledDp(80f)),
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
                    .size(scaledDp(180f))
                    .offset(y = chickenOffset)
            )

            // ПЛИТА
            Image(
                painter = painterResource(id = R.drawable.plate),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = scaledDp(24f)),
                contentScale = ContentScale.Fit
            )
        }



        // ---------- EGG FALLING ----------
        state.eggY?.let { eggPos ->
            val eggYOffset = (eggPos * (screenHeight - 230)).dp
            val eggRotation = if (state.isDropping) eggWobble else 0f

            Image(
                painter = painterResource(id = R.drawable.egg),
                contentDescription = null,
                modifier = Modifier
                    .size(scaledDp(44f))
                    .align(Alignment.TopCenter)
                    .offset(y = eggYOffset)
                    .graphicsLayer { rotationZ = eggRotation }
            )
        }

        // ---------- BUCKET ----------
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = scaledDp(48f)),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.bucket),
                contentDescription = null,
                modifier = Modifier
                    .size(width = scaledDp(120f), height = scaledDp(100f))
                    .offset(x = bucketX)
            )
        }

        // ---------- BROKEN EGG ----------
        AnimatedVisibility(
            visible = state.brokenEggVisible,
            enter = fadeIn(animationSpec = tween(200)) + scaleIn(initialScale = 0.85f),
            exit = fadeOut(animationSpec = tween(250)) + scaleOut(targetScale = 0.85f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = scaledDp(22f)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.egg_broke),
                    contentDescription = null,
                    modifier = Modifier.size(scaledDp(72f))
                )
            }
        }

        // ---------- PAUSE OVERLAY ----------
        if (state.isPaused) {
            PauseOverlay(
                audioState = audioState,
                onToggleMusic = audioSettingsViewModel::toggleMusic,
                onToggleSound = audioSettingsViewModel::toggleSound,
                onToggleVibration = audioSettingsViewModel::toggleVibration,
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
    onPause: () -> Unit,
    scaleFactor: Float
) {
    fun scaled(value: Float) = (value * scaleFactor).dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = scaled(16f))
            .windowInsetsPadding(WindowInsets.displayCutout),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        // ---- PAUSE BUTTON ----
        SecondaryButton(
            icon = painterResource(id = R.drawable.ic_pause),
            onClick = onPause,
            buttonSize = scaled(60f),
            iconSize = scaled(32f)
        )

        // ---- SCORE + HEARTS ----
        Column(
            horizontalAlignment = Alignment.End
        ) {

            // --- SCORE ---
            GradientOutlinedText(
                text = "Score: ${score.toString().padStart(4, '0')}",
                fontSize = 22.sp * scaleFactor,
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
                horizontalArrangement = Arrangement.spacedBy(scaled(6f)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(lives) {
                    Image(
                        painter = painterResource(id = R.drawable.heart),
                        contentDescription = null,
                        modifier = Modifier.size(scaled(40f))
                    )
                }
            }
        }
    }
}
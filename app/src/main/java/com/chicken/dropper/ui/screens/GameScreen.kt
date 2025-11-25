package com.chicken.dropper.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.chicken.dropper.ui.theme.rememberScreenScale
import com.chicken.dropper.ui.theme.scaled
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
    val scale = rememberScreenScale()

    val infiniteTransition = rememberInfiniteTransition(label = "chicken_bounce")
    val chickenBob by infiniteTransition.animateFloat(
        initialValue = -4f * scale,
        targetValue = 4f * scale,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "chicken_bounce_anim"
    )

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

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val bucketWidth = 120.dp.scaled(scale)
    val bucketHalfWidth = bucketWidth / 2
    val bucketX by animateFloatAsState(
        targetValue = (state.bucketX * screenWidth) - bucketHalfWidth.value,
        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
        label = "bucket_move_anim"
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
            scale = scale
        )

        // ---------- PLATE + CHICKEN ----------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp.scaled(scale)),
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
                    .size(180.dp.scaled(scale))
                    .offset(y = chickenBob.dp)
            )

            // ПЛИТА
            Image(
                painter = painterResource(id = R.drawable.plate),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp.scaled(scale)),
                contentScale = ContentScale.Fit
            )
        }



        // ---------- EGG FALLING ----------
        state.eggY?.let { eggPos ->
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            val bucketHeight = 100.dp.scaled(scale)
            val eggMaxY = screenHeight - bucketHeight - 40.dp.scaled(scale)

            val density = LocalDensity.current

            val eggYOffset = with(density) {
                (eggPos * eggMaxY.toPx()).toDp()
            }

            val eggRotation by infiniteTransition.animateFloat(
                initialValue = -6f,
                targetValue = 6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 700, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "egg_rotation"
            )

            Image(
                painter = painterResource(id = R.drawable.egg),
                contentDescription = null,
                modifier = Modifier
                    .size(44.dp.scaled(scale))
                    .align(Alignment.TopCenter)
                    .offset(y = eggYOffset)
                    .graphicsLayer { rotationZ = eggRotation }
            )
        }

        // ---------- BUCKET ----------
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp.scaled(scale)),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.bucket),
                contentDescription = null,
                modifier = Modifier
                    .size(width = bucketWidth, height = 100.dp.scaled(scale))
                    .offset(x = bucketX.dp)
            )
        }

        // ---------- BROKEN EGG ----------
        AnimatedVisibility(visible = state.brokenEggVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 22.dp.scaled(scale)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.egg_broke),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp.scaled(scale))
                )
            }
        }

        // ---------- PAUSE OVERLAY ----------
        AnimatedVisibility(visible = state.isPaused) {
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
    scale: Float = 1f
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp.scaled(scale))
            .windowInsetsPadding(WindowInsets.displayCutout),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        // ---- PAUSE BUTTON ----
        SecondaryButton(
            icon = painterResource(id = R.drawable.ic_pause),
            onClick = onPause,
            buttonSize = 60.dp.scaled(scale),
            iconSize = 32.dp.scaled(scale)
        )

        // ---- SCORE + HEARTS ----
        Column(
            horizontalAlignment = Alignment.End
        ) {

            // --- SCORE ---
            GradientOutlinedText(
                text = "Score: ${score.toString().padStart(4, '0')}",
                fontSize = 22.sp * scale,
                outlineWidth = 7f * scale,
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
                        modifier = Modifier.size(40.dp.scaled(scale))
                    )
                }
            }
        }
    }
}
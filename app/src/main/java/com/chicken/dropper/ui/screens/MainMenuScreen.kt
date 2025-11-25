package com.chicken.dropper.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.GameTitle
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.components.SecondaryButton
import com.chicken.dropper.ui.screens.Overlay.SettingsOverlay
import com.chicken.dropper.ui.theme.rememberScreenScale
import com.chicken.dropper.ui.theme.scaled
import com.chicken.dropper.ui.viewmodel.AudioSettingsViewModel
import com.chicken.dropper.ui.viewmodel.MenuViewModel
@Composable
fun MainMenuScreen(
    onPlay: () -> Unit,
    onShop: () -> Unit,
    audioSettingsViewModel: AudioSettingsViewModel,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val audioState by audioSettingsViewModel.state.collectAsStateWithLifecycle()
    var showSettings by rememberSaveable { mutableStateOf(false) }
    val scale = rememberScreenScale()

    val infiniteTransition = rememberInfiniteTransition(label = "menu_chicken")
    val chickenFloat by infiniteTransition.animateFloat(
        initialValue = -6f * scale,
        targetValue = 6f * scale,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "menu_chicken_float"
    )

    LaunchedEffect(Unit) { audioSettingsViewModel.playMenuMusic() }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp.scaled(scale), vertical = 24.dp.scaled(scale)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ---------- TOP BAR ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.displayCutout),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SecondaryButton(
                    icon = painterResource(id = R.drawable.ic_settings),
                    onClick = { showSettings = true }
                )
            }

            // ---------- SPACE BEFORE CONTENT ----------
            Spacer(modifier = Modifier.weight(1f))

            // ---------- TITLE ----------
            GameTitle()

            // ---------- CHICKEN IMAGE ----------
            Image(
                painter = painterResource(
                    id = when (state.selectedSkinId) {
                        "cooker" -> R.drawable.chicken_2_egg
                        "hero" -> R.drawable.chicken_3_egg
                        else -> R.drawable.chicken_1_egg
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.64f)
                    .offset(y = chickenFloat.dp),
                contentScale = ContentScale.Crop
            )

            // ---------- SPACE ABOVE BUTTONS ----------
            Spacer(modifier = Modifier.weight(0.5f))

            // ---------- BUTTONS BLOCK ----------
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PrimaryButton(
                    text = "Shop",
                    onClick = onShop,
                    style = ChickenButtonStyle.Magenta,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )

                PrimaryButton(
                    text = "START",
                    onClick = onPlay,
                    style = ChickenButtonStyle.Green,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }

            // ---------- SPACE AFTER BUTTONS ----------
            Spacer(modifier = Modifier.weight(1f))
        }

        if (showSettings) {
            SettingsOverlay(
                state = audioState,
                onToggleMusic = audioSettingsViewModel::toggleMusic,
                onToggleSound = audioSettingsViewModel::toggleSound,
                onDismiss = { showSettings = false }
            )
        }
    }
}

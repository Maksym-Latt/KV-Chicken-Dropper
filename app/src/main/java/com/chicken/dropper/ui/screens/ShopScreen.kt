package com.chicken.dropper.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.EggCounter
import com.chicken.dropper.ui.components.GradientOutlinedText
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.components.SecondaryButton
import com.chicken.dropper.ui.theme.rememberScreenScale
import com.chicken.dropper.ui.theme.scaled
import com.chicken.dropper.ui.viewmodel.ShopViewModel


@Composable
fun ShopScreen(
    onBack: () -> Unit,
    viewModel: ShopViewModel = hiltViewModel()
) {
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val skins = viewModel.skins
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentSkin = skins.getOrNull(currentIndex)
    val scale = rememberScreenScale()
    val infiniteTransition = rememberInfiniteTransition(label = "shop_skin")
    val skinFloat by infiniteTransition.animateFloat(
        initialValue = -10f * scale,
        targetValue = 10f * scale,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shop_skin_float"
    )

    if (skins.isNotEmpty() && currentIndex !in skins.indices) {
        currentIndex = 0
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp.scaled(scale), vertical = 24.dp.scaled(scale)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                eggs = playerState.eggs,
                onBack = onBack,
                scale = scale
            )

            Spacer(modifier = Modifier.height(8.dp.scaled(scale)))

            if (currentSkin != null) {
                TitleBlock(title1 = currentSkin.titleTop, title2 = currentSkin.titleBottom, scale = scale)
            }

            Spacer(modifier = Modifier.height(8.dp.scaled(scale)))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (skins.isNotEmpty()) {
                    val skin = skins[currentIndex]
                    val owned = playerState.ownedSkins.contains(skin.id)
                    val selected = playerState.selectedSkinId == skin.id

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ArrowButton(direction = ArrowDirection.Left, scale = scale) {
                            currentIndex = (currentIndex - 1 + skins.size) % skins.size
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.62f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val sprite = if (selected) skin.dropSprite else skin.eggSprite

                            Image(
                                painter = painterResource(id = sprite),
                                contentDescription = skin.name,
                                modifier = Modifier
                                    .height(340.dp.scaled(scale))
                                    .fillMaxWidth(),
                                    .offset(y = skinFloat.dp),
                                contentScale = ContentScale.Fit
                            )
                        }

                        ArrowButton(direction = ArrowDirection.Right, scale = scale) {
                            currentIndex = (currentIndex + 1) % skins.size
                        }
                    }
                }
            }

            // 🟩 2. КНОПКА — строго под Box, внизу Columns
            if (skins.isNotEmpty()) {
                val skin = skins[currentIndex]
                val owned = playerState.ownedSkins.contains(skin.id)
                val selected = playerState.selectedSkinId == skin.id

                Spacer(modifier = Modifier.height(12.dp.scaled(scale)))

                ShopBottomAction(
                    price = skin.price,
                    owned = owned,
                    selected = selected,
                    eggs = playerState.eggs,
                    onBuy = { viewModel.onBuySkin(skin) },
                    onSelect = { viewModel.onSelectSkin(skin.id) }
                )

                Spacer(modifier = Modifier.height(8.dp.scaled(scale)))
            }
        }
    }
}

// endregion

// region TopBar

@Composable
private fun TopBar(
    eggs: Int,
    onBack: () -> Unit,
    scale: Float = 1f
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.displayCutout)
            .padding(bottom = 8.dp.scaled(scale)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SecondaryButton(
            icon = painterResource(id = R.drawable.ic_home),
            onClick = onBack,
            buttonSize = 60.dp.scaled(scale),
            iconSize = 32.dp.scaled(scale)
        )

        EggCounter(
            count = eggs,
            eggIcon = R.drawable.egg
        )
    }
}

// endregion

// region TitleBlock

@Composable
private fun TitleBlock(title1: String, title2: String, scale: Float = 1f) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ---------- CHICKEN ----------
        GradientOutlinedText(
            text = title1,
            fontSize = 54.sp * scale,
            outlineWidth = 10f * scale,
            outlineColor = Color(0xFF551A32),
            gradient = Brush.verticalGradient(
                listOf(Color(0xFFFF88D0), Color(0xFFCA3CC7))
            ),
        )

        // ---------- DROPPER ----------
        GradientOutlinedText(
            text = title2,
            gradient = Brush.verticalGradient(
                listOf(Color(0xFFFFC107), Color(0xFFFFC107))
            ),
            outlineColor = Color(0xFF6A3C00),
            fontSize = 54.sp * scale,
            outlineWidth = 10f * scale,
            modifier = Modifier
                .offset(y = (-55).dp.scaled(scale))
        )
    }
}

// endregion

// region Arrows

private enum class ArrowDirection { Left, Right }

@Composable
private fun ArrowButton(direction: ArrowDirection, onClick: () -> Unit, scale: Float = 1f) {
    val icon = when (direction) {
        ArrowDirection.Left -> Icons.Filled.KeyboardArrowLeft
        ArrowDirection.Right -> Icons.Filled.KeyboardArrowRight
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp.scaled(scale)),
        color = Color.Transparent,
        border = BorderStroke(2.dp, Color(0xFFB88416)),
        modifier = Modifier
            .size(width = 46.dp.scaled(scale), height = 80.dp.scaled(scale))
            .shadow(8.dp.scaled(scale), RoundedCornerShape(24.dp.scaled(scale)), clip = false)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFFFF682), Color(0xFFF1B01C))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF8B4D00),
                modifier = Modifier.size(32.dp.scaled(scale))
            )
        }
    }
}

// endregion

// region BottomAction

@Composable
private fun ShopBottomAction(
    price: Int,
    owned: Boolean,
    selected: Boolean,
    eggs: Int,
    onBuy: () -> Unit,
    onSelect: () -> Unit
) {
    val canBuy = !owned && eggs >= price

    val text = when {
        selected -> "Selected"
        owned -> "Select"
        else -> price.toString()
    }

    val style = if (owned) ChickenButtonStyle.Magenta else ChickenButtonStyle.Green

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        PrimaryButton(
            text = text,
            onClick = {
                when {
                    selected -> Unit
                    owned -> onSelect()
                    canBuy -> onBuy()
                }
            },
            style = style,
            modifier = Modifier
        )
    }
}
package com.chicken.dropper.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.graphicsLayer
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
import com.chicken.dropper.ui.components.rememberVerticalUiScale
import com.chicken.dropper.ui.components.scaled
import com.chicken.dropper.ui.viewmodel.ShopViewModel
import java.util.Collections.rotate
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate

@Composable
fun ShopScreen(
    onBack: () -> Unit,
    viewModel: ShopViewModel = hiltViewModel()
) {
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val skins = viewModel.skins
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentSkin = skins.getOrNull(currentIndex)
    val scale = rememberVerticalUiScale()

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
                .padding(horizontal = 20.dp.scaled(scale)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                eggs = playerState.eggs,
                onBack = onBack,
                scale = scale
            )

            Spacer(modifier = Modifier.weight(1f))

            if (currentSkin != null) {
                TitleBlock(title1 = currentSkin.titleTop, title2 = currentSkin.titleBottom, scale = scale)
            }

            Spacer(modifier = Modifier.weight(1f))

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
                            val sprite = skin.eggSprite

                            Image(
                                painter = painterResource(id = sprite),
                                contentDescription = skin.name,
                                modifier = Modifier
                                    .fillMaxWidth(1f) .aspectRatio(0.65f),
                                contentScale = ContentScale.Fit
                            )
                        }

                        ArrowButton(direction = ArrowDirection.Right, scale = scale) {
                            currentIndex = (currentIndex + 1) % skins.size
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(2f))

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
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

// endregion

// region TopBar

@Composable
private fun TopBar(
    eggs: Int,
    onBack: () -> Unit,
    scale: Float = 1f,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.displayCutout)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SecondaryButton(
            icon = painterResource(id = R.drawable.ic_home),
            onClick = onBack,
            buttonSize = 50.dp.scaled(scale),
            iconSize = 40.dp.scaled(scale)
        )

        EggCounter(
            count = eggs,
            eggIcon = R.drawable.egg,
            height = 50.dp.scaled(scale),
            horizontalPadding = 18.dp.scaled(scale),
            verticalPadding = 6.dp.scaled(scale),
            cornerRadius = 30.dp.scaled(scale),
            elevation = 8.dp.scaled(scale),
            eggIconSize = 36.dp.scaled(scale)
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
            fontSize = 54.sp.scaled(scale),
            outlineWidth = 10f,
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
            fontSize = 54.sp.scaled(scale),
            outlineWidth = 10f,
            modifier = Modifier
                .offset(y = (-55).dp.scaled(scale))
        )
    }
}

// endregion

// region Arrows

private enum class ArrowDirection { Left, Right }
@Composable
private fun ArrowButton(
    direction: ArrowDirection,
    scale: Float = 1f,
    onClick: () -> Unit
) {
    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xFFFFF35A),
            Color(0xFFFFC107)
        )
    )

    val rotation = when (direction) {
        ArrowDirection.Right -> 0f
        ArrowDirection.Left -> 180f
    }

    Surface(
        onClick = onClick,
        color = Color.Transparent,
        shape = CircleShape,
        modifier = Modifier
            .size(46.dp.scaled(scale))
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = size.minDimension * 0.18f
            val w = size.width
            val h = size.height

            val path = Path().apply {
                moveTo(w * 0.30f, h * 0.25f)
                lineTo(w * 0.68f, h * 0.50f)
                lineTo(w * 0.30f, h * 0.75f)
            }

            rotate(rotation, pivot = center) {
                drawPath(
                    path = path,
                    brush = gradient,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
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
        ) {
            if (!owned && !selected) {
                // Цена + яйцо
                Icon(
                    painter = painterResource(id = R.drawable.egg),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                GradientOutlinedText(
                    text = price.toString(),
                    fontSize = 26.sp,
                    outlineWidth = 4f,
                    outlineColor = Color(0xff000000),
                    gradient = Brush.verticalGradient(
                        listOf(Color(0xFFFFEAB4), Color(0xFFE2A53C))
                    ),
                    fillWidth = false
                )
            } else {
                // Select / Selected
                GradientOutlinedText(
                    text = text.uppercase(),
                    fontSize = 26.sp,
                    outlineWidth = 4f,
                    outlineColor = Color(0xff000000),
                    gradient = Brush.verticalGradient(
                        listOf(Color(0xFFFFEAB4), Color(0xFFE2A53C))
                    ),
                    fillWidth = false
                )
            }
        }
    }
}
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.ButtonVisualMode
import com.chicken.dropper.ui.components.ResourceBadge
import com.chicken.dropper.ui.components.ShinyStrokeLabel
import com.chicken.dropper.ui.components.ActionButton
import com.chicken.dropper.ui.components.SecondaryButton
import com.chicken.dropper.ui.components.rememberVerticalUiScale
import com.chicken.dropper.ui.components.scaled
import com.chicken.dropper.ui.viewmodel.ShopViewModel
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.delay

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
    var showNoMoney by remember { mutableStateOf(false) }

    if (skins.isNotEmpty() && currentIndex !in skins.indices) {
        currentIndex = 0
    }

    LaunchedEffect(showNoMoney) {
        if (showNoMoney) {
            delay(1200)
            showNoMoney = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_menu_cd),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (showNoMoney) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp.scaled(scale)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xAA000000),
                    border = BorderStroke(3.dp, Color.Black)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFFF7D7D), Color(0xFFB12020))
                                )
                            )
                            .padding(horizontal = 24.dp, vertical = 14.dp)
                    ) {
                        ShinyStrokeLabel(
                            caption = "Not enough eggs!",
                            size = 24.sp.scaled(scale),
                            stroke = 6f,
                            strokeTint = Color(0xFF4A0E0E),
                            expandHorizontal = false,
                            shineBrush = Brush.verticalGradient(
                                listOf(Color(0xFFFFF38A), Color(0xFFFFC300))
                            )
                        )
                    }
                }
            }
        }

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
                    onSelect = { viewModel.onSelectSkin(skin.id) },
                    onNotEnoughEggs = { showNoMoney = true }
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

        ResourceBadge(
            amount = eggs,
            iconResId = R.drawable.egg,
            badgeHeight = 50.dp.scaled(scale),
            sidePadding = 18.dp.scaled(scale),
            topBottomPadding = 6.dp.scaled(scale),
            shapeRadius = 30.dp.scaled(scale),
            elevationSize = 8.dp.scaled(scale),
            iconSize = 36.dp.scaled(scale)
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
        ShinyStrokeLabel(
            caption = title1,
            size = 54.sp.scaled(scale),
            stroke = 10f,
            strokeTint = Color(0xFF551A32),
            shineBrush = Brush.verticalGradient(
                listOf(Color(0xFFFF88D0), Color(0xFFCA3CC7))
            ),
        )

        // ---------- DROPPER ----------
        ShinyStrokeLabel(
            caption = title2,
            shineBrush = Brush.verticalGradient(
                listOf(Color(0xFFFFC107), Color(0xFFFFC107))
            ),
            strokeTint = Color(0xFF6A3C00),
            size = 54.sp.scaled(scale),
            stroke = 10f,
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
    onSelect: () -> Unit,
    onNotEnoughEggs: () -> Unit
) {
    val canBuy = !owned && eggs >= price

    val text = when {
        selected -> "Selected"
        owned -> "Select"
        else -> price.toString()
    }

    val style = if (owned) ButtonVisualMode.Magenta else ButtonVisualMode.Green

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ActionButton(
            label = text,
            onClick = {
                when {
                    selected -> Unit
                    owned -> onSelect()
                    canBuy -> onBuy()
                    else -> onNotEnoughEggs()
                }
            },
            visualMode = style,
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

                ShinyStrokeLabel(
                    caption = price.toString(),
                    size = 26.sp,
                    stroke = 4f,
                    strokeTint = Color(0xff000000),
                    shineBrush = Brush.verticalGradient(
                        listOf(Color(0xFFFFEAB4), Color(0xFFE2A53C))
                    ),
                    expandHorizontal = false
                )
            } else {
                // Select / Selected
                ShinyStrokeLabel(
                    caption = text.uppercase(),
                    size = 26.sp,
                    stroke = 4f,
                    strokeTint = Color(0xff000000),
                    shineBrush = Brush.verticalGradient(
                        listOf(Color(0xFFFFEAB4), Color(0xFFE2A53C))
                    ),
                    expandHorizontal = false
                )
            }
        }
    }
}
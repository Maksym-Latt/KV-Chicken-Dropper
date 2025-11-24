package com.chicken.dropper.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.viewmodel.ShopViewModel

@Composable
fun ShopScreen(onBack: () -> Unit, viewModel: ShopViewModel = hiltViewModel()) {
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val skins = viewModel.skins
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }

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
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.displayCutout)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onBack,
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFFE83C6),
                    modifier = Modifier.size(width = 72.dp, height = 48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "Back", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFF5F2A7F).copy(alpha = 0.85f),
                    tonalElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(painter = painterResource(id = R.drawable.egg), contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = playerState.eggs.toString(), color = Color.White, style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }

            Text(
                text = "Chicken skins",
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box(modifier = Modifier.weight(1f, fill = true)) {
                if (skins.isNotEmpty()) {
                    val skin = skins[currentIndex]
                    val owned = playerState.ownedSkins.contains(skin.id)
                    val selected = playerState.selectedSkinId == skin.id

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ArrowButton(direction = ArrowDirection.Left) {
                            currentIndex = (currentIndex - 1 + skins.size) % skins.size
                        }

                        SkinCard(
                            title = skin.name,
                            price = skin.price,
                            owned = owned,
                            selected = selected,
                            image = if (selected) skin.dropSprite else skin.eggSprite,
                            onAction = {
                                if (owned) viewModel.onSelectSkin(skin.id) else viewModel.onBuySkin(skin)
                            },
                            modifier = Modifier.weight(1f)
                        )

                        ArrowButton(direction = ArrowDirection.Right) {
                            currentIndex = (currentIndex + 1) % skins.size
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SkinCard(
    title: String,
    price: Int,
    owned: Boolean,
    selected: Boolean,
    image: Int,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFFEDE1F8).copy(alpha = 0.72f),
        tonalElevation = 4.dp,
        modifier = modifier.padding(horizontal = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(180.dp),
                contentScale = ContentScale.FillHeight
            )
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF472B13)
            )
            val actionLabel = when {
                selected -> "Selected"
                owned -> "Select"
                else -> "${price}"
            }
            val buttonStyle = if (owned) ChickenButtonStyle.Magenta else ChickenButtonStyle.Green
            PrimaryButton(
                text = actionLabel,
                onClick = onAction,
                style = buttonStyle,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private enum class ArrowDirection { Left, Right }

@Composable
private fun ArrowButton(direction: ArrowDirection, onClick: () -> Unit) {
    val icon = when (direction) {
        ArrowDirection.Left -> Icons.Default.KeyboardArrowLeft
        ArrowDirection.Right -> Icons.Default.KeyboardArrowRight
    }
    val description = when (direction) {
        ArrowDirection.Left -> "Previous skin"
        ArrowDirection.Right -> "Next skin"
    }

    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color(0xFF4E3466).copy(alpha = 0.9f),
        tonalElevation = 6.dp,
        modifier = Modifier.size(64.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

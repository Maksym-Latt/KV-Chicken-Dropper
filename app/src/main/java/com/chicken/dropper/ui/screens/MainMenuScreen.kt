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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.chicken.dropper.ui.components.GameTitle
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.viewmodel.MenuViewModel

@Composable
fun MainMenuScreen(
    onPlay: () -> Unit,
    onRecords: () -> Unit,
    onShop: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.displayCutout),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFF5F2A7F).copy(alpha = 0.85f),
                    tonalElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(painter = painterResource(id = R.drawable.egg), contentDescription = null, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = state.eggs.toString(), color = Color.White, style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }

            GameTitle()
            Image(
                painter = painterResource(id = when (state.selectedSkinId) {
                    "cooker" -> R.drawable.chicken_2_egg
                    "hero" -> R.drawable.chicken_3_egg
                    else -> R.drawable.chicken_1_egg
                }),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(240.dp),
                contentScale = ContentScale.FillHeight
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PrimaryButton(text = "START", onClick = onPlay, style = ChickenButtonStyle.Green)
                PrimaryButton(text = "Records", onClick = onRecords, style = ChickenButtonStyle.Magenta)
                PrimaryButton(text = "Shop", onClick = onShop, style = ChickenButtonStyle.Magenta)
            }
        }
    }
}

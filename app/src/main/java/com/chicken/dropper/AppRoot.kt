package com.chicken.dropper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chicken.dropper.ui.screens.GameScreen
import com.chicken.dropper.ui.screens.MainMenuScreen
import com.chicken.dropper.ui.screens.RecordsScreen
import com.chicken.dropper.ui.screens.ResultScreen
import com.chicken.dropper.ui.screens.ShopScreen
import com.chicken.dropper.ui.viewmodel.AudioSettingsViewModel

@Composable
fun AppRoot(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var bestScore by remember { mutableStateOf(0) }
    val audioViewModel: AudioSettingsViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "menu", modifier = modifier) {
        composable("menu") {
            MainMenuScreen(
                onPlay = { navController.navigate("game?showIntro=true") },
                onShop = { navController.navigate("shop") },
                audioSettingsViewModel = audioViewModel
            )
        }
        composable(
            route = "game?showIntro={showIntro}",
            arguments = listOf(navArgument("showIntro") { type = NavType.BoolType; defaultValue = false })
        ) {
            GameScreen(
                onFinish = { score ->
                    bestScore = maxOf(bestScore, score)
                    navController.navigate("result/$score") { popUpTo("menu") { inclusive = false } }
                },
                onQuit = { navController.popBackStack(route = "menu", inclusive = false) },
                audioSettingsViewModel = audioViewModel
            )
        }
        composable("records") {
            RecordsScreen(bestScore = bestScore, onBack = { navController.popBackStack() })
        }
        composable("shop") {
            ShopScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = "result/{score}",
            arguments = listOf(navArgument("score") { type = NavType.IntType })
        ) {
            val score = it.arguments?.getInt("score") ?: 0
            ResultScreen(score = score, onRetry = {
                navController.navigate("game?showIntro=false") { popUpTo("menu") { inclusive = false } }
            }, onMenu = {
                navController.popBackStack(route = "menu", inclusive = false)
            })
        }
    }
}

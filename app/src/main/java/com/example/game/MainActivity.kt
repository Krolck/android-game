package com.example.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.game.ui.GameScreen
import com.example.game.ui.ShopScreen
import com.example.game.ui.theme.GameTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

enum class AppScreen {
    GAME, SHOP
}

class MainActivity : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameTheme {
                LaunchedEffect(Unit) {
                    while (isActive) {
                        delay(1000L)
                        viewModel.addPassiveMoney()
                    }
                }

                GameApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GameApp(viewModel: GameViewModel) {
    var currentScreen by remember { mutableStateOf(AppScreen.GAME) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.GAME,
                    onClick = { currentScreen = AppScreen.GAME },
                    icon = { Text("🎮", fontSize = 20.sp) },
                    label = { Text("Game") }
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.SHOP,
                    onClick = { currentScreen = AppScreen.SHOP },
                    icon = { Text("🛒", fontSize = 20.sp) },
                    label = { Text("Shop") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                AppScreen.GAME -> GameScreen(viewModel = viewModel)
                AppScreen.SHOP -> ShopScreen(viewModel = viewModel)
            }
        }
    }
}

package ru.prod.application.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import ru.prod.application.mainMenu.ui.navigation.TabNavigator

// Экраны приложния(вершины графа)
@Serializable
private object MainMenu



// Основной граф навигации
@Composable
fun StackNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainMenu) {
        composable<MainMenu> {
            TabNavigator()
        }
    }
}
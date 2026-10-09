package com.pemmob.digimonexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemmob.digimonexplorer.ui.screen.DetailScreen
import com.pemmob.digimonexplorer.ui.screen.HomeScreen
import com.pemmob.digimonexplorer.ui.theme.DigimonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DigimonTheme {
                DigimonApp()
            }
        }
    }
}

object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{digimonId}"
    fun detail(id: Int) = "detail/$id"
}

@Composable
fun DigimonApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(onDigimonClick = { id -> navController.navigate(Routes.detail(id)) })
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("digimonId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("digimonId") ?: 0
            DetailScreen(digimonId = id, onBack = { navController.popBackStack() })
        }
    }
}
package com.vadlap.practise3.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vadlap.practise3.presentation.HomeScreen.DetailsScreen
import com.vadlap.practise3.presentation.HomeScreen.HomeScreen
import com.vadlap.practise3.presentation.HomeScreen.WatchScreen
import com.vadlap.practise3.presentation.list.ListScreen // Используем правильный ListScreen
import com.vadlap.practise3.presentation.navigation.BottomNavItem
import com.vadlap.practise3.presentation.settings.SettingsScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onBottomVisibilityChanged: (Boolean) -> Unit
) {
    NavHost(navController, startDestination = BottomNavItem.Home.route, modifier = modifier) {
        composable(BottomNavItem.Home.route) {
            onBottomVisibilityChanged(true)
            HomeScreen(navController = navController)
        }
        composable(BottomNavItem.List.route) {
            onBottomVisibilityChanged(true)
            ListScreen(navController = navController)
        }

        composable(BottomNavItem.Watch.route) {
            onBottomVisibilityChanged(true)
            WatchScreen()
        }

        composable("settings") {
            onBottomVisibilityChanged(false)
            SettingsScreen(navController = navController)
        }

        composable(
            route = "details/{kinoId}",
            arguments = listOf(navArgument("kinoId") { type = NavType.StringType })
        ) { backStackEntry ->
            onBottomVisibilityChanged(false)
            val kinoId = backStackEntry.arguments?.getString("kinoId") ?: ""
            DetailsScreen(navController = navController, kinoId = kinoId)
        }
    }
}

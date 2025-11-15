package com.vadlap.practise3.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vadlap.practise3.HomeScreen.DetailsScreen
import com.vadlap.practise3.HomeScreen.HomeScreen
import com.vadlap.practise3.HomeScreen.ListScreen
import com.vadlap.practise3.HomeScreen.WatchScreen // Импортируем новый экран

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

        // Добавляем обработку для маршрута "watch"
        composable(BottomNavItem.Watch.route) {
            onBottomVisibilityChanged(true)
            WatchScreen()
        }

        composable(
            route = "details/{kinoId}",
            arguments = listOf(navArgument("kinoId") { type = NavType.IntType })
        ) { backStackEntry ->
            onBottomVisibilityChanged(false)
            val kinoId = backStackEntry.arguments?.getInt("kinoId") ?: 0
            DetailsScreen(navController = navController, kinoId = kinoId)
        }
    }
}

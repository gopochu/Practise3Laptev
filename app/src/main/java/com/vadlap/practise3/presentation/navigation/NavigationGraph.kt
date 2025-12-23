package com.vadlap.practise3.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vadlap.practise3.HomeScreen.WatchScreen
import com.vadlap.practise3.presentation.HomeScreen.DetailsScreen
import com.vadlap.practise3.presentation.HomeScreen.HomeScreen
import com.vadlap.practise3.presentation.list.ListScreen
import com.vadlap.practise3.presentation.profile.EditProfileScreen
import com.vadlap.practise3.presentation.profile.ProfileScreen
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
            WatchScreen(navController = navController)
        }
        
        // Экран профиля
        composable(BottomNavItem.Profile.route) {
            onBottomVisibilityChanged(true)
            ProfileScreen(navController = navController)
        }
        
        // Экран редактирования профиля
        composable("edit_profile") {
            onBottomVisibilityChanged(false)
            EditProfileScreen(navController = navController)
        }

        // Экран настроек
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

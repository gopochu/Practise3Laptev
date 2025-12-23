package com.vadlap.practise3.presentation.navigation

sealed class Routes (val route: String){
    object Home : Routes("home")
    object List : Routes("list")
    object Watch : Routes("watch")
}
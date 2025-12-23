package com.vadlap.practise3.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.vadlap.practise3.presentation.navigation.BottomNavigationBar
import com.vadlap.practise3.presentation.navigation.NavigationGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var bottomBarVisibility by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            if (bottomBarVisibility) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            onBottomVisibilityChanged = { isVisible ->
                bottomBarVisibility = isVisible
            }
        )
    }
}

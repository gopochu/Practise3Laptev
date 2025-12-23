package com.vadlap.practise3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vadlap.practise3.presentation.MainScreen
import com.vadlap.practise3.presentation.theme.Practise3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Practise3Theme {
                MainScreen()
            }
        }
    }
}
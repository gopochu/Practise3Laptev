package com.vadlap.practise3.HomeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vadlap.practise3.news.KinoItem
import com.vadlap.practise3.news.KinoRepository

@Composable
fun ListScreen(navController: NavController) {
    val kinoList = KinoRepository.getKinoList()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(kinoList) { kinoItem ->
            KinoCard(kinoItem = kinoItem) {
                // Переходим на экран деталей, передавая ID элемента
                navController.navigate("details/${kinoItem.id}")
            }
        }
    }
}

@Composable
fun KinoCard(kinoItem: KinoItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = kinoItem.title,
            modifier = Modifier.padding(16.dp)
        )
    }
}
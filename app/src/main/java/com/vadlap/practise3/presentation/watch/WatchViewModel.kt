package com.vadlap.practise3.presentation.watch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadlap.practise3.domain.FavoritesUseCase
import com.vadlap.practise3.domain.MovieModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class WatchViewModel(private val favoritesUseCase: FavoritesUseCase) : ViewModel() {

    val favorites: StateFlow<List<MovieModel>> = favoritesUseCase.getFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

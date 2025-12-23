package com.vadlap.practise3.presentation.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadlap.practise3.domain.FavoritesUseCase
import com.vadlap.practise3.domain.GetMovieUseCase
import com.vadlap.practise3.domain.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class DetailsScreenState(
    val isLoading: Boolean = false,
    val movie: MovieModel? = null,
    val isFavorite: Boolean = false,
    val error: String? = null
)

class DetailsViewModel(
    private val getMovieUseCase: GetMovieUseCase,
    private val favoritesUseCase: FavoritesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsScreenState())
    val state = _state.asStateFlow()

    fun loadMovie(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true)
            
            val favoriteMovie = favoritesUseCase.getFavorites().firstOrNull()?.find { it.id == id }

            if (favoriteMovie != null) {
                Log.d("FAVORITES_DEBUG", "Movie $id found in favorites DB.")
                _state.value = DetailsScreenState(movie = favoriteMovie, isFavorite = true)
            } else {
                Log.d("FAVORITES_DEBUG", "Movie $id not in DB, fetching from network.")
                try {
                    val movieFromNet = getMovieUseCase(id)
                    _state.value = DetailsScreenState(
                        movie = movieFromNet,
                        isFavorite = favoritesUseCase.isFavorite(id)
                    )
                } catch (e: Exception) {
                    _state.value = DetailsScreenState(error = "Ошибка загрузки: ${e.message}")
                }
            }
        }
    }

    fun toggleFavorite() {
        val currentMovie = _state.value.movie ?: return
        val isCurrentlyFavorite = _state.value.isFavorite

        viewModelScope.launch(Dispatchers.IO) {
            if (isCurrentlyFavorite) {
                favoritesUseCase.removeFromFavorites(currentMovie.id)
            } else {
                favoritesUseCase.addToFavorites(currentMovie)
            }
            _state.value = _state.value.copy(isFavorite = !isCurrentlyFavorite)
        }
    }
}

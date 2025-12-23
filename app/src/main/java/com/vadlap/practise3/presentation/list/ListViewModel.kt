package com.vadlap.practise3.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadlap.practise3.data.FilterStorage
import com.vadlap.practise3.domain.GetMoviesUseCase
import com.vadlap.practise3.domain.MovieModel
import com.vadlap.practise3.presentation.utils.BadgeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ListScreenState(
    val isLoading: Boolean = false,
    val movies: List<MovieModel> = emptyList(),
    val error: String? = null,
    val showBadge: Boolean = false
)

class ListViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val filterStorage: FilterStorage,
    private val badgeCache: BadgeCache
) : ViewModel() {

    private val _state = MutableStateFlow(ListScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            filterStorage.filterFlow.collectLatest { settings ->
                val showBadge = badgeCache.shouldShowBadge(settings)
                _state.value = _state.value.copy(showBadge = showBadge)
                
                loadMovies(settings.title, settings.type, settings.year)
            }
        }
    }

    private fun loadMovies(title: String, type: String, year: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val movies = getMoviesUseCase(title, type, year)
                if (movies.isNotEmpty()) {
                    _state.value = _state.value.copy(isLoading = false, movies = movies)
                } else {
                    _state.value = _state.value.copy(isLoading = false, movies = emptyList(), error = "Ничего не найдено")
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = "Ошибка: ${e.message}")
            }
        }
    }
}

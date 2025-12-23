package com.vadlap.practise3.domain

import kotlinx.coroutines.flow.Flow

class FavoritesUseCase(private val repository: MoviesRepository) {

    fun getFavorites(): Flow<List<MovieModel>> = repository.getFavoriteMovies()

    suspend fun addToFavorites(movie: MovieModel) = repository.addToFavorites(movie)

    suspend fun removeFromFavorites(movieId: String) = repository.removeFromFavorites(movieId)

    suspend fun isFavorite(movieId: String): Boolean = repository.isFavorite(movieId)
}

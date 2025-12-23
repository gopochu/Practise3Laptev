package com.vadlap.practise3.domain

import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getMovies(title: String, type: String, year: String): List<MovieModel>
    suspend fun getMovie(id: String): MovieModel?

    fun getFavoriteMovies(): Flow<List<MovieModel>>
    suspend fun addToFavorites(movie: MovieModel)
    suspend fun removeFromFavorites(movieId: String)
    suspend fun isFavorite(movieId: String): Boolean
}

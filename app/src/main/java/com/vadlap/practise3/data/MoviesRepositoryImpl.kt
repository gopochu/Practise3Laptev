package com.vadlap.practise3.data

import com.vadlap.practise3.data.db.FavoriteMovieDao
import com.vadlap.practise3.data.db.FavoriteMovieEntity
import com.vadlap.practise3.domain.MovieModel
import com.vadlap.practise3.domain.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRepositoryImpl(
    private val api: Api,
    private val favoriteDao: FavoriteMovieDao
) : MoviesRepository {

    override suspend fun getMovies(title: String, type: String, year: String): List<MovieModel> {
        return api.searchMovies(title, type, year).map { searchItem ->
            MovieModel(
                id = searchItem.imdbId,
                name = searchItem.title,
                description = "${searchItem.year} • ${searchItem.type}", 
                imageUrl = searchItem.poster,
                rating = 0.0
            )
        }
    }

    override suspend fun getMovie(id: String): MovieModel? {
        val movieResponse = api.getMovie(id) ?: return null
        
        if (movieResponse.imdbId == null || movieResponse.title == null) {
            return null
        }

        return MovieModel(
            id = movieResponse.imdbId,
            name = movieResponse.title,
            description = movieResponse.plot ?: "No description available",
            imageUrl = movieResponse.poster ?: "",
            rating = 0.0
        )
    }

    override fun getFavoriteMovies(): Flow<List<MovieModel>> {
        return favoriteDao.getAllFavorites().map { entities ->
            entities.map { entity ->
                MovieModel(
                    id = entity.imdbId,
                    name = entity.title,
                    description = entity.description,
                    imageUrl = entity.posterUrl,
                    rating = 0.0
                )
            }
        }
    }

    override suspend fun addToFavorites(movie: MovieModel) {
        val entity = FavoriteMovieEntity(
            imdbId = movie.id,
            title = movie.name,
            posterUrl = movie.imageUrl,
            description = movie.description
        )
        favoriteDao.insert(entity)
    }

    override suspend fun removeFromFavorites(movieId: String) {
        val entity = favoriteDao.getFavoriteById(movieId)
        if (entity != null) {
            favoriteDao.delete(entity)
        }
    }

    override suspend fun isFavorite(movieId: String): Boolean {
        return favoriteDao.isFavorite(movieId)
    }
}

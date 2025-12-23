package com.vadlap.practise3.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {

    // Получить все избранные фильмы (как поток данных, чтобы UI обновлялся сам)
    @Query("SELECT * FROM favorite_movies")
    fun getAllFavorites(): Flow<List<FavoriteMovieEntity>>

    // Добавить фильм в избранное (если есть - заменить)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: FavoriteMovieEntity)

    // Удалить фильм
    @Delete
    suspend fun delete(movie: FavoriteMovieEntity)

    // Проверить, есть ли фильм в избранном
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE imdbId = :id)")
    suspend fun isFavorite(id: String): Boolean
    
    // Получить фильм по ID (для удаления)
    @Query("SELECT * FROM favorite_movies WHERE imdbId = :id LIMIT 1")
    suspend fun getFavoriteById(id: String): FavoriteMovieEntity?
}

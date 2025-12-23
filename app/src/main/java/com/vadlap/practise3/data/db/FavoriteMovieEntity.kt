package com.vadlap.practise3.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey
    val imdbId: String,
    val title: String,
    val posterUrl: String,
    val description: String
)

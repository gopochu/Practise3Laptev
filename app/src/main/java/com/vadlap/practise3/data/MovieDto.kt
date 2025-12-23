package com.vadlap.practise3.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    @SerialName("imdb_id")
    val imdbId: String,
    @SerialName("title")
    val title: String,
    val year: Int? = null,
    val plot: String? = null,
    val poster: String? = null
)

package com.vadlap.practise3.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponseModel(
    @SerialName("imdbID")
    val imdbId: String? = null,
    @SerialName("Title")
    val title: String? = null,
    @SerialName("Plot")
    val plot: String? = null,
    @SerialName("Poster")
    val poster: String? = null,
    @SerialName("Year")
    val year: String? = null,
    
    @SerialName("Response")
    val response: String? = null,
    @SerialName("Error")
    val error: String? = null
)

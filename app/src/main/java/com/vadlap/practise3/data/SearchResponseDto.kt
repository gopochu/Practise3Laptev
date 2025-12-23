package com.vadlap.practise3.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    @SerialName("Search")
    val search: List<SearchItemDto>? = null,
    @SerialName("totalResults")
    val totalResults: String? = null,
    @SerialName("Response")
    val response: String? = null,
    @SerialName("Error")
    val error: String? = null
)

@Serializable
data class SearchItemDto(
    @SerialName("Title")
    val title: String,
    @SerialName("Year")
    val year: String,
    @SerialName("imdbID")
    val imdbId: String,
    @SerialName("Type")
    val type: String,
    @SerialName("Poster")
    val poster: String
)
